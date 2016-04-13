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

package com.codequicker.quick.templates.processors;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.state.VariableNode;
import com.codequicker.quick.templates.state.VariableType;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class ExpressionHierarchyPreprocessor {

	private Stack<VariableNode> nodeStack=new Stack<VariableNode>();
	
	private Pattern arrayNodePattern=Pattern.compile(".+\\[(\\d+)\\](@.+)?");	
	
	public static void main(String[] args) {
		ExpressionHierarchyPreprocessor tester=new ExpressionHierarchyPreprocessor();
		
		VariableNode root=tester.processExpression("#employee.address.subList(0, 1).size()");
//		tester.processExpression("#list.size().variable1.variable2()");
		
//		tester.processExpression("#list.contains(#map.data(), #map1.data(), #map2.data).mydata.myMethod()");
		
		tester.traverse(root, 1);
	}	
	
	public void traverse(VariableNode node, int tabCount)
	{
		for(int i=1;i<=tabCount;i++)
		{
			System.out.print("\t");
		}
		
		System.out.println(node.getName()+"\t"+node.getType());
		
		List<VariableNode> childList=node.getChildren();
		
		if(childList!=null && childList.size()>0)
		{
			for(VariableNode child: childList)
			{
				traverse(child, tabCount+1);
			}
		}
	}
	
	public void recycle()
	{
		this.nodeStack.clear();
	}
	
	public VariableNode processExpression(String expr)
	{
		this.recycle();
		
		if(!expr.startsWith("$"))
		{
			return null;
		}
		
		int index=0;
		int prevIndex=0;
		
		while(true)
		{
			char ch=expr.charAt(index);
			
			if(ch=='"')
			{
				index=pushLiteral(expr, index+1);
				prevIndex=index+1;
			}
			else if(ch=='.')
			{
				prevIndex=pushVariable(expr, prevIndex, index, true);
			}
			else if(ch=='(')
			{
				if(nodeStack.isEmpty())
					throw new PreprocessorException("unexpected '(' encountered...");
				
				prevIndex=pushMethodCall(expr, prevIndex, index);
			}
			else if(ch==',')
			{
				if(nodeStack.isEmpty())
					throw new PreprocessorException("unexpected ',' encountered...");
				
				prevIndex=pushVariable(expr, prevIndex, index, false);
				
				wrapUpTillRootVariable();
				
				prevIndex=index+1;
			}
			else if(ch==')')
			{
				if(nodeStack.isEmpty())
					throw new PreprocessorException("unexpected ')' encountered...");

				prevIndex=pushVariable(expr, prevIndex, index, false);
				
				int braceStartIndex=TemplateUtil.lookAheadNeglectWhitespace(expr, index+1, '[');
				
				String arrayIndexStr=null;
				
				if(braceStartIndex>0)
				{
					int braceEndIndex= TemplateUtil.scanThroughForTargetChar(expr, braceStartIndex+1, ']');
					
					arrayIndexStr=expr.substring(braceStartIndex+1, braceEndIndex).trim();
					
					index=braceEndIndex;
				}
				
				wrapUpTillMethod(arrayIndexStr);
				
				prevIndex=index+1;
			}

			index++;
			
			if(index>=expr.length())
			{
				prevIndex=pushVariable(expr, prevIndex, index, false);
				break;
			}
		}
		
		wrapUpTillRootVariable();

		if(nodeStack.isEmpty())
		{
			throw new PreprocessorException("invalid expression syntax..."+expr);
		}
		
		return nodeStack.pop();
		
	}
	
	private int pushLiteral(String expr, int index)
	{
		int startIndex=index;
		
		while(true)
		{
			int tmpIndex=expr.indexOf('"', index);
			if(tmpIndex==-1)
			{
				throw new PreprocessorException("literal string is not properly closed...@"+startIndex);
			}
			
			char tmpChar=expr.charAt(tmpIndex-1);
			
			if(tmpChar!='\\')
			{
				VariableNode node=new VariableNode();
				node.setName(expr.substring(startIndex, tmpIndex));
				
				node.setType(VariableType.LITERAL);
				node.setSubType(VariableType.STRING);
				
				node.setStartIndex(startIndex);
				node.setEndIndex(tmpIndex);
				
				nodeStack.push(node);
				
				return tmpIndex;				
			}
			
			index=tmpIndex+1;
		}
	}
	
	private int pushVariable(String expr, int prevIndex, int index, boolean hasReminderPart)
	{
		if(prevIndex>=index)
		{
			return prevIndex+1;
		}
		
		String variableToken=expr.substring(prevIndex, index).trim();
		
		if(variableToken.equals(""))
		{
			throw new PreprocessorException("variable name cannot be empty...");
		}
		
		VariableNode node=new VariableNode();
		node.setName(variableToken);
		
		Matcher matcher=arrayNodePattern.matcher(variableToken);
		
		if(matcher.matches())
		{
			node.setType(variableToken.startsWith("$")?VariableType.ROOT_VARIABLE:VariableType.VARIABLE);
			node.setSubType(VariableType.ARRAY);
		}
		else
		{
			if(variableToken.equals("null"))
			{
				node.setType(VariableType.LITERAL);
				node.setSubType(VariableType.NULL);
			}
			else if(variableToken.equals("true") || variableToken.equals("false"))
			{
				node.setType(VariableType.LITERAL);
				node.setSubType(VariableType.BOOLEAN);
			}
			else
			{
				try
				{
					Integer.parseInt(variableToken);
					
					if(hasReminderPart)
					{
						// scan through double number and return index accordingly
						int tempIndex=scanThroughDoubleEntity(expr, index+1);
						
						String tmpDecimalToken=expr.substring(index+1, tempIndex);
						
						Integer.parseInt(tmpDecimalToken);
						
						node.setName(variableToken+"."+tmpDecimalToken);
	
						node.setType(VariableType.LITERAL);
						node.setSubType(VariableType.DOUBLE);
						
						index=tempIndex;
					}
					else
					{
						node.setType(VariableType.LITERAL);
						node.setSubType(VariableType.INT);
					}
				}
				catch(NumberFormatException nfe)
				{
					node.setType(variableToken.startsWith("$")?VariableType.ROOT_VARIABLE:VariableType.VARIABLE);				
				}
			}
		}
		
		node.setStartIndex(prevIndex);
		node.setEndIndex(index);
		
		nodeStack.push(node);
		
		prevIndex=index+1;
		
		return prevIndex;
	}

	private int scanThroughDoubleEntity(String expr, int index)
	{
		int length=expr.length();
		while(index<length)
		{
			char ch=expr.charAt(index);
			
			if(!Character.isDigit(ch))
			{
				return index;
			}
			
			index++;
		}
		
		return index;
	}
	
	private int pushMethodCall(String expr, int prevIndex, int index)
	{
		String methodNameToken=expr.substring(prevIndex, index).trim();
		
		if(methodNameToken.equals(""))
		{
			throw new PreprocessorException("method name cannot be empty...");
		}
		
		VariableNode node=new VariableNode();
		node.setName(methodNameToken);
		node.setType(VariableType.METHOD_CALL);
		node.setStartIndex(prevIndex);
		node.setEndIndex(index);
		
		nodeStack.push(node);
		prevIndex=index+1;
		
		return prevIndex;
	}
	
	private VariableNode wrapUpTillRootVariable()
	{
		VariableNode current=nodeStack.peek();
		
		if(current.getType()==VariableType.LITERAL)
		{
			nodeStack.pop();
			nodeStack.peek().addChildNode(current);
			return current;
		}
		
		for(;!nodeStack.empty();)
		{
			VariableNode node=nodeStack.pop();
			
			if(node.getType()!=VariableType.ROOT_VARIABLE)
			{
				nodeStack.peek().addChildNode(node);
			}
			else
			{
				if(nodeStack.isEmpty())
				{
					nodeStack.push(node);
				}
				else
				{
					
					nodeStack.peek().addChildNode(node);
				}
				
				return node;
			}
		}
		
		return null;
	}
	
	private VariableNode wrapUpTillMethod(String arrayIndexStr)
	{
		for(;!nodeStack.empty();)
		{
			VariableNode node=nodeStack.pop();
			
			nodeStack.peek().addChildNode(node);
			
			if(node.getType()==VariableType.METHOD_CALL)
			{
				if(arrayIndexStr!=null)
				{
					node.setSubType(VariableType.ARRAY);
					node.setSubTypeContent(arrayIndexStr);
				}
				
				return node;
			}
		}
		
		return null;
	}
	
}
