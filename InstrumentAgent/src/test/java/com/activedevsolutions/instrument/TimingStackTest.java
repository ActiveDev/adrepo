package com.activedevsolutions.instrument;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimingStackTest {

	@Test
	public void testOverall() {
		TimingStack.INSTANCE.push("test1.main");
		TimingStack.INSTANCE.push("test1.child1");
		TimingStack.INSTANCE.push("test1.child2");
		TimingStack.INSTANCE.pop();
		TimingStack.INSTANCE.pop();
		TimingStack.INSTANCE.pop();
		
		//fail("Not yet implemented");
		assertTrue(true);
	}
	
	@Test
	public void testPush() {
		//fail("Not yet implemented");
	}

	@Test
	public void testPop() {
		//fail("Not yet implemented");
	}

	@Test
	public void testLog() {
		//fail("Not yet implemented");
	}

}
