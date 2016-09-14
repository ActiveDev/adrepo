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
package com.activedevsolutions.rules.business;

import java.util.Map;

import com.activedevsolutions.rules.engine.TestResult;

/**
 * Interface to define an object as having the ability
 * to validate itself against a set of rules.
 * 
 * To be honest, I'm not sure if Validatable is even a word.
 * I also created this to show functional interfaces.
 * 
 * @author techguy
 *
 * @param <T> is the business object that is defined as validatable. 
 */
@FunctionalInterface
public interface IValidatable<T> {
	/**
	 * This method will fire the rules in the engine.
	 * 
	 * @param businessObject is the business object to validate
	 * @return Map containing the results of the validation with the rule
	 * 			name being the key.
	 */
	Map<String, TestResult> validate(T businessObject);
}
