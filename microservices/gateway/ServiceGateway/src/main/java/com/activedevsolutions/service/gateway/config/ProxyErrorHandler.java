package com.activedevsolutions.service.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * Overrides the default response error handler so that the
 * proxy can return the microservice information without having
 * to catch exceptions.
 *  
 * @author techguy
 *
 */
public class ProxyErrorHandler extends DefaultResponseErrorHandler {
	/**
	 * {@inheritDoc}
	 * 
	 * Always returns false so that an exception is not thrown when
	 * a microservice returns a non 200 status.
	 */
	@Override
	protected boolean hasError(HttpStatus statusCode) {
		return false;
	}
}
