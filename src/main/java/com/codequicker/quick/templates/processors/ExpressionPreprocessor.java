package com.codequicker.quick.templates.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.state.ExpressionNode;
import com.codequicker.quick.templates.utils.TemplateUtil;

public class ExpressionPreprocessor {
	
	private List<ExpressionNode> nodeList=null;
	
	private Pattern exprPattern=Pattern.compile("([^\\s]+(\\.)?)+(\\s*)((~=|==|!=|<=|>=|<|>)(\\s*)([^\\s]+(\\.)?)+)?", Pattern.CASE_INSENSITIVE);
	
	private Pattern operatorPattern=Pattern.compile("(\\s*)(~=|==|!=|<=|>=|<|>)(\\s*)");
	
	private Pattern booleanOperatorPattern=Pattern.compile("(\\s*)(and|or|&&|\\|\\|)(\\s*)", Pattern.CASE_INSENSITIVE);
	
	public List<ExpressionNode> preprocess(String expr)
	{
		if(TemplateUtil.isNullOrEmpty(expr))
			throw new PreprocessorException("expression used with #if or #for keywords cannot be null or empty...");
		
		this.nodeList=new ArrayList<ExpressionNode>();
		
		Matcher matcher=exprPattern.matcher(expr);
		
		while(matcher.find())
		{
			String subExpr=matcher.group();
			
			Matcher booleanMatcher=booleanOperatorPattern.matcher(subExpr);
			
			ExpressionNode exprNode=new ExpressionNode();
			
			if(booleanMatcher.matches())
			{
				exprNode.setBooleanOperator(subExpr.trim());
			}
			else
			{
				Matcher operatorMatcher=operatorPattern.matcher(subExpr);
				
				if(operatorMatcher.find())
				{
					int opStartIndex=operatorMatcher.start();
					int opEndIndex=operatorMatcher.end();
					
					String leftOperand=subExpr.substring(0, opStartIndex);
					String rightOperand=subExpr.substring(opEndIndex);
					
					exprNode.setLeftOperand(leftOperand.trim());
					exprNode.setRightOperand(rightOperand.trim());
					exprNode.setOperator(operatorMatcher.group().trim());
				}
				else
				{
					exprNode.setLeftOperand(subExpr.trim());
				}
			}
			
			nodeList.add(exprNode);
		}
		
		return nodeList;
	}
}
