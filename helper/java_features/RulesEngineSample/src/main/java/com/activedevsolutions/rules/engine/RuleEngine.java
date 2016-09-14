package com.activedevsolutions.rules.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will hold all the rules to execute and will run them against
 * a business object.
 * 
 * @author techguy
 *
 * @param <T> is the business object type
 */
public class RuleEngine<T> {
	private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
	
	// Holds the rules that will be fired off
	private List<Rule<T>> rules = new ArrayList<>();
	
	/**
	 * Registers a rule with the engine. This is sample code
	 * so this method is not thread safe.
	 * 
	 * @param rule is the rule to add to the collection.
	 */
	public void registerRule(Rule<T> rule) {
		rules.add(rule);
	}
	
	/**
	 * Will execute all of the rules that have been registered.
	 * 
	 * @param businessObject contains the values that the rules will validate against.
	 * @return Map<String, TestResult> containing the results of each rule test. The key
	 * 						is the name of the rule.
	 */
	public Map<String, TestResult> fireAllRules(T businessObject) {
		// This is just a demo, but there is lots of functionality that
		// could be added here including a flag to indicate if it should stop
		// fire rules at the first failure or maybe set a threshold of failure.
		Map<String, TestResult> results = new HashMap<>();
		TestResult testResult;
		
		// Iterate through each rule
		for (Rule<T> rule : rules) {
			// Do not fire a rule if it is marked as disabled
			if (!rule.isEnabled()) {
				logger.info("Rule [" + rule.getName() + "] is disabled.");
				continue;
			} // end if
			
			// Fire the rule and show the result
			logger.info("Rule [" + rule.getName() + "] executing...");
			ResultEnum status = rule.getPredicate().test(businessObject) ? ResultEnum.PASS : ResultEnum.FAIL;
			
			//TODO Implement stopOnFailure check and throw an exception
			
			testResult = new TestResult(rule.getName(), status);
			results.put(rule.getName(), testResult);
		} // end for
		
		return results;
	}
}
