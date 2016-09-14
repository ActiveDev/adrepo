package com.activedevsolutions.rules.engine;

/**
 * Represents the result of a test.
 * 
 * @author techguy
 *
 */
public enum ResultEnum {
	/**
	 * Indicates the test did not run.
	 */
	NOT_RUN,
	/**
	 * Indicates the test passed.
	 */
	PASS, 
	/**
	 * Indicates the test failed.
	 */
	FAIL
}
