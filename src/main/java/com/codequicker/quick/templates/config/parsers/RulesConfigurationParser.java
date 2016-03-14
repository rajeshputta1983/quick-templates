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
