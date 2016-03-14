package com.codequicker.quick.templates.data;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.state.EngineContext;

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
