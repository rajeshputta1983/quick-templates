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

import java.util.List;

/*
* @author Rajesh Putta
*/
public class RuleDefinition {
	
	private List<ExpressionNode> exprNodes;
	
	private String payloadTemplatePath;

	public void setExprNodes(List<ExpressionNode> exprNodes) {
		this.exprNodes = exprNodes;
	}
	
	public List<ExpressionNode> getExprNodes() {
		return exprNodes;
	}

	public String getPayloadTemplatePath() {
		return payloadTemplatePath;
	}

	public void setPayloadTemplatePath(String payloadTemplatePath) {
		this.payloadTemplatePath = payloadTemplatePath;
	}
}
