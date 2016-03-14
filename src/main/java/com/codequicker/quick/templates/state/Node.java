package com.codequicker.quick.templates.state;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private NodeType type;
	private String variable;
	private List<ExpressionNode> exprNodes;
	private String content;
	private List<Node> children=new ArrayList<Node>();
	
	private int startIndex;
	private int endIndex;
	
	public NodeType getType() {
		return type;
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}

	public void setExprNodes(List<ExpressionNode> exprNodes) {
		this.exprNodes = exprNodes;
	}
	
	public List<ExpressionNode> getExprNodes() {
		return exprNodes;
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	public void addChild(Node child)
	{
		this.children.add(child);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getVariable() {
		return variable;
	}
}
