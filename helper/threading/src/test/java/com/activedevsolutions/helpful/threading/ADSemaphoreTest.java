package com.activedevsolutions.helpful.threading;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ADSemaphoreTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAcquireWithPermit() {
		ADSemaphore semaphore = new ADSemaphore(3);
		ADPermit permit = semaphore.acquireWithPermit();
		assertEquals(semaphore.availablePermits(), 2);
		assertTrue(permit.isValidPermit());
		permit.release();
	}
	
	@Test
	public void testAuthenticateNegative() {
		ADSemaphore semaphore = new ADSemaphore(3);
		assertFalse(semaphore.authenticate("fakevalue"));	
	}
}
