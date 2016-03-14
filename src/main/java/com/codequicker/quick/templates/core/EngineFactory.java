package com.codequicker.quick.templates.core;

public class EngineFactory {
	
	private static final EngineFactory factory=new EngineFactory();
	
	private EngineFactory(){
	}
	
	public static EngineFactory getInstance()
	{
		return factory;
	}
	
	public IEngine getEngine(EngineType type)
	{
		IEngine engine=null;
		
		switch(type)
		{
		case TEMPLATES: engine = new TemplateEngine();
						break;
		case RULES:	engine = new RuleEngine();
					break;
		}
		
		return engine;
	}
}
