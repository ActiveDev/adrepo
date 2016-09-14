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
