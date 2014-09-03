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

/**
 * ADPermit represents a concrete permit obtained by the
 * semaphore. This class is currently public which means
 * that someone could create a permit for a different semaphore.
 * 
 * To prevent this, the semaphore creates a GUUID which is passed
 * to the permit. Only if the values match, will the permit be considered
 * valid. Another option would be to change the access modifier to default
 * and then place the consumer of the permit in the same package.
 *  
 * @author techguy
 *
 */
public final class ADPermit {
	private transient ADSemaphore semaphore;
	private String guuid;
	
	/**
	 * Constructor to set the semaphore associated with this permit.
	 * 
	 * @param associatedSemaphore is the associated semaphore to the permit
	 * @param identifier of the associatedSemaphore to ensure it was the source
	 */
	public ADPermit(ADSemaphore associatedSemaphore, String identifier) {
		semaphore = associatedSemaphore;
		guuid = identifier;
		
		// Make sure the semaphore is valid and that the identifier matches up
		if (semaphore == null || !semaphore.authenticate(identifier)) {
			throw new IllegalArgumentException("Identifier does not match the associated semaphore for this permit.");
		} // end if
	}

	/**
	 * Scoped to the package so that only the ADSemaphore and ADPermit can call it.
	 * 
	 * @return String representing the identifier of the associated semaphore
	 */
	String getGUUID() {
		return guuid;
	}

	/**
	 * Releases the permit from the semaphore and performs a clean up
	 * that serves two purposes: 1) Marks the permit as invalid
	 * and 2) helps my paranoia with possible circular references.
	 */
	public void release() {
		if (semaphore != null) { 
			semaphore.release();
			semaphore = null;
			guuid = null;
		} // end if
	}
}
