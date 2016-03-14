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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.codequicker.quick.templates.cache.RuleCache;
import com.codequicker.quick.templates.exceptions.RuleEngineRuntimeException;
import com.codequicker.quick.templates.processors.ExpressionPreprocessor;
import com.codequicker.quick.templates.rules.core.IRuleProcessor;
import com.codequicker.quick.templates.utils.ReflectionUtils;
import com.codequicker.quick.templates.utils.StreamUtils;

/*
* @author Rajesh Putta
*/
public class RulesConfigurationParser extends DefaultHandler {
	
	private String ruleId=null;
	
	private ExpressionPreprocessor exprProcessor=new ExpressionPreprocessor();
	
	private RuleCache cache=null;
	
	public RulesConfigurationParser(String configPath, RuleCache ruleCache){
		this.cache=ruleCache;
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
		
		if(qName.equalsIgnoreCase("rule"))
		{
			RuleConfig config=new RuleConfig();
			
			ruleId=attributes.getValue("id");
			
			config.setRuleId(ruleId);
			config.setExprNodes(exprProcessor.preprocess(attributes.getValue("expr")));
			
			if(this.cache.contains(ruleId))
			{
				throw new RuleEngineRuntimeException("duplicate rule encountered..."+ruleId);
			}
			
			this.cache.set(ruleId, config);
		}
		else if(qName.equalsIgnoreCase("processor"))
		{
			String name=attributes.getValue("name");
			String clazz=attributes.getValue("class");
			
			IRuleProcessor processor=this.cache.getProcessorInstance(clazz);
			
			if(processor==null)
			{
				processor=ReflectionUtils.loadClass(clazz, IRuleProcessor.class);
				this.cache.addProcessorInstance(clazz, processor);
			}
			
			RuleConfig ruleConfig=this.cache.get(ruleId);
			
			if(ruleConfig==null)
			{
				throw new IllegalStateException("Oops...something wrong with configuration file...");
			}
			
			ruleConfig.addProcessor(name, clazz);
		}
	}
}
