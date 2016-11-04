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
package com.activedevsolutions.service.gateway.service;

import java.net.URI;
import java.util.Arrays;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.activedevsolutions.service.gateway.config.ProxyErrorHandler;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * Provides functionality for making REST calls to external services.
 * In the very near future, this will be converted to a Hystrix command.
 * 
 * NOTE: I could have used the @HystrixCommand annotation but I wanted more
 * control over the creation of the Group and Command keys. There is probably
 * a way to do it with the annotations that I will need to investigate.
 * 
 * @author techguy
 *
 */
public class ServiceCommand extends HystrixCommand<ResponseEntity<String>> {
	private static final Logger logger = LoggerFactory.getLogger(ServiceCommand.class);
	
	private String commandId;
	
	// Static objects
	private HttpHeaders headers;
	private RestTemplate restTemplate;
	
	// Dynamic objects
	private URI uri;
	private HttpMethod method;
	private HttpEntity<?> httpEntity;
	
	/**
	 * Constructor to help setup the object before running the Hystrix command.
	 * It also sets up the Hystrix group and command based on the serviceId
	 * 
	 * @param serviceId is the service that will be executed
	 */
	public ServiceCommand(String serviceId, String commandId) {
		// Create group key and command key for the hystrix command
		this(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(serviceId)).
				andCommandKey(HystrixCommandKey.Factory.asKey(commandId)));
		
		// The command id is used to determine what will be uniquely identified as a component in the
		// hystrix dashboard, i.e. Stock - GET
		this.commandId = commandId;
	}
		
	/**
	 * Constructor for creating the Hystrix Command.
	 * 
	 * @param setter holds the configuration for this HystrixCommand
	 */
	protected ServiceCommand(com.netflix.hystrix.HystrixCommand.Setter setter) {
		super(setter);
	}
	
	// Getter
	public String getCommandId() {
		return commandId;
	}
	
	/**
	 * This sets up the dynamic data that changes with each call to the service.
	 * 
	 * @param accepts contains the value for the http request accepts
	 * @param contentType contains the http request content type
	 * @param uri is the location of the service to run
	 * @param method is the http method to use
	 * @param body contains the body of the request
	 * @param formData is any form data coming from a PUT or PATCH
	 */
	public void setup(String accepts, String contentType, URI uri, HttpMethod method, 
			String body, MultiValueMap<String, String> formData) {
		// Separating static from dynamic to cache later on
		init(accepts, contentType);
		
		this.uri = uri;
		this.method = method;
		
		// Create entity with the body if it exists, otherwise use the form data
		if (body != null) {
			httpEntity = new HttpEntity<>(body, headers);
		}
		else {
			httpEntity = new HttpEntity<>(formData, headers);
		} // end if		
	}

	/**
	 * Initializes the static objects that only need to be created once
	 * for a particular service.
	 * 
	 * @param accepts contains the value for the http request accepts
	 * @param contentType contains the http request content type
	 */
	private void init(String accepts, String contentType) {
		headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.valueOf(accepts)));
		headers.setContentType(MediaType.valueOf(contentType));

		// Create the restClient that will call the microservice
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new ProxyErrorHandler());
	}
	
	/**
	 * Makes a rest call to a microservice and returns its response.
	 *  
	 * @return ResponseEntity<String> containing the response from the service
	 * @throws Exception when any exception occurs during the run
	 */
	@Override
	protected ResponseEntity<String> run() throws Exception {
		logger.info("[START] runService: " + uri.toString());
						
		final ResponseEntity<String> responseEntity = restTemplate.exchange(uri, method, httpEntity, String.class);
		
		logger.info("[END] runService: " + uri.toString());
		return responseEntity;
	}

	/**
	 * This method gets called when the run encounters an exception.
	 * 
	 * @return ResponseEntity<String> containing a response to the error
	 */
    @Override
    protected ResponseEntity<String> getFallback() {
        logger.info("Events (so far) in Fallback: " + getExecutionEvents());
        logger.error("Error running service: " + commandId, getFailedExecutionException());
        
	    return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE)
	    		             .body("Unable to execute service: " + commandId);
    }
	
	/**
	 * Creates a client http request factory for the rest template. This
	 * is required for running PATCH and DELETE.
	 * 
	 * @return ClientHttpRequestFacotry with all the configuration setup
	 */
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		//TODO Now with Hystrix, I don't believe this is necessary. Leaving it here
		// until I get around to looking at all of the hystrix properties.
		int timeout = 5000;
		
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}
}
