package com.codequicker.quick.templates.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.codequicker.quick.templates.config.parsers.RuleConfig;
import com.codequicker.quick.templates.rules.core.IRuleProcessor;

public class RuleCache {

	private Map<String, RuleConfig> preprocessedRuleCache=new LinkedHashMap<String, RuleConfig>();
	
	private Map<String, IRuleProcessor> processorInstanceCache=new HashMap<String, IRuleProcessor>(); 
	
	public RuleCache(){
	}

	public void set(String ruleId, RuleConfig ruleConfig)
	{
		this.preprocessedRuleCache.put(ruleId, ruleConfig);
	}
	
	public RuleConfig get(String ruleId)
	{
		return this.preprocessedRuleCache.get(ruleId);
	}
	
	public boolean contains(String ruleId)
	{
		return this.preprocessedRuleCache.containsKey(ruleId);
	}
	
	public Map<String, RuleConfig> getPreprocessedRuleCache() {
		return preprocessedRuleCache;
	}
	
	public void addProcessorInstance(String name, IRuleProcessor processor)
	{
		this.processorInstanceCache.put(name, processor);
	}
	
	public IRuleProcessor getProcessorInstance(String name)
	{
		return this.processorInstanceCache.get(name);
	}
	
	public boolean containsProcessorInstance(String name)
	{
		return this.processorInstanceCache.containsKey(name);
	}
	
	public Map<String, IRuleProcessor> getProcessorInstanceCache() {
		return processorInstanceCache;
	}
	
}
