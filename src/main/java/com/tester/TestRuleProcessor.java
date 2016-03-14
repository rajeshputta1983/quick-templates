package com.tester;

import com.codequicker.quick.templates.rules.core.IRuleProcessor;
import com.codequicker.quick.templates.state.EngineContext;

public class TestRuleProcessor implements IRuleProcessor {
	public void process(EngineContext context) {
		System.out.println("in test rule processor impl...");
	}
}
