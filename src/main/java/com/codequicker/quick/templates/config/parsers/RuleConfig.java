package com.codequicker.quick.templates.config.parsers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.codequicker.quick.templates.state.ExpressionNode;

public class RuleConfig {
	
	private String ruleId;
	private List<ExpressionNode> exprNodes;
	private Map<String, String> processors=new LinkedHashMap<String, String>();
	
	public String getRuleId() {
		return ruleId;
	}
	
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	public List<ExpressionNode> getExprNodes() {
		return exprNodes;
	}
	
	public void setExprNodes(List<ExpressionNode> exprNodes) {
		this.exprNodes = exprNodes;
	}

	public Map<String, String> getProcessors() {
		return processors;
	}
	
	public void addProcessor(String processorName, String processorClass)
	{
		this.processors.put(processorName, processorClass);
	}
	
	public String getProcessor(String processorName)
	{
		return this.processors.get(processorName);
	}
}
