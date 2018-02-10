package com.activedevsolutions.instrument;

import java.lang.instrument.Instrumentation;

public class InstrumentAgent {
	public static void premain(String args, Instrumentation instrumentation) {
		ClassLogger transformer = new ClassLogger();
		instrumentation.addTransformer(transformer);
	}
}
