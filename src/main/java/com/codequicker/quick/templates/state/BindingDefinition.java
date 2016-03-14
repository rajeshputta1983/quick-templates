package com.codequicker.quick.templates.state;

import java.util.ArrayList;
import java.util.List;

public class BindingDefinition {

	private String id;
	
	private List<RuleDefinition> ruleDefList=new ArrayList<RuleDefinition>();

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public List<RuleDefinition> getRuleDefList() {
		return ruleDefList;
	}

	public void setRuleDefList(List<RuleDefinition> ruleDefList) {
		this.ruleDefList = ruleDefList;
	}
	
	public void addRuleDefinition(RuleDefinition rule)
	{
		this.ruleDefList.add(rule);
	}
}
