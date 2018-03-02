package com.activedevsolutions.democoin.exception;

public class DemoCoinException extends Exception {
	private static final long serialVersionUID = -5126422270432667665L;

	public DemoCoinException(String message) {
		super(message);
	}

	public DemoCoinException(Throwable cause) {
		super(cause);
	}

	public DemoCoinException(String message, Throwable cause) {
		super(message, cause);
	}
}
