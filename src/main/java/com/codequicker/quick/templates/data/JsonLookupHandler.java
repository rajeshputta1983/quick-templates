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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/*
* @author Rajesh Putta
*/
public class JsonLookupHandler extends BaseDataLookupHandler {
	
	private int position=-1;
	
	public JsonLookupHandler(int position) {
		this.position=position;
	}
	
	public Object lookupFromMap(Object context, String key, boolean getMethodCall)
	{
		Class<?> clazz=context.getClass();

		if(JsonObject.class.isAssignableFrom(clazz))
		{
			return ((JsonObject)context).getAsJsonObject().get(key);
		}
		
		return getNextHandler(context, position).lookupFromMap(context, key, getMethodCall);
	}
	
	@SuppressWarnings("rawtypes")
	public Object lookupFromList(Object context, String key, int index)
	{
		Class clazz=context.getClass();
		
		if(JsonArray.class.isAssignableFrom(clazz))
		{
			JsonArray jsonArray=((JsonArray)context).getAsJsonArray();
			
			return jsonArray.get(index);
		}

		return getNextHandler(context, position).lookupFromList(context, key, index);
	}
	
	public Object lookupAsPrimitive(Object context)
	{
		if(JsonPrimitive.class.isAssignableFrom(context.getClass()))
		{
			return ((JsonPrimitive)context).getAsString();
		}
		
		return getNextHandler(context, position).lookupAsPrimitive(context);
	}
	
	public Object returnFinalResult(Object context, boolean returnArrayType, String key)
	{
		if(returnArrayType)
		{
			if(context instanceof JsonElement && ((JsonElement)context).isJsonArray())
			{
				return ((JsonElement)context).getAsJsonArray().iterator();
			}
		}
		
		if(context instanceof JsonElement && ((JsonElement)context).isJsonPrimitive())
			return ((JsonElement)context).getAsString();
		
		return getNextHandler(context, position).returnFinalResult(context, returnArrayType, key);
	}	
}
