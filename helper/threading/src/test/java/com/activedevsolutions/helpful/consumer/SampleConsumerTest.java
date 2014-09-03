package com.activedevsolutions.helpful.consumer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.activedevsolutions.helpful.threading.ADPermit;
import com.activedevsolutions.helpful.threading.ADSemaphore;

public class SampleConsumerTest {

	@Test
	public void testAcquireWithPermit() {
		ADSemaphore semaphore = new ADSemaphore(3);
		ADPermit permit = semaphore.acquireWithPermit();
		assertEquals(semaphore.availablePermits(), 2);
		assertTrue(semaphore.isValidPermit(permit));
		permit.release();
	}

}
