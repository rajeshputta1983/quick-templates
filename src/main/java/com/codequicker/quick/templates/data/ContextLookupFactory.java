package com.codequicker.quick.templates.data;

import java.util.HashMap;
import java.util.Map;

public class ContextLookupFactory {
	
	private static final ContextLookupFactory factory=new ContextLookupFactory();
	
	private Map<ContextLookupHandlerEnum, ILookupHandler> cache=new HashMap<ContextLookupHandlerEnum, ILookupHandler>();
	
	private ContextLookupFactory(){
		this.cache.put(ContextLookupHandlerEnum.MAP, new MapLookupHandler());
		this.cache.put(ContextLookupHandlerEnum.JSON, new JsonLookupHandler());
		this.cache.put(ContextLookupHandlerEnum.XML, new XmlLookupHandler());
	}
	
	public static ContextLookupFactory getInstance()
	{
		return factory;
	}
	
	public ILookupHandler getLookupHandler(ContextLookupHandlerEnum lookupHandlerEnum)
	{
		return this.cache.get(lookupHandlerEnum);
	}
	
}
