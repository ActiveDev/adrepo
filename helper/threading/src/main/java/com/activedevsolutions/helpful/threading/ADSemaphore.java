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
package com.activedevsolutions.helpful.threading;

import java.util.UUID;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

/**
 * Provides all of the same functionality as the Semaphore class with the
 * added ability to provide a concrete Permit that can be handed out.
 * 
 * @author techguy
 *
 */
public class ADSemaphore extends Semaphore {
	private static final long serialVersionUID = 2918908916899792714L;
	private static final transient Logger logger = Logger.getLogger(ADSemaphore.class);
	
	// Used to help identify this particular instance
	private final String guuid = UUID.randomUUID().toString();

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ADSemaphore(int permits) {
		super(permits);
	}

	/**
	 * {@inheritDoc}
	 */
	public ADSemaphore(int permits, boolean fair) {
		super(permits, fair);
	}

	/**
	 * acquireWithPermit acts just like the acquire method only
	 * it returns a concrete representation of the permit acquired.
	 * This allows the ability put a hold on an available permit
	 * within a semaphore.
	 * 
	 * This can be useful when the producer must do initializing, but only
	 * when it's sure that it can submit the work. For instance, if a producer
	 * is polling files and wants to pick one up, it only wants to lock it if
	 * it knows it will be able to work on it immediately, otherwise it wants to
	 * give another process the ability to grab it.
	 * 
	 * @return ADPermit representing the permit acquired or null if unsuccessful
	 */
	public ADPermit acquireWithPermit() {
		ADPermit permit = null;
		
		try {
			acquire();
			permit = new ADPermit(this, guuid);
		}
		catch (InterruptedException ie) {
			logger.error("Unable to acquire permit due to interruption.", ie);
		} // end try catch
		
		return permit;
	}
	
	/**
	 * Determines if the value passed in matches with the guuid
	 * associated with this semaphore.
	 * 
	 * @param value is the string to match up to
	 * 
	 * @return boolean indicating if there was a match
	 */
	public boolean authenticate(String value) {
		return (guuid.equals(value));
	}
}
