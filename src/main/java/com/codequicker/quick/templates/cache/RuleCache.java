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

package com.codequicker.quick.templates.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.codequicker.quick.templates.config.parsers.RuleConfig;
import com.codequicker.quick.templates.rules.core.IRuleProcessor;

/*
 * @author Rajesh Putta
 */
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
