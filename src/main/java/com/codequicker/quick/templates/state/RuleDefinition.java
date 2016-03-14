package com.codequicker.quick.templates.state;

import java.util.List;

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
