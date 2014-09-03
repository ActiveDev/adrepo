package com.activedevsolutions.helpful.threading;

import static org.junit.Assert.*;

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
	public void testMismatchIdentifier() {
		try {
			new ADPermit(semaphore, "fakevalue");
			fail();
		}
		catch (IllegalArgumentException iae) {
			assertTrue(true);
		} // end try catch
	}

	@Test
	public void testIsValidPermitNoSemaphore() {
		try {
			new ADPermit(null, "fakevalue");
			fail();
		}
		catch (IllegalArgumentException iae) {
			assertTrue(true);
		} // end try catch
	}

	@Test
	public void testIsValidPermitNoIdentifier() {
		try {
			new ADPermit(semaphore, null);
			fail();
		}
		catch (IllegalArgumentException iae) {
			assertTrue(true);
		} // end try catch
	}

	@Test
	public void testGetIdentifier() {
		ADPermit permit = semaphore.acquireWithPermit();
		assertNotNull(permit.getGUUID());		
	}
	
	@Test
	public void testRelease() {
		ADPermit permit = semaphore.acquireWithPermit();
		assertEquals(semaphore.availablePermits(), 2);
		permit.release();
		assertEquals(semaphore.availablePermits(), 3);
		
		// Attempt an extra release
		permit.release();
		assertEquals(semaphore.availablePermits(), 3);
	}
}
