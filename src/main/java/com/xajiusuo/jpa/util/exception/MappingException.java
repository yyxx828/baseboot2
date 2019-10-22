package com.xajiusuo.jpa.util.exception;

public class MappingException extends RuntimeException {
	private static final long serialVersionUID = -7013086716644015081L;

	public MappingException(Throwable e) {
		super(e);
	}

	public MappingException(String msg) {
		super(msg);
	}

	public MappingException() {
	}

	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

}
