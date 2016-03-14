package com.codequicker.quick.templates.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLookupHandler implements ILookupHandler {
	
	private Pattern arrayNodePattern=Pattern.compile(".+\\[(\\d+)\\]");
	
	public Class<?> getType() {
		return null;
	}
	
	public Object lookup(String key, EngineContext context, Object reference,
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
