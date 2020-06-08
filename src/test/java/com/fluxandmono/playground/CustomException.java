package com.fluxandmono.playground;

public class CustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public CustomException(Throwable e) {
		super(e);
		this.message = e.getMessage();
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
