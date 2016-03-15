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

import java.util.Iterator;
import java.util.List;

import com.codequicker.quick.templates.cache.TemplateCache;
import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.Node;
import com.codequicker.quick.templates.state.NodeType;

/*
* @author Rajesh Putta
*/
public class TemplateEvaluator {

	private ExpressionEvaluator exprEvaluator=new ExpressionEvaluator();
	
	public String evaluate(Node rootNode, EngineContext context)
	{
		StringBuilder content=new StringBuilder();
		
		traverse(rootNode, content, context);
		
		return content.toString();
	}
	
	public String evaluate(String filePath, EngineContext context, TemplateCache cache)
	{
		Node rootNode=cache.get(filePath);
		
		if(rootNode==null)
			throw new TemplateRuntimeException("Template Cache is not initialized properly...");
		
		StringBuilder content=new StringBuilder();
		
		traverse(rootNode, content, context);
		
		return content.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private void traverse(Node node, StringBuilder content, EngineContext context)
	{
		boolean traverseChildren=true;
		
		if(node.getType()==NodeType.TEXT)
		{
			content.append(node.getContent());
		}
		else if(node.getType()==NodeType.IF)
		{
			boolean result=exprEvaluator.evaluateAsBoolean(node.getExprNodes(), context);
			
			if(!result)
			{
				List<Node> nodeList=node.getElseNodes();
				
				for(int nodeIndex=0;nodeIndex<nodeList.size();nodeIndex++)
				{
					Node elseNode=nodeList.get(nodeIndex);
					
					boolean elseNodeFlag=false;
					boolean elseResult=false;
					
					if(elseNode.getType()==NodeType.ELSE)
						elseNodeFlag=true;
					else
						elseResult=exprEvaluator.evaluateAsBoolean(elseNode.getExprNodes(), context);
					
					if(elseResult || elseNodeFlag)
					{
						traverseChildren(elseNode, content, context);
						break;
					}
				}
				
				traverseChildren=false;
			}
		}
		else if(node.getType()==NodeType.SWITCH)
		{
			String result=exprEvaluator.evaluateAsPrimitive(node.getExprNodes(), context);
			
			Node defaultCase=null;
			
			boolean caseFound=false;
			
			for(Node caseNode: node.getChildren())
			{
				if(caseNode.getType()==NodeType.DEFAULT)
				{
					defaultCase=caseNode;
					continue;
				}
					
				String caseContent=caseNode.getContent();
				
				if(caseContent.equalsIgnoreCase(result))
				{
					traverseChildren(caseNode, content, context);
					caseFound=true;
					break;
				}
			}
			
			if(!caseFound && defaultCase!=null)
			{
				traverseChildren(defaultCase, content, context);
			}
			
			traverseChildren=false;
		}
		else if(node.getType()==NodeType.FOR)
		{
			traverseChildren=false;
			
			Iterator listIterator=(Iterator)exprEvaluator.evaluateAsArray(node.getExprNodes(), context);
			
			int loopCounter=1;
			
			while(listIterator.hasNext())
			{
				Object element=listIterator.next();
				
				context.setVariable(node.getVariable(), element);
				context.setVariable(node.getVariable()+"$index", String.valueOf(loopCounter));
				
				traverseChildren(node, content, context);
				
				context.removeVariable(node.getVariable());
				context.removeVariable(node.getVariable()+"$index");
				
				loopCounter++;
			}
		}
		else if(node.getType()==NodeType.EXPR)
		{
			content.append(exprEvaluator.evaluateAsPrimitive(node.getExprNodes(), context));
		}
		
		if(traverseChildren)
		{
			traverseChildren(node, content, context);
		}
	}
	
	private void traverseChildren(Node node, StringBuilder content, EngineContext context)
	{
		for(Node child: node.getChildren())
		{
			traverse(child, content, context);
		}		
	}	
}
