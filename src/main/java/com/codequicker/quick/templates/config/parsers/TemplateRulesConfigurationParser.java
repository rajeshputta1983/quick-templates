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
import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.parallel.ParallelTemplatePreprocessor;
import com.codequicker.quick.templates.processors.ExpressionPreprocessor;
import com.codequicker.quick.templates.state.BindingDefinition;
import com.codequicker.quick.templates.state.RuleDefinition;
import com.codequicker.quick.templates.utils.StreamUtils;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class TemplateRulesConfigurationParser extends DefaultHandler {
	
	private String rootDirectoryPath=null;
	
	private Map<String, BindingDefinition> definitionMap=new HashMap<String, BindingDefinition>();
	
	private String bindingId=null;
	
	private TemplateCache cache=null;
	
	private ExpressionPreprocessor exprProcessor=new ExpressionPreprocessor();
	
	private ParallelTemplatePreprocessor parallelProcessor=ParallelTemplatePreprocessor.getInstance();
	
	public TemplateRulesConfigurationParser(String configPath, TemplateCache cache){
		this.cache=cache;
		parse(configPath);
	}
	
	public void parse(String configPath)
	{
		this.parallelProcessor.setCache(this.cache);
		
		InputStream stream=StreamUtils.loadStream(configPath);
		
		if(stream==null)
		{
			throw new IllegalStateException("Template Engine is not able to load configuration file...@"+configPath);
		}
		
		SAXParserFactory factory=SAXParserFactory.newInstance();
		
		try {
			SAXParser parser=factory.newSAXParser();
			
			parser.parse(stream, this);
			
			this.parallelProcessor.waitForCompletion();
			
		} catch (ParserConfigurationException e) {
			throw new PreprocessorException(e);
		} catch (SAXException e) {
			throw new PreprocessorException(e);
		} catch (IOException e) {
			throw new PreprocessorException(e);
		} catch(PreprocessorException e) {
			this.parallelProcessor.shutdown();
			throw e;
		}
		finally{
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
			
			if(TemplateUtil.isNullOrEmpty(templatePath))
			{
				throw new PreprocessorException("template path cannot be null or empty...");
			}
			
			def.setPayloadTemplatePath(templatePath);
			
			this.definitionMap.get(this.bindingId).addRuleDefinition(def);

			if(templatePath.endsWith(".qt"))
			{
				parallelProcessor.submitTemplate(this.rootDirectoryPath, templatePath);
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
