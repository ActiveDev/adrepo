package com.activedevsolutions.instrument;

import java.lang.instrument.Instrumentation;

/**
 * Entry point for the Java Agent.
 *
 */
public class InstrumentAgent {
	
	/**
	 * Private constructor so that the object cannot be created.
	 */
	private InstrumentAgent() {
		return;
	}
	
	/**
	 * Method executed for the java agent.
	 * 
	 * @param args
	 * @param instrumentation
	 */
	public static void premain(String args, Instrumentation instrumentation) {
		ClassLogger transformer = new ClassLogger();
		instrumentation.addTransformer(transformer);
	}
}
