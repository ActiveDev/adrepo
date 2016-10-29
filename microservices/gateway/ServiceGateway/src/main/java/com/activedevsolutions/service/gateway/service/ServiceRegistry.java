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
		// TODO Implement a service registry. This will soon be using the Eureka server.
		return "http://localhost:9000/" + serviceId + "/";
	}
}
