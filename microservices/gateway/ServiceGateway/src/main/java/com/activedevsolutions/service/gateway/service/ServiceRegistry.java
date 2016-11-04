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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * This class now looks up the service in Eureka. This means that the MonitorSample
 * application be running as it has the Eureka server within it.
 * 
 * @author techguy
 *
 */
//TODO Look at Feign
//TODO Look at Ribbon
//TODO Investigate the best way to add meta data 
@Service
public class ServiceRegistry {	
	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
	
	@Autowired
	@Lazy
    private EurekaDiscoveryClient eurekaClient;

	/**
	 * Gets the service object from the registry based on the id.
	 * Right now it is doing a basic lookup from Eureka. Next step
	 * will be to play around with Feign and Ribbon.
	 * 
	 * @param serviceId is the identifier of the service to look up
	 * @return String this is temporary as it should return a Service object.
	 */
	public String getServicePath(String serviceId) {
		logger.info("Services: " + eurekaClient.getServices().toString());
		
		String result = null;
		
		//TODO For now, we will just use the first instance, but this is temporary until we get load balancing
		if (!eurekaClient.getInstances(serviceId).isEmpty()) {
			result = eurekaClient.getInstances(serviceId).get(0).getUri().toString() + "/" + serviceId + "/";
		} // end if
		
		return result;
	}
}
