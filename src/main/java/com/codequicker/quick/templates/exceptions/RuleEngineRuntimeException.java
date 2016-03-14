package com.codequicker.quick.templates.exceptions;

public class RuleEngineRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8952086989733763984L;

	public RuleEngineRuntimeException() {
		super();
	}
	
	public RuleEngineRuntimeException(String msg) {
		super(msg);
	}
	
	public RuleEngineRuntimeException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public RuleEngineRuntimeException(Throwable t) {
		super(t);
	}
}
