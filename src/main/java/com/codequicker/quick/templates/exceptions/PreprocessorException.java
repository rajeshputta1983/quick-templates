package com.codequicker.quick.templates.exceptions;

public class PreprocessorException extends RuntimeException {

	private static final long serialVersionUID = -8952086989733763984L;

	public PreprocessorException() {
		super();
	}
	
	public PreprocessorException(String msg) {
		super(msg);
	}
	
	public PreprocessorException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public PreprocessorException(Throwable t) {
		super(t);
	}
}
