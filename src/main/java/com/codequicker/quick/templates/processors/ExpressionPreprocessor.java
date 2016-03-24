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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.state.ExpressionNode;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class ExpressionPreprocessor {
	
	private List<ExpressionNode> nodeList=null;
	
//	private Pattern exprPattern=Pattern.compile("([^\\s]+(\\.)?)+(\\s*)((~=|==|!=|<=|>=|<|>)(\\s*)([^\\s]+(\\.)?)+)?", Pattern.CASE_INSENSITIVE);
	
	private Pattern operatorPattern=Pattern.compile("(\\s*)(~=|==|!=|<=|>=|<|>)(\\s*)");
	
	private Pattern booleanOperatorPattern=Pattern.compile("(\\s+)(and|or|&&|\\|\\|)(\\s+)", Pattern.CASE_INSENSITIVE);
	
	public ExpressionPreprocessor() {
	}
	
	public List<ExpressionNode> preprocess(String expr)
	{
		if(TemplateUtil.isNullOrEmpty(expr))
			throw new PreprocessorException("expression used with #if or #for keywords cannot be null or empty...");

		ExpressionHierarchyPreprocessor preProcessor=new ExpressionHierarchyPreprocessor();
		
		this.nodeList=new ArrayList<ExpressionNode>();
		
		Matcher booleanMatcher=booleanOperatorPattern.matcher(expr);
		
		int prevIndex=0;
		
		while(booleanMatcher.find())
		{
			int booleanStartIndex=booleanMatcher.start();
			
			String subExpr=expr.substring(prevIndex, booleanStartIndex);
			
			prevIndex=booleanMatcher.end();

			processExpression(preProcessor, subExpr);
			
			ExpressionNode exprNode=new ExpressionNode();
			exprNode.setBooleanOperator(booleanMatcher.group().trim());
			
			nodeList.add(exprNode);
		}
		
		processExpression(preProcessor, expr.substring(prevIndex));
		
		return nodeList;
	}
	
	private void processExpression(ExpressionHierarchyPreprocessor preProcessor, String subExpr)
	{
		Matcher operatorMatcher=operatorPattern.matcher(subExpr);
		
		ExpressionNode exprNode=new ExpressionNode();
		
		if(operatorMatcher.find())
		{
			int opStartIndex=operatorMatcher.start();
			int opEndIndex=operatorMatcher.end();
			
			String leftOperand=subExpr.substring(0, opStartIndex);
			String rightOperand=subExpr.substring(opEndIndex);
			
			
			exprNode.setLeftOperand(leftOperand.trim());

			exprNode.setLeftNode(preProcessor.processExpression(leftOperand.trim()));
			
			exprNode.setRightOperand(rightOperand.trim());

			exprNode.setRightNode(preProcessor.processExpression(rightOperand.trim()));
			
			exprNode.setOperator(operatorMatcher.group().trim());
		}
		else
		{
			exprNode.setLeftOperand(subExpr.trim());
			
			exprNode.setLeftNode(preProcessor.processExpression(subExpr.trim()));
		}
		
		nodeList.add(exprNode);
	}
}
