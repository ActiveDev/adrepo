package com.activedevsolutions.instrument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton that contains the timing events for the
 * methods being executed.
 *
 */
public enum TimingStack {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(TimingStack.class);
		
	private Map<String, Timings> timingsCache = new ConcurrentHashMap<String, Timings>();

	/**
	 * Start of the timing event.
	 * 
	 * @param name is the name of the event
	 */
	public void push(String name) {
		// NOT THREAD-SAFE
		//TODO This needs to be moved off of this singleton
		String threadName = Thread.currentThread().getName();
		Timings timings = timingsCache.get(threadName);
		String prefix = "";
		
		if (timings == null) {
			timings = new Timings();
			timingsCache.put(threadName, timings);
			prefix = "[START]";
		} // end if
		
		Timing timing = new Timing(prefix + threadName, name);
		timing.start();
		timings.push(timing);
	}

	/**
	 * Ends the timing event.
	 */
	public void pop() {
		// NOT THREAD-SAFE
		//TODO This needs to be moved off of this singleton
		String threadName = Thread.currentThread().getName();
		Timings timings = timingsCache.get(threadName);

		if (timings != null) {
			Timing timing = timings.pop();
			timing.stop();
			
			String prefix = "";
			if (timings.getPopped() == 1) {
				prefix = "[END]";
			} // end if
			
			String indent = "";
			for (int i = 0; i < timings.getStack().size(); i++) {
				indent = indent + "    ";
			} // end for
			
			
			// It logs from the bottom up
			logger.info(indent + prefix + timing.toString());
		} // end if
	}
}
