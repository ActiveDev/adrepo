package com.activedevsolutions.service.gateway.service;

/**
 * This is just a stand in for a proper registry where
 * services would be registered with information such as:
 * serviceId
 * serviceName
 * serviceDescription
 * serviceUrl
 * etc ...
 * 
 * @author techguy
 *
 */
public final class ServiceRegistry {
	/**
	 * Private constructor to prevent object creation. 
	 */
	private ServiceRegistry() {
		return;
	}
	
	/**
	 * Gets the service object from the registry based on the id.
	 * Right now, this is just a stand in until a proper registry
	 * is implemented.
	 * 
	 * @param serviceId is the identifier of the service to look up
	 * @return String this is temporary as it should return a Service object.
	 */
	public static String getService(String serviceId) {
		// TODO Implement a service registry
		return "http://localhost:8080/stock/";
	}
}
