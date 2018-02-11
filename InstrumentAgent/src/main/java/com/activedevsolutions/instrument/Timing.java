package com.activedevsolutions.instrument;

/**
 * Represents the timing of one event.
 *
 */
public class Timing {
	private static final String DELIMITER = ",";
	
	private final String threadName;
	private final String name;
	private long startTime;
	private long endTime;
	private long elapsedTime;

	/**
	 * Constructor.
	 * 
	 * @param name is the name assigned to this timing
	 */
	public Timing(String threadName, String name) {
		this.threadName = threadName;
		this.name = name;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Start timing.
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Stop timing.
	 */
	public void stop() {
		this.endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
	}

	/**
	 * Provides a string representation of the timing event.
	 * 
	 * @return String holding the object contents
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(threadName);
		sb.append(DELIMITER);
		sb.append(name);
		sb.append(DELIMITER);
		sb.append(startTime);
		sb.append(DELIMITER);
		sb.append(endTime);
		sb.append(DELIMITER);
		sb.append(elapsedTime);

		return sb.toString();
	}
}
