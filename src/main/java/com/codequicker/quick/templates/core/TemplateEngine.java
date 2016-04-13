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

package com.codequicker.quick.templates.core;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codequicker.quick.templates.cache.TemplateCache;
import com.codequicker.quick.templates.config.parsers.TemplateRulesConfigurationParser;
import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.processors.ExpressionEvaluator;
import com.codequicker.quick.templates.processors.TemplateEvaluator;
import com.codequicker.quick.templates.processors.TemplatePreprocessor;
import com.codequicker.quick.templates.source.FileSource;
import com.codequicker.quick.templates.source.ISource;
import com.codequicker.quick.templates.source.UrlSource;
import com.codequicker.quick.templates.state.BindingDefinition;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.ExpressionNode;
import com.codequicker.quick.templates.state.MimeType;
import com.codequicker.quick.templates.state.Node;
import com.codequicker.quick.templates.state.RuleDefinition;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class TemplateEngine implements IEngine {
	
	private static final Logger LOGGER=Logger.getLogger(RuleEngine.class.getName());
	
	private TemplateEvaluator templateEvaluator=new TemplateEvaluator();
	
	private ExpressionEvaluator exprEvaluator=new ExpressionEvaluator();
	
	private TemplateRulesConfigurationParser templateRulesConfiguration=null;
	
	private TemplateCache cache=new TemplateCache();
	
	public TemplateCache getCache()
	{
		return this.cache;
	}
	
	public void initialize(String configPath)
	{
		if(TemplateUtil.isNullOrEmpty(configPath))
		{
			throw new IllegalArgumentException("template configuration file path cannot be null or empty...");
		}
		
		templateRulesConfiguration=new TemplateRulesConfigurationParser(configPath, cache);
		
		LOGGER.log(Level.INFO, "Template configuration is initialized successfully...");		
	}
	
	public EngineResponse execute(EngineContext context)
	{
		if(this.templateRulesConfiguration==null)
		{
			throw new IllegalStateException("Template Engine is not properly initialized...");
		}
		
		BindingDefinition definition=templateRulesConfiguration.getBindingDefinition((String)context.get("bindingId"));
		
		if(definition==null)
			throw new TemplateRuntimeException("bindingId is not configured in configuration file...");
		
		List<RuleDefinition> rules=definition.getRuleDefList();
		
		String filePath=null;

		for(RuleDefinition rule: rules)
		{
			List<ExpressionNode> exprNodeList=rule.getExprNodes();
			
			boolean result=exprEvaluator.evaluateAsBoolean(exprNodeList, context);
			
			if(result)
			{
				filePath=rule.getPayloadTemplatePath();
				break;
			}
		}		
		
		if(TemplateUtil.isNullOrEmpty(filePath))
			throw new TemplateRuntimeException("didn't match with any rule configured..."+context.get("bindingId"));
		
		
		EngineResponse response=new EngineResponse();
		
		
		String extn=TemplateUtil.extractFileExtension(filePath);
		
		if(!extn.equalsIgnoreCase("qt"))
		{
			String completePath=this.templateRulesConfiguration.getBasePath()+File.separator+filePath;
			
			ISource source = new FileSource();
			
			byte[] content=source.readContentAsBinary(completePath);
			
			response.setBinary(true);
			
			response.setContent(content);
			
			response.setMimeType(MimeType.typeOf(extn));
			
			return response;
		}
		
		String content=templateEvaluator.evaluate(filePath, context, cache);
		response.setContent(content);
		response.setMimeType(MimeType.TEXT_PLAIN);
		
		return response;
	}

	public EngineResponse executeSingleEntity(String entity, EngineContext context) {
		
		if(TemplateUtil.isNullOrEmpty(entity))
		{
			throw new IllegalArgumentException("template path cannot be null or empty...");
		}
		
		ISource source=null;
		
		if(entity.startsWith("http://"))
		{
			source=new UrlSource();
		}
		else
		{
			source=new FileSource();
		}
		
		String content=source.readContentAsText(entity);
		
		TemplatePreprocessor preprocessor=new TemplatePreprocessor();
		Node rootNode=preprocessor.preprocess(content);
		
		content=templateEvaluator.evaluate(rootNode, context);
		
		EngineResponse response=new EngineResponse();
		
		response.setContent(content);
		response.setMimeType(MimeType.TEXT_PLAIN);
		
		return response;
	}
}
