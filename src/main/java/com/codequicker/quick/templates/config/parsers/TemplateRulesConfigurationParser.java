/*
 * Copyright 2016 Rajesh Putta
	
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.codequicker.quick.templates.config.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.codequicker.quick.templates.cache.TemplateCache;
import com.codequicker.quick.templates.processors.ExpressionPreprocessor;
import com.codequicker.quick.templates.processors.TemplatePreprocessor;
import com.codequicker.quick.templates.source.FileSource;
import com.codequicker.quick.templates.source.ISource;
import com.codequicker.quick.templates.state.BindingDefinition;
import com.codequicker.quick.templates.state.Node;
import com.codequicker.quick.templates.state.RuleDefinition;
import com.codequicker.quick.templates.utils.StreamUtils;

/*
* @author Rajesh Putta
*/
public class TemplateRulesConfigurationParser extends DefaultHandler {
	
	private String rootDirectoryPath=null;
	
	private Map<String, BindingDefinition> definitionMap=new HashMap<String, BindingDefinition>();
	
	private String bindingId=null;
	
	private TemplateCache cache=null;
	
	private ExpressionPreprocessor exprProcessor=new ExpressionPreprocessor();
	
	public TemplateRulesConfigurationParser(String configPath, TemplateCache cache){
		this.cache=cache;
		parse(configPath);
	}
	
	public void parse(String configPath)
	{
		InputStream stream=this.getClass().getResourceAsStream(configPath);
		
		SAXParserFactory factory=SAXParserFactory.newInstance();
		
		try {
			SAXParser parser=factory.newSAXParser();
			
			parser.parse(stream, this);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			StreamUtils.closeStreams(stream, null);
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(qName.equalsIgnoreCase("payload"))
		{
			this.rootDirectoryPath=attributes.getValue("rootDirectory");
		}
		else if(qName.equalsIgnoreCase("binding"))
		{
			this.bindingId=attributes.getValue("id");
			
			this.definitionMap.put(this.bindingId, new BindingDefinition());
		}
		else if(qName.equalsIgnoreCase("rule"))
		{
			RuleDefinition def=new RuleDefinition();
			def.setExprNodes(exprProcessor.preprocess(attributes.getValue("expr")));
			
			String templatePath=attributes.getValue("template");
			
			def.setPayloadTemplatePath(templatePath);
			
			this.definitionMap.get(this.bindingId).addRuleDefinition(def);
			
			if(!this.cache.contains(templatePath))
			{
				ISource source=new FileSource();
				String content=source.readContent(this.rootDirectoryPath + File.separator+ templatePath);
				
				TemplatePreprocessor preprocessor=new TemplatePreprocessor();
				Node rootNode=preprocessor.preprocess(content);
				
				this.cache.set(templatePath, rootNode);
			}
		}
	}
	
	public String getBasePath()
	{
		return rootDirectoryPath;
	}
	
	public BindingDefinition getBindingDefinition(String bindingId)
	{
		return this.definitionMap.get(bindingId);
	}

}
