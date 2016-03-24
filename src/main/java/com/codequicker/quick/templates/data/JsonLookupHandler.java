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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;
import com.codequicker.quick.templates.state.VariableNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/*
* @author Rajesh Putta
*/
public class JsonLookupHandler implements ILookupHandler {
	
	private Pattern arrayNodePattern=Pattern.compile(".+\\[(\\d+)\\]");
	
	public Class<?> getType() {
		return null;
	}
	
	public Object lookup(String key, VariableNode exprNode, EngineContext context, Object reference,
			boolean returnArrayType) {
		
		JsonElement jsonObject=(reference!=null)?(JsonElement)reference:context.getJsonObject();
		
		if(jsonObject==null)
			throw new TemplateRuntimeException("No Json Payload is set in the EngineContext instance...");

		String[] keys=key.split("\\.");
		
		boolean isArrayType=false;
		
		for(String jsonKey: keys)
		{
			Matcher matcher=arrayNodePattern.matcher(jsonKey);
			
			if(matcher.matches())
				isArrayType=true;
			else
				isArrayType=false;

			if(isArrayType)
			{
				int startIndex=jsonKey.indexOf("[");
				
				int index=Integer.parseInt(jsonKey.substring(startIndex+1, jsonKey.indexOf("]", startIndex+1)));
				
				jsonKey=jsonKey.substring(0, startIndex);
				
				jsonObject=jsonObject.getAsJsonObject().get(jsonKey);
				
				JsonArray jsonArray=jsonObject.getAsJsonArray();
				
				jsonObject=jsonArray.get(index);
			}
			else if(jsonObject.isJsonObject())
			{
				JsonObject obj=jsonObject.getAsJsonObject();
				
				jsonObject=obj.get(jsonKey);
			}
		}
		
		if(returnArrayType)
		{
			if(jsonObject.isJsonArray())
			{
				return jsonObject.getAsJsonArray().iterator();
			}	
			else
			{
				throw new TemplateRuntimeException("json object is not of array type..."+key);
			}
		}
		
		if(jsonObject.isJsonPrimitive())
			return jsonObject.getAsString();
		
		return null;
	}
}
