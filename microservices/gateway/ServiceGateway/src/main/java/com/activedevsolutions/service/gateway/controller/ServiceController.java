/***
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
* THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.activedevsolutions.service.gateway.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.activedevsolutions.service.gateway.service.ServiceCommand;
import com.activedevsolutions.service.gateway.service.ServiceRegistry;

/**
 * REST Controller that provides the ability to act as a REST gateway
 * between calling applications and microservices.
 * 
 * @author techguy
 *
 */
@RestController
@ControllerAdvice
public class ServiceController {
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
		
	@Autowired
	private ServiceRegistry registry;
		
	/**
	 * Makes a call to a microservice by looking up the service id being passed. It then
	 * uses the rest of the url to pass along to the microservice. It also passes essential
	 * headers, the body, and any query strings.
	 * 
	 * @param method is the http method that the calling application used
	 * @param request is the servlet request
	 * @param response is the servlet response
	 * @param id is the path variable that was passed in by the url
	 * @param body is the body passed in by the calling application. This is optional.
	 * @param formData is any form data passed in by the calling application. This is optional.
	 * @param accepts is the format to accept the response in.
	 * @param contentType is the format that the request is in.
	 * 
	 * @throws URIException is thrown when a service has an invalid uri
	 * @return ResponseEntity<String> holding the response received from the microservice
	 * @throws NoSuchMethodException occurs when a service cannot be found in the registry (usually
	 * 					due to a bad {id} being passed in by the url
	 */
	@RequestMapping(value = "/proxy/{id}/**", 
			method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
	@ResponseBody
	public ResponseEntity<String> proxyCall(final HttpMethod method, 
			final HttpServletRequest request, 
			final HttpServletResponse response, 
			@PathVariable final String id, 
			@RequestBody(required = false) String body,
			@RequestParam(required = false) MultiValueMap<String, String> formData,
			@RequestHeader(value = "Accepts", defaultValue = MediaType.APPLICATION_JSON_VALUE) String accepts,
			@RequestHeader(value = "Content-Type", defaultValue = MediaType.APPLICATION_FORM_URLENCODED_VALUE) String contentType) 
			throws URISyntaxException, NoSuchMethodException {
		logger.info("[START] Proxy call for " + id);
		
		// Get the query string and prefix with ? if a query string exists
		final String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
		
		// Get the part of the path marked as the wildcard
		final String restOfPath = getWildcardPath(request);
		
		// Use the identifier to look up the service
		final String servicePath = registry.getServicePath(id);
		if (servicePath == null) {
		    throw new NoSuchMethodException("The service requested cannot be found in the registry.");
		}
		
		// Create the URI based on the microservice's path, the rest of the path passed
		// into the proxy and any query strings.
		URI uri = new URI(servicePath + restOfPath + queryString);
		
		// TODO Add the ability to apply request filters 
		
		// TODO Look at caching these objects
		String commandId = id + "-" + method.toString();
		ServiceCommand command = new ServiceCommand(id, commandId);
		command.setup(accepts, contentType, uri, method, body, formData);
		ResponseEntity<String> responseEntity = command.execute();
		
		// Merge headers from response of microservice with those of the proxy
		// TODO this should really be one of those response filters
		HttpHeaders microHeaders = responseEntity.getHeaders();
		for (String key : microHeaders.keySet()) {
			String value = microHeaders.get(key).toString();
			response.addHeader(key, value); 
		}

		// TODO Add the ability to apply response filters
		
		// Return the response from the microservice
		return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
	}
	
	/**
	 * Extracts the part of the url that was marked as a wildcard so that
	 * it can be appended to the call for the microservice.
	 * 
	 * @param request is the http request
	 * @return String representing the wildcard portion of the url
	 */
	private String getWildcardPath(final HttpServletRequest request) {
		final String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		final String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		final AntPathMatcher apm = new AntPathMatcher();
		
		return apm.extractPathWithinPattern(bestMatchPattern, path);
	}
	
	/**
	 * Exception handler that will handle anything that derives from the
	 * NoSuchMethodException class.
	 * 
	 * @param e is the NoSuchMethodException object containing the exception thrown
	 * @return ProxyError containing the exception message
	 */
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(value = NoSuchMethodException.class)
	public ProxyError handleBadServiceId(NoSuchMethodException e) {
		logger.error("An error occured trying to find the service in the registry.", e);
		
		ProxyError error = new ProxyError();
		error.setMessage(e.getMessage());

		return error;
	}
	
	/**
	 * Exception handler that will handle anything that derives from the
	 * Exception class.
	 * 
	 * @param e is the Exception object containing the exception thrown
	 * @return ProxyError containing the exception message
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(value = Exception.class)
	public ProxyError handleBaseException(Exception e) {
		logger.error("An error occured during the proxy call.", e);
		
		ProxyError error = new ProxyError();
		error.setMessage(e.getMessage());

		return error;
	}
}
