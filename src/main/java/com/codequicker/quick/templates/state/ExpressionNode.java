package com.codequicker.quick.templates.state;

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
