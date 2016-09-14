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
