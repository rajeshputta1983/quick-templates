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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codequicker.quick.templates.cache.RuleCache;
import com.codequicker.quick.templates.config.parsers.RuleConfig;
import com.codequicker.quick.templates.config.parsers.RulesConfigurationParser;
import com.codequicker.quick.templates.processors.ExpressionEvaluator;
import com.codequicker.quick.templates.processors.ExpressionPreprocessor;
import com.codequicker.quick.templates.rules.core.IRuleProcessor;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.ExpressionNode;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class RuleEngine implements IEngine {
	
	private static final Logger LOGGER=Logger.getLogger(RuleEngine.class.getName());
	
	private RulesConfigurationParser rulesConfigurationParser=null;
	
	private ExpressionEvaluator exprEvaluator=new ExpressionEvaluator();
	
	private ExpressionPreprocessor exprProcessor=new ExpressionPreprocessor();
	
	private RuleCache ruleCache=new RuleCache();
	
	public void initialize(String configPath)
	{
		if(TemplateUtil.isNullOrEmpty(configPath))
		{
			throw new IllegalArgumentException("Rule configuration file path cannot be null or empty...");
		}
		
		this.rulesConfigurationParser=new RulesConfigurationParser(configPath, ruleCache);
		
		LOGGER.log(Level.INFO, "Rule configuration is initialized successfully...");
	}
	
	public EngineResponse execute(EngineContext context)
	{
		if(this.rulesConfigurationParser==null)
		{
			throw new IllegalStateException("Rule Engine is not properly initialized...");
		}
		
		Map<String, RuleConfig> ruleConfigCache=this.ruleCache.getPreprocessedRuleCache();
		
		Set<Entry<String, RuleConfig>> ruleEntrySet=ruleConfigCache.entrySet();
		
		boolean isAnyRuleMatched=false;
		
		for(Entry<String, RuleConfig> ruleEntry: ruleEntrySet)
		{
			String ruleId=ruleEntry.getKey();
			RuleConfig ruleConfig=ruleEntry.getValue();
			
			boolean result=exprEvaluator.evaluateAsBoolean(ruleConfig.getExprNodes(), context);
			
			if(!result)
			{
				continue;
			}
			
			isAnyRuleMatched=true;
			
			LOGGER.log(Level.INFO, "executing processors of rule id..."+ruleId);
			
			Collection<String> processorNames=ruleConfig.getProcessors().values();
			
			for(String processor: processorNames)
			{
				IRuleProcessor processorInstance=this.ruleCache.getProcessorInstance(processor);
				
				processorInstance.process(context);
			}
		}
		
		LOGGER.log(Level.INFO, "Any Rule matched with context ?..."+isAnyRuleMatched);
		
		return null;
	}

	public Object executeSingleEntity(String ruleExpr, EngineContext context) {
		
		if(TemplateUtil.isNullOrEmpty(ruleExpr))
		{
			throw new IllegalArgumentException("Rule Expression cannot be null or empty...");
		}
		
		List<ExpressionNode> exprNodeList=exprProcessor.preprocess(ruleExpr);
		
		return exprEvaluator.evaluateAsBoolean(exprNodeList, context);
	}
}
