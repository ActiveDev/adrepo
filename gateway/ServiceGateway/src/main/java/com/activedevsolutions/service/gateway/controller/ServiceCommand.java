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
import java.util.Arrays;

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

/**
 * Provides functionality for making REST calls to external services.
 * In the very near future, this will be converted to a Hystrix command.
 * 
 * @author techguy
 *
 */
public class ServiceCommand {
	private static final Logger logger = LoggerFactory.getLogger(ServiceCommand.class);
		  
	/**
	 * Makes a rest call to a microservice and returns its response.
	 * 
	 * @param accepts contains the value for the http request accepts
	 * @param contentType contains the http request content type
	 * @param body contains the body of the request
	 * @param uri is the location of the service to run
	 * @param method is the http method to use
	 * @param formData is any form data coming from a PUT or PATCH
	 * 
	 * @return ResponseEntity<String> containing the response from the service
	 */
	public ResponseEntity<String> runService(String accepts, String contentType, 
			String body, URI uri, HttpMethod method, MultiValueMap<String, String> formData) {
		logger.info("[START] runService: " + uri.toString());
		
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.valueOf(accepts)));
		headers.setContentType(MediaType.valueOf(contentType));

		// Make the call to the microservice
		// Create the restClient that will call the microservice
		final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new ProxyErrorHandler());

		// Create entity with the body if it exists, otherwise use the form data
		HttpEntity<?> httpEntity;
		if (body != null) {
			httpEntity = new HttpEntity<>(body, headers);
		}
		else {
			httpEntity = new HttpEntity<>(formData, headers);
		} // end if
		
		final ResponseEntity<String> responseEntity = restTemplate.exchange(uri, method, httpEntity, String.class);
		
		logger.info("[END] runService: " + uri.toString());
		return responseEntity;
	}
	
	/**
	 * Creates a client http request factory for the rest template.
	 * 
	 * @return ClientHttpRequestFacotry with all the configuration setup
	 */
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		//TODO Externalize this
		int timeout = 5000;
		
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}
}
