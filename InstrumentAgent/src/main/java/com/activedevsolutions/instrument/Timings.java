package com.activedevsolutions.instrument;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Holds a stack of timing events.
 *
 */
public class Timings {
	// Stack
	private Deque<Timing> stack = new LinkedList<Timing>();
	
	// Keeps track of how many have popped off the stack
	private int popped;

	// Getters
	public Deque<Timing> getStack() {
		return stack;
	}
	public int getPopped() {
		return popped;
	}
	
	/**
	 * Pushes a timing event onto the stack.
	 * 
	 * @param timing is the timing event to put on the stack
	 */
	public void push(Timing timing) {
		stack.push(timing);
	}
	
	/**
	 * Pops a timing event off of the stack.
	 * 
	 * @return Timing object
	 */
	public Timing pop() {
		popped++;
		return stack.pop();
	}

}
