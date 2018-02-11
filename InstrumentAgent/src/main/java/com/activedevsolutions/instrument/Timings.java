package com.activedevsolutions.instrument;

import java.util.Deque;
import java.util.LinkedList;

public class Timings {
	private Deque<Timing> stack = new LinkedList<Timing>();
	private int popped;

	public Deque<Timing> getStack() {
		return stack;
	}
	public int getPopped() {
		return popped;
	}
	
	public void push(Timing timing) {
		stack.push(timing);
	}
	
	public Timing pop() {
		popped++;
		return stack.pop();
	}

}
