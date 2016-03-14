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

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class MapLookupHandler implements ILookupHandler {
	
	private Pattern arrayNodePattern=Pattern.compile(".+\\[(\\d+)\\]");
	
	public Class<?> getType() {
		return null;
	}
	
	@SuppressWarnings({"rawtypes" })
	public Object lookup(String key, EngineContext context, Object reference,
			boolean returnArrayType) {

		Object contextReference=(reference!=null)?reference:context.getContext();
		
		String[] keys=key.split("\\.");
		
		boolean isArrayType=false;
		
		for(String mapKey: keys)
		{
			Matcher matcher=arrayNodePattern.matcher(mapKey);
			
			if(matcher.matches())
				isArrayType=true;
			else
				isArrayType=false;

			if(isArrayType)
			{
				int startIndex=mapKey.indexOf("[");
				
				int index=Integer.parseInt(mapKey.substring(startIndex+1, mapKey.indexOf("]", startIndex+1)));
				
				mapKey=mapKey.substring(0, startIndex);
				
				if(contextReference instanceof Map)
				{
					contextReference=((List)((Map)contextReference).get(mapKey)).get(index);
				}
				else
				{
					throw new TemplateRuntimeException("invalid key reference used in template..."+key);
				}
			}
			else if(contextReference instanceof Map)
			{
				contextReference =((Map)contextReference).get(mapKey);
			}
		}
		
		if(returnArrayType)
		{
			if(contextReference instanceof List)
			{
				return ((List) contextReference).iterator();
			}
			else
			{
				throw new TemplateRuntimeException("Map object is not of array type..."+key);
			}
		}
		
		return String.valueOf(contextReference);
	}
}
