package com.activedevsolutions.rules.engine;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the rule to execute. It acts as a wrapper around
 * the predicate.
 * 
 * @author techguy
 *
 * @param <T> is the business object type
 */
public class Rule<T> {
	private static final Logger logger = LoggerFactory.getLogger(Rule.class);
	
	private String name;
	private boolean enabled = true;
	
	// This could probably be an enum with a severity level as well
	private boolean stopOnFailure;
	
	private Predicate<T> predicate;

	/**
	 * Constructor to set the minimum fields.
	 * 
	 * @param name is the name of the rule
	 * @param predicate performs the actual test against the business object
	 */
	public Rule(String name, Predicate<T> predicate) {
		this.name = name;
		this.predicate = predicate;
				
		logger.info("Rule: [" + name + "] created with minimum.");
	}

	/**
	 * Constructor to set the all of the fields.
	 * 
	 * @param name is the name of the rule
	 * @param enabled is used to determine if the rule should be fired
	 * @param stopOnFauilure is used to determine if the rules should stop firing if
	 * 					a failure occurs for this particular rule
	 * @param predicate performs the actual test against the business object
	 */
	public Rule(String name, boolean enabled, boolean stopOnFailure, Predicate<T> predicate) {
		this(name, predicate);
		this.enabled = enabled;
		this.stopOnFailure = stopOnFailure;
		
		logger.info("Rule: [" + name + "] created.");
	}

	// Getter and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Predicate<T> getPredicate() {
		return predicate;
	}
	public void setPredicate(Predicate<T> predicate) {
		this.predicate = predicate;
	}
	public boolean isStopOnFailure() {
		return stopOnFailure;
	}
	public void setStopOnFailure(boolean stopOnFailure) {
		this.stopOnFailure = stopOnFailure;
	}
}
