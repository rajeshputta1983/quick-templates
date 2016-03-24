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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.VariableNode;
import com.codequicker.quick.templates.state.VariableType;
import com.codequicker.quick.templates.utils.ReflectionUtils;

/*
* @author Rajesh Putta
*/
public class MapLookupHandler implements ILookupHandler {
	
	public Class<?> getType() {
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Object lookup(String key, VariableNode exprNode, EngineContext context, Object reference,
			boolean returnArrayType) {

		Object contextReference=(reference!=null)?reference:context.getContext();

		contextReference=processRecursively(contextReference, null, exprNode);
		
		if(returnArrayType)
		{
			if(Iterable.class.isAssignableFrom(contextReference.getClass()))
			{
				return ((Iterable) contextReference).iterator();
			}
			else
			{
				throw new TemplateRuntimeException("Object is not of array type..."+key);
			}
		}
		
		return String.valueOf(contextReference);
	}
	
	
	@SuppressWarnings("rawtypes")
	private Object processRecursively(Object context, Object carriedValue, VariableNode node)
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
				int startIndex=node.getName().indexOf("[");
				
				index=Integer.parseInt(node.getName().substring(startIndex+1, node.getName().indexOf("]", startIndex+1)));
				
				tmpName=tmpName.substring(0, startIndex);
			}
			
			carriedValue=((Map)context).get(tmpName);

			carriedValue=processArray(node, carriedValue, index);
		}
		else if(node.getType()==VariableType.VARIABLE)
		{
			int index=-1;
			
			String tmpName=node.getName();
			
			if(node.getSubType()==VariableType.ARRAY)
			{
				int startIndex=node.getName().indexOf("[");
				
				index=Integer.parseInt(node.getName().substring(startIndex+1, node.getName().indexOf("]", startIndex+1)));
				
				tmpName=tmpName.substring(0, startIndex);
			}
			
			Class<?> clazz=carriedValue.getClass();

			if(Map.class.isAssignableFrom(clazz))
			{
				carriedValue=((Map)carriedValue).get(tmpName);
			}
			else
			{
				String methodName="get"+Character.toUpperCase(tmpName.charAt(0))+tmpName.substring(1);

				carriedValue=ReflectionUtils.invokeMethod(carriedValue, methodName, Arrays.asList(new Class<?>[]{}), Arrays.asList(new Object[]{}));
			}
			
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
				Object tmp=processRecursively(context, null, child);
				paramList.add(tmp);
				classList.add(tmp.getClass());
			}
			
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
				carriedValue=processRecursively(context, carriedValue, child);
			}
		}
		
		return carriedValue;
	}
	
	@SuppressWarnings("rawtypes")
	private Object processArray(VariableNode node, Object carriedValue, int index)
	{
		if(index==-1)
			return carriedValue;
		
		Class clazz=carriedValue.getClass();
		
		if(Object[].class.isAssignableFrom(clazz))
		{
			carriedValue=((Object[])carriedValue)[index];
		}
		else if(List.class.isAssignableFrom(clazz))
		{
			carriedValue=((List)carriedValue).get(index);
		}
		else if(Iterable.class.isAssignableFrom(clazz))
		{
			Iterator iterator=((Iterable)carriedValue).iterator();
			
			int count=0;
			
			while(iterator.hasNext())
			{
				Object tmp=iterator.next();
				
				if(count==index)
				{
					return tmp;
				}
				
				count++;
			}
		}

		return carriedValue;
	}
}
