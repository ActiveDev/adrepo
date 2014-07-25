package com.activedevsolutions.helpful.threading;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class ADPermitTest {
	private ADSemaphore semaphore;

	@Before
	public void setUp() throws Exception {
		// Lets create a semaphore with a bound of 3
		semaphore = new ADSemaphore(3);
	}

	@Test
	public void testIsValidPermit() {
		ADPermit permit = semaphore.acquireWithPermit();
		assertTrue(permit.isValidPermit());		
	}

	@Test
	public void testIsValidPermitNegative() {		
		ADPermit permit = new ADPermit(null, null);
		assertFalse(permit.isValidPermit());
		
		permit = new ADPermit(semaphore, null);
		assertFalse(permit.isValidPermit());
	}

	@Test
	public void testBypassSemaphoreAttempt() {
		String guuid = UUID.randomUUID().toString();
		
		ADPermit permit = new ADPermit(semaphore, guuid);
		assertFalse(permit.isValidPermit());
		
		permit = new ADPermit(null, guuid);
		assertFalse(permit.isValidPermit());
	}

	@Test
	public void testRelease() {
		ADPermit permit = semaphore.acquireWithPermit();
		assertEquals(semaphore.availablePermits(), 2);
		assertTrue(permit.isValidPermit());
		permit.release();
		assertEquals(semaphore.availablePermits(), 3);
		assertFalse(permit.isValidPermit());
	}
}
