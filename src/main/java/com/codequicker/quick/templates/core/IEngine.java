package com.codequicker.quick.templates.core;

import com.codequicker.quick.templates.state.EngineContext;

public interface IEngine {
	public void initialize(String configPath);
	public String execute(EngineContext context);
	public Object executeSingleEntity(String entity, EngineContext context);
}
