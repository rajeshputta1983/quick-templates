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

import java.util.Arrays;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.utils.ReflectionUtils;

/*
* @author Rajesh Putta
*/
public class DefaultDataLookupHandler extends BaseDataLookupHandler {
	
	public DefaultDataLookupHandler() {
	}
	
	public Object lookupFromMap(Object context, String key, boolean getMethodCall)
	{
		if(getMethodCall)
		{
			String methodName="get"+Character.toUpperCase(key.charAt(0))+key.substring(1);

			return ReflectionUtils.invokeMethod(context, methodName, Arrays.asList(new Class<?>[]{}), Arrays.asList(new Object[]{}));
		}
		
		return context;
	}
	
	public Object lookupFromList(Object context, String key, int index)
	{
		return context;
	}
	
	public Object lookupAsPrimitive(Object context)
	{
		if(String.class.isAssignableFrom(context.getClass()))
		{
			return String.valueOf(context);
		}
		
		return context;
	}
	
	public Object returnFinalResult(Object context, boolean returnArrayType, String key)
	{
		if(returnArrayType)
		{
			throw new TemplateRuntimeException("Object is not of array type..."+key);
		}
		
		return context;
	}
}
