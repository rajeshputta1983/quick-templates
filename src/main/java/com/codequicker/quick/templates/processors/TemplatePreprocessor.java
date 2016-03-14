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

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.Node;
import com.codequicker.quick.templates.state.NodeType;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class TemplatePreprocessor {
	
	private ExpressionPreprocessor exprProcessor=new ExpressionPreprocessor();
	
	private Stack<Node> nodeStack=new Stack<Node>();
	
	private Pattern forExprPattern=Pattern.compile("[^\\s]+\\s+(in)\\s+([^\\s]+(\\.)?)+", Pattern.CASE_INSENSITIVE);
	
	private Pattern keywordPattern=Pattern.compile("(#for\\s*\\()|(#if\\s*\\()|(#end)|(#\\{)");
	
	private int lastIndex=0;
	
	public Node preprocess(String content)
	{
		Node rootNode=new Node();
		rootNode.setType(NodeType.ROOT);
		nodeStack.push(rootNode);
		
		Matcher matcher=keywordPattern.matcher(content);
		
		while(matcher.find())
		{
			String keyword=matcher.group();
			
			if(matcher.start()>lastIndex)
			{
				Node textNode=new Node();
				textNode.setType(NodeType.TEXT);
				textNode.setStartIndex(lastIndex);
				textNode.setEndIndex(matcher.start());
				textNode.setContent(content.substring(lastIndex, matcher.start()));
				
				if(!nodeStack.empty())
				{
					Node parent=nodeStack.peek();
					parent.addChild(textNode);
				}
				else
					nodeStack.push(textNode);
			}
			
			lastIndex=handleKeyword(content, keyword, matcher.start(), matcher.end());
		}
		
		// process last chunk
		if(lastIndex<content.length())
		{
			Node textNode=new Node();
			textNode.setType(NodeType.TEXT);
			textNode.setStartIndex(lastIndex);
			textNode.setEndIndex(content.length());
			textNode.setContent(content.substring(lastIndex, content.length()));
			
			if(!nodeStack.empty())
			{
				Node parent=nodeStack.peek();
				parent.addChild(textNode);
			}
		}
		
		if(nodeStack.size()>1)
		{
			// TO DO :: add tags and position level details for better debugging
			
			throw new PreprocessorException("Template is syntactically not correct...\n"+content);
		}
		
		return rootNode;
	}
	
	private int handleKeyword(String content, String keyword, int startIndex, int endIndex)
	{
		int bodyIndex=startIndex;
		
		Node node=new Node();
		
		if(keyword.startsWith("#for"))
			node.setType(NodeType.FOR);
		else if(keyword.startsWith("#if"))
			node.setType(NodeType.IF);
		else if(keyword.startsWith("#{"))
			node.setType(NodeType.EXPR);
		else if(keyword.startsWith("#end"))
		{
			bodyIndex=startIndex+4;
			
			Node current=nodeStack.pop();
			
			if(!nodeStack.empty())
			{
				Node parent=nodeStack.peek();
				parent.addChild(current);
			}
			else
				nodeStack.push(current);
		}
		
		if(keyword.startsWith("#if"))
		{
			node.setStartIndex(startIndex);
			
			bodyIndex=content.indexOf(")", startIndex)+1;
			
			node.setExprNodes(exprProcessor.preprocess(content.substring(endIndex, bodyIndex-1).trim()));

			nodeStack.push(node);
		}
		else if(keyword.startsWith("#for"))
		{
			node.setStartIndex(startIndex);
			
			bodyIndex=content.indexOf(")", startIndex)+1;
			
			String expr=content.substring(endIndex, bodyIndex-1).trim();
			
			Matcher forExprMatcher=forExprPattern.matcher(expr);
			
			if(forExprMatcher.matches())
			{
				String[] parts=expr.split("\\s+(in)\\s+");
				
				if(TemplateUtil.isReservedWord(parts[0]))
				{
					throw new TemplateRuntimeException("Reserved keyword cannot be used as variable in for loop..."+parts[0]);
				}
				
				node.setVariable(parts[0]);
				
				node.setExprNodes(exprProcessor.preprocess(parts[1]));
			}
			else
				throw new TemplateRuntimeException("invalid expression format in for loop...");
			
			nodeStack.push(node);
		}
		else if(keyword.startsWith("#{"))
		{
			node.setStartIndex(startIndex);
			
			bodyIndex=content.indexOf("}", startIndex)+1;
			
			node.setExprNodes(exprProcessor.preprocess(content.substring(endIndex, bodyIndex-1).trim()));

			if(!nodeStack.empty())
			{
				Node parent=nodeStack.peek();
				parent.addChild(node);
			}
		}
		
		return bodyIndex;
	}
}
