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
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.rules.engine.Rule;
import com.activedevsolutions.rules.engine.RuleEngine;
import com.activedevsolutions.rules.engine.TestResult;

/**
 * This class provides functionality for any business object that
 * wants to be able to validate a set of rules against itself. All
 * it has to do is implement the setupRules method in order to register
 * the rules it wants to fire.
 * 
 * To be honest, it's really not all that necessary as the business 
 * object can call the engine itself. It's just here as a helper.
 * 
 * @author techguy
 *
 * @param <T> is the business object type
 */
public abstract class AbstractValidatable<T> implements IValidatable<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractValidatable.class);
	
	private final RuleEngine<T> engine;
	
	/**
	 * Default Constructor.
	 */
	public AbstractValidatable() {
		engine = new RuleEngine<>();
		setupRules();
	}
	
	/**
	 * This method should be implemented to register one or more rules
	 * that will validate the business object.
	 */
	protected abstract void setupRules();
	
	/**
	 * This method will add rules to the engine so that it can
	 * fire them off during the validate.
	 * 
	 * @param ruleName is the name of the rule
	 * @param predicate is the predicate to fire
	 */
	protected void registerRules(String ruleName, Predicate<T> predicate) {
		Rule<T> rule = new Rule<>(ruleName, predicate);
		engine.registerRule(rule);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, TestResult> validate(T businessObject) {
		logger.info("Validating business object...");
		return engine.fireAllRules(businessObject);
	}
}
