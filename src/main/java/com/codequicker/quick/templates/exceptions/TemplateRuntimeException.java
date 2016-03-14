package com.codequicker.quick.templates.exceptions;

public class TemplateRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8952086989733763984L;

	public TemplateRuntimeException() {
		super();
	}
	
	public TemplateRuntimeException(String msg) {
		super(msg);
	}
	
	public TemplateRuntimeException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public TemplateRuntimeException(Throwable t) {
		super(t);
	}
}
