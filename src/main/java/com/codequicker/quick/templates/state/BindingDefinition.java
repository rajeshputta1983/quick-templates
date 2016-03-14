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

package com.codequicker.quick.templates.state;

import java.util.ArrayList;
import java.util.List;

/*
* @author Rajesh Putta
*/
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
