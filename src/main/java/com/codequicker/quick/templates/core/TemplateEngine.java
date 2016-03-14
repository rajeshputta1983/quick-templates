package com.codequicker.quick.templates.core;

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
import com.codequicker.quick.templates.state.Node;
import com.codequicker.quick.templates.state.RuleDefinition;
import com.codequicker.quick.templates.utils.TemplateUtil;

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
	
	public String execute(EngineContext context)
	{
		if(this.templateRulesConfiguration==null)
		{
			throw new IllegalStateException("Template Engine is not properly initialized...");
		}
		
		BindingDefinition definition=templateRulesConfiguration.getBindingDefinition((String)context.get("bindingId"));
		
		if(definition==null)
			throw new TemplateRuntimeException("bindingId is not configured in simulator configuration file...");

		
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
		
		return templateEvaluator.evaluate(filePath, context, cache);
	}

	public Object executeSingleEntity(String entity, EngineContext context) {
		
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
		
		String content=source.readContent(entity);
		
		TemplatePreprocessor preprocessor=new TemplatePreprocessor();
		Node rootNode=preprocessor.preprocess(content);
		
		return templateEvaluator.evaluate(rootNode, context);
	}
}
