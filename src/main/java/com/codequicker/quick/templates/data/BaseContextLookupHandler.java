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

package com.codequicker.quick.templates.data;

import java.util.ArrayList;
import java.util.List;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.VariableNode;
import com.codequicker.quick.templates.state.VariableType;
import com.codequicker.quick.templates.utils.ReflectionUtils;

/*
* @author Rajesh Putta
*/
public class BaseContextLookupHandler implements ILookupHandler {

	private DataLookupHandlerChain lookupHandlerChain=DataLookupHandlerChain.getInstance();
	
	public Class<?> getType() {
		return null;
	}
	
	public Object lookup(String key, VariableNode exprNode, EngineContext context, boolean returnArrayType) {

		Object contextReference=context.getContext();

		contextReference=processRecursively(contextReference, null, exprNode, context);

		return this.lookupHandlerChain.returnFinalResult(contextReference, returnArrayType, key);
	}
	
	private Object processRecursively(Object context, Object carriedValue, VariableNode node, EngineContext engineContext)
	{
		boolean processChildren=true;
		
		if(node.getType()==VariableType.LITERAL)
		{
			String tmp=node.getName();
			
			VariableType tmpType=node.getSubType();
			
			switch(tmpType)
			{
			case STRING: carriedValue=tmp;
						 break;
			case INT: carriedValue=Integer.parseInt(tmp);
						break;
			case DOUBLE: carriedValue=Double.parseDouble(tmp);
						break;
			case BOOLEAN: carriedValue=Boolean.parseBoolean(tmp);
						break;
			case NULL: carriedValue=null;
						break;
			}
			
			processChildren=false;
		}
		else if(node.getType()==VariableType.ROOT_VARIABLE)
		{
			int index=-1;

			String tmpName=node.getName().substring(1);
			
			if(node.getSubType()==VariableType.ARRAY)
			{
				int startIndex=tmpName.indexOf("[");
				
				index=Integer.parseInt(tmpName.substring(startIndex+1, tmpName.indexOf("]", startIndex+1)));
				
				tmpName=tmpName.substring(0, startIndex);
			}

			if(tmpName.endsWith("$index"))
			{
				return engineContext.getVariable(tmpName);
			}				
			
			if(engineContext.getVariable(tmpName)==null)
			{
				context=engineContext.getContext();
			}
			else
			{
				context=engineContext.getVariableContext();
			}
			
			carriedValue = lookupHandlerChain.lookupFromMap(context, tmpName, false);

			carriedValue = processArray(node, carriedValue, index);
		}
		else if(node.getType()==VariableType.VARIABLE)
		{
			int index=-1;
			
			String tmpName=node.getName();
			
			if(node.getSubType()==VariableType.ARRAY)
			{
				int startIndex=tmpName.indexOf("[");
				
				index=Integer.parseInt(tmpName.substring(startIndex+1, tmpName.indexOf("]", startIndex+1)));
				
				tmpName=tmpName.substring(0, startIndex);
			}

			carriedValue = lookupHandlerChain.lookupFromMap(carriedValue, tmpName, true);
			
			carriedValue=processArray(node, carriedValue, index);
		}
		else if(node.getType()==VariableType.METHOD_CALL)
		{
			processChildren=false;
			
			List<VariableNode> childList=node.getChildren();
			
			List<Object> paramList=new ArrayList<Object>();
			List<Class<?>> classList=new ArrayList<Class<?>>();
			
			for(VariableNode child:childList)
			{
				Object tmp=processRecursively(context, null, child, engineContext);
				paramList.add(tmp);
				classList.add(tmp.getClass());
			}
			
			carriedValue=this.lookupHandlerChain.lookupAsPrimitive(carriedValue);
			
			carriedValue=ReflectionUtils.invokeMethod(carriedValue, node.getName(), classList, paramList);
			
			if(node.getSubType()!=null && node.getSubType()==VariableType.ARRAY)
			{
				int index=-1;
				
				try
				{
					index=Integer.parseInt(node.getSubTypeContent());
				}catch(NumberFormatException e)
				{
					throw new TemplateRuntimeException("number expected as array index...found..."+node.getSubTypeContent()+"\tMethod call..."+node.getName());
				}
				
				carriedValue=processArray(node, carriedValue, index);
			}
		}

		if(processChildren)
		{
			List<VariableNode> childList=node.getChildren();
			
			for(VariableNode child:childList)
			{
				carriedValue=processRecursively(context, carriedValue, child, engineContext);
			}
		}
		
		return carriedValue;
	}
	
	private Object processArray(VariableNode node, Object carriedValue, int index)
	{
		if(index==-1)
			return carriedValue;
		
		return this.lookupHandlerChain.lookupFromList(carriedValue, node.getName(), index);
	}
}
