package com.codequicker.quick.templates.data;

import com.codequicker.quick.templates.state.EngineContext;

public interface ILookupHandler {
	public Class<?> getType();
	public Object lookup(String key, EngineContext context, Object reference, boolean returnArrayType);	
}
