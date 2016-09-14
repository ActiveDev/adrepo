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
package com.activedevsolutions.rules.engine;

import java.time.LocalDateTime;

/**
 * Used to store the results of a Rule test.
 * 
 * @author techguy
 *
 */
public final class TestResult {
	private final String ruleName;
	private final ResultEnum status;
	private final LocalDateTime dateExecuted;

	/**
	 * Constructor to set the mandatory fields.
	 * 
	 * @param ruleName is the name of the rule that was executed
	 * @param status is the result of the test
	 */
	public TestResult(String ruleName, ResultEnum status) {
		this.ruleName = ruleName;
		this.status = status;
		dateExecuted = LocalDateTime.now();
	}
	
	public String getRuleName() {
		return ruleName;
	}
	public ResultEnum getStatus() {
		return status;
	}
	public LocalDateTime getDateExecuted() {
		return dateExecuted;
	}
	
	/**
	 * Override toString to show a more friendly message.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(ruleName);
		builder.append(": ");
		builder.append(status);
		builder.append(" On ");
		builder.append(dateExecuted);
		
		return builder.toString();
	}
}
