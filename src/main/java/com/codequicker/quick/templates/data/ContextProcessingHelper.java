package com.codequicker.quick.templates.data;

import org.w3c.dom.Node;

import com.codequicker.quick.templates.state.EngineContext;
import com.google.gson.JsonElement;

public class ContextProcessingHelper {
	
	private ContextLookupFactory lookupFactory=ContextLookupFactory.getInstance();
	
	public Object lookup(String key, EngineContext context, boolean returnArrayType)
	{
		if(!key.startsWith("#"))
		{
			return key;
		}
		
		key=key.substring(1);

		int keyIndex=key.indexOf(".");
		
		String firstKey=null;
				
		if(keyIndex>-1)
		{
			firstKey=key.substring(0,keyIndex);
		}
		
		Object finalValue=null;
		
		if("json".equalsIgnoreCase(firstKey))
		{
			finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.JSON).lookup(key.substring(keyIndex+1), context, null, returnArrayType);
		}
		else if("xml".equalsIgnoreCase(firstKey))
		{
			finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.XML).lookup(key.substring(keyIndex+1), context, null, returnArrayType);
		}
		else 
		{
			Object value=context.getVariable(firstKey);
			
			if(value!=null)
			{
				if(value instanceof JsonElement)
				{
					finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.JSON).lookup(key.substring(keyIndex+1), context, value, returnArrayType);
				}
				else if(value instanceof Node)
				{
					finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.XML).lookup(key.substring(keyIndex+1), context, value, returnArrayType);
				}
				else
				{
					finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.MAP).lookup(key.substring(keyIndex+1), context, value, returnArrayType);
				}
			}
			else
			{
				if(key.endsWith("$index"))
				{
					return context.getVariable(key);
				}
				
				finalValue=lookupFactory.getLookupHandler(ContextLookupHandlerEnum.MAP).lookup(key, context, null, returnArrayType);
			}
		}
		
		return finalValue;
	}
}
