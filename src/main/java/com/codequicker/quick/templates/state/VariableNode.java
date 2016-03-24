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
public class VariableNode {

	private String name=null;
	private VariableType type=null;
	private VariableType subType=null;
	private List<VariableNode> children=new ArrayList<VariableNode>();
	private int startIndex;
	private int endIndex;
	private String subTypeContent=null;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public VariableType getType() {
		return type;
	}
	
	public void setType(VariableType type) {
		this.type = type;
	}
	
	public void setSubType(VariableType subType) {
		this.subType = subType;
	}
	
	public void setSubTypeContent(String subTypeContent) {
		this.subTypeContent = subTypeContent;
	}
	
	public String getSubTypeContent() {
		return subTypeContent;
	}
	
	public VariableType getSubType() {
		return subType;
	}
	
	public void addChildNode(VariableNode child)
	{
		this.children.add(child);
	}
	
	public List<VariableNode> getChildren() {
		return children;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public int getEndIndex() {
		return endIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder state=new StringBuilder();
		state.append(this.name).append("\t").append(this.type);
		
		return state.toString();
	}
}
