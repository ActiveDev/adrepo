package com.activedevsolutions.rules.business;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.Test;

import com.activedevsolutions.rules.engine.ResultEnum;
import com.activedevsolutions.rules.engine.TestResult;

public class ValidatableTest {	
	@Test
	public void testValidate_AllPass() {
		Map<String, TestResult> results = runSample("100001", "WIDGET-1", 1250, LocalDateTime.now());
		
		// Another "new" feature. Print out the TestResult objects
		// This is done by iterating through the TestResult objects
		// and passing them the println method.
		results.values().forEach(System.out::println);
		
		assertTrue(results.get(SampleOrderManager.NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.ORDER_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.AMOUNT_GREATER_THAN_ZERO).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.PRODUCT_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
	}

	@Test
	public void testValidate_NullOrderObject() {
		IValidatable<SampleOrder> orderManager = new SampleOrderManager();
		
		try {
			orderManager.validate(null);
			fail();
		}
		catch (NullPointerException npe) {
			// Have a look at this stack trace. While lambda expressions
			// are nice to use, they do pose certain problems with readability
			// and maintainability.
			npe.printStackTrace();
		}
	}
	
	@Test
	public void testValidate_NullOrderId() {
		Map<String, TestResult> results = runSample(null, "WIDGET-1", 1250, LocalDateTime.now());		
		assertTrue(results.get(SampleOrderManager.NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.ORDER_ID_NOT_NULL).getStatus().equals(ResultEnum.FAIL));
		assertTrue(results.get(SampleOrderManager.AMOUNT_GREATER_THAN_ZERO).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.PRODUCT_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
	}

	@Test
	public void testValidate_AmountZero() {
		Map<String, TestResult> results = runSample("100001", "WIDGET-1", 0, LocalDateTime.now());
		assertTrue(results.get(SampleOrderManager.NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.ORDER_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.AMOUNT_GREATER_THAN_ZERO).getStatus().equals(ResultEnum.FAIL));
		assertTrue(results.get(SampleOrderManager.PRODUCT_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
	}
	
	@Test
	public void testValidate_NullProductId() {
		Map<String, TestResult> results = runSample("100001", null, 1250, LocalDateTime.now());		
		assertTrue(results.get(SampleOrderManager.NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.ORDER_ID_NOT_NULL).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.AMOUNT_GREATER_THAN_ZERO).getStatus().equals(ResultEnum.PASS));
		assertTrue(results.get(SampleOrderManager.PRODUCT_ID_NOT_NULL).getStatus().equals(ResultEnum.FAIL));
	}

	/**
	 * Helper function so that we can create test cases quickly.
	 * 
	 * @param orderId
	 * @param productId
	 * @param amount
	 * @param orderDate
	 * @return
	 */
	private Map<String, TestResult> runSample(String orderId, String productId, int amount, LocalDateTime orderDate) {
		SampleOrder order1 = new SampleOrder();
		order1.setOrderId(orderId);
		order1.setProductId(productId);
		order1.setAmount(amount);
		order1.setOrderDate(LocalDateTime.now());
		
		IValidatable<SampleOrder> orderManager = new SampleOrderManager();
		return orderManager.validate(order1);
	}
}
