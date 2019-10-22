package com.xajiusuo.jpa.util.exception;

public class MappException extends RuntimeException {
	private static final long serialVersionUID = -7013086716644015081L;

	public MappException(Throwable e) {
		super(e);
	}

	public MappException(String msg) {
		super(msg);
	}

	public MappException() {
	}

	public MappException(String message, Throwable cause) {
		super(message, cause);
	}

}
