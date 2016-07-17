package com.activedevsolutions.service.gateway.controller;

/**
 * Very simple bean to hold an error message. This is
 * used to make it easy to return json messages in the 
 * global exception handler in the controller.
 * 
 * @author techguy
 *
 */
public class ProxyError {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
