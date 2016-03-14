package com.codequicker.quick.templates.processors;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.data.ContextProcessingHelper;
import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.ExpressionNode;
import com.codequicker.quick.templates.state.EngineContext;

public class ExpressionEvaluator {
	
	private ContextProcessingHelper lookupHandler=new ContextProcessingHelper();
	
	private Pattern quoteString=Pattern.compile("(\"|\')(.+)(\"|\')");
	
	public Object evaluateAsArray(List<ExpressionNode> exprNodeList, EngineContext context)
	{
		if(exprNodeList.size()>1)
		{
			throw new TemplateRuntimeException("two or more expressions cannot be resulted as one array...");
		}
		
		ExpressionNode exprNode=exprNodeList.get(0);
		
		if(!exprNode.isSingleEntity())
		{
			throw new TemplateRuntimeException("expression is not of type array...");
		}
		
		return lookupHandler.lookup(exprNode.getLeftOperand(), context, true);
	}
	
	public String evaluateAsPrimitive(List<ExpressionNode> exprNodeList, EngineContext context)
	{
		if(exprNodeList.size()==1)
		{
			ExpressionNode exprNode=exprNodeList.get(0);
			
			return (String)lookupHandler.lookup(exprNode.getLeftOperand(), context, false);
		}
		else
		{
			boolean result=evaluateAsBoolean(exprNodeList, context);
			
			return String.valueOf(result);
		}
	}
	
	
	public boolean evaluateAsBoolean(List<ExpressionNode> exprNodeList, EngineContext context)
	{
		Boolean prevResult=null;
		
		boolean operatorFlag=false;
		
		for(ExpressionNode exprNode: exprNodeList)
		{
			if(exprNode.isLogicalExpression())
			{
				Object leftValue=lookupHandler.lookup(exprNode.getLeftOperand(), context, false);
				
				String operator=exprNode.getOperator();
				
				Object rightValue=lookupHandler.lookup(exprNode.getRightOperand(), context, false);
				
				boolean result=evaluateExpression(leftValue, operator, rightValue);
				
				if(prevResult==null)
				{
					prevResult=result;
				}
				else
				{
					if(operatorFlag)
					{
						if(!prevResult)
						{
							break;
						}
							
						prevResult&=result;
					}
					else
					{
						if(prevResult)
						{
							break;
						}
						
						prevResult|=result;
					}
				}
			}
			else if(exprNode.isBooleanOperator())
			{
				String operator=exprNode.getBooleanOperator();
				
				if(operator.equalsIgnoreCase("and") || operator.equals("&&"))
				{
					operatorFlag=true;
				}
				else if(operator.equalsIgnoreCase("or") || operator.equals("||"))
				{
					operatorFlag=false;
				}
			}
			else if(exprNode.isSingleEntity())
			{
				String result=(String)lookupHandler.lookup(exprNode.getLeftOperand(), context, false);
				
				boolean booleanResult=(result!=null && result.equalsIgnoreCase("true"))?true:false;
				
				if(prevResult==null)
				{
					prevResult=booleanResult;
				}
				else
				{
					if(operatorFlag)
					{
						if(!prevResult)
						{
							break;
						}
							
						prevResult&=booleanResult;
					}
					else
					{
						if(prevResult)
						{
							break;
						}
						
						prevResult|=booleanResult;
					}
				}

					
			}
		}
		
		
		return prevResult;
	}
	
	private boolean evaluateExpression(Object leftValue, String operator, Object rightValue)
	{
		String lVal=String.valueOf(leftValue);
		String rVal=String.valueOf(rightValue);
		
		Matcher matcher=quoteString.matcher(lVal);
		
		if(matcher.matches())
		{
			lVal=lVal.substring(1, lVal.length()-1);
		}
		
		matcher=quoteString.matcher(rVal);
		
		if(matcher.matches())
		{
			rVal=rVal.substring(1, rVal.length()-1);
		}
		
		if(operator.equals("~="))
		{
			return lVal.equalsIgnoreCase(rVal);
		}
		else if(operator.equals("=="))
		{
			return lVal.equals(rVal);
		}
		else if(operator.equals("!="))
		{
			return !lVal.equals(rVal);
		}
		else
		{
			try
			{
				double num1=Double.parseDouble(String.valueOf(lVal));
				double num2=Double.parseDouble(String.valueOf(rVal));
				
				if(operator.equals("<="))
				{
					return num1<=num2;
				}
				else if(operator.equals(">="))
				{
					return num1>=num2;
				}
				else if(operator.equals("<"))
				{
					return num1<num2;
				}
				else if(operator.equals(">"))
				{
					return num1>num2;
				}
			}
			catch (Exception e) {
			}
		}
		
		return false;
	}
}
