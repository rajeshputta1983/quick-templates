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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
* @author Rajesh Putta
*/
public class MapLookupHandler extends BaseDataLookupHandler {
	
	private int position=-1;
	
	public MapLookupHandler(int position) {
		this.position=position;
	}
	
	@SuppressWarnings("rawtypes")
	public Object lookupFromMap(Object context, String key, boolean getMethodCall)
	{
		Class<?> clazz=context.getClass();

		if(Map.class.isAssignableFrom(clazz))
		{
			return ((Map)context).get(key);
		}
		
		return getNextHandler(context, position).lookupFromMap(context, key, getMethodCall);
	}
	
	@SuppressWarnings("rawtypes")
	public Object lookupFromList(Object context, String key, int index)
	{
		Class clazz=context.getClass();
		
		if(Object[].class.isAssignableFrom(clazz))
		{
			return ((Object[])context)[index];
		}
		else if(List.class.isAssignableFrom(clazz))
		{
			return ((List)context).get(index);
		}
		else if(Iterable.class.isAssignableFrom(clazz))
		{
			Iterator iterator=((Iterable)context).iterator();
			
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

		return getNextHandler(context, position).lookupFromList(context, key, index);
	}
	
	public Object lookupAsPrimitive(Object context)
	{
		return getNextHandler(context, position).lookupAsPrimitive(context);
	}
	
	@SuppressWarnings("rawtypes")
	public Object returnFinalResult(Object context, boolean returnArrayType, String key)
	{
		if(returnArrayType)
		{
			if(Iterable.class.isAssignableFrom(context.getClass()))
			{
				return ((Iterable) context).iterator();
			}
		}
		
		if(String.class.isAssignableFrom(context.getClass()))
		{
			return String.valueOf(context);
		}

		return getNextHandler(context, position).returnFinalResult(context, returnArrayType, key);
	}
}
