package com.activedevsolutions.services.stock.model;

/**
 * Just a simple exception class to show how a REST controller can
 * react to custom exceptions.
 * 
 * @author activedev
 *
 */
public class ResourceNotFoundException extends Exception {
	private static final long serialVersionUID = -339224910184706580L;

	private static final String DEFAULT_MESSAGE = "Unable to find resource.";
	
	public ResourceNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
