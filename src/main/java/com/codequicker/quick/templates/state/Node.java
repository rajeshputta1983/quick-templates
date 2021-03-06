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
public class Node {
	
	private NodeType type;
	private String variable;
	private List<ExpressionNode> exprNodes;
	private String content;
	private List<Node> children=new ArrayList<Node>();
	private List<Node> elseNodes=new ArrayList<Node>();
	
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
	
	public void setElseNodes(List<Node> elseNodes) {
		this.elseNodes = elseNodes;
	}
	
	public List<Node> getElseNodes() {
		return elseNodes;
	}
	
	public void addElseNode(Node elseNode) {
		this.elseNodes.add(elseNode);
	}
}
