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

import org.w3c.dom.Node;

import com.codequicker.quick.templates.state.EngineContext;
import com.google.gson.JsonElement;

/*
* @author Rajesh Putta
*/
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
