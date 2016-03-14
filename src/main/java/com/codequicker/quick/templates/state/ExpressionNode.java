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

/*
* @author Rajesh Putta
*/
public class ExpressionNode {
	
	private String leftOperand;
	private String operator;
	private String rightOperand;
	private String booleanOperator;
	
	public String getLeftOperand() {
		return leftOperand;
	}
	public void setLeftOperand(String leftOperand) {
		this.leftOperand = leftOperand;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRightOperand() {
		return rightOperand;
	}
	public void setRightOperand(String rightOperand) {
		this.rightOperand = rightOperand;
	}
	public String getBooleanOperator() {
		return booleanOperator;
	}
	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	public boolean isBooleanOperator()
	{
		return (booleanOperator!=null);
	}
	
	public boolean isSingleEntity()
	{
		return (this.operator==null && booleanOperator==null);
	}
	
	public boolean isLogicalExpression()
	{
		return (this.operator!=null && this.leftOperand!=null && this.rightOperand!=null);
	}
	
}
