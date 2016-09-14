package com.activedevsolutions.rules.business;

import java.time.LocalDateTime;

/**
 * Sample value object that represents some kind of
 * horrible order that apparently only accepts amounts
 * as whole numbers.
 * 
 * On the bright side, we get to show case the new LocalDateTime object.
 * 
 * @author techguy
 *
 */
public class SampleOrder {
	private String orderId;
	private String productId;
	private int amount;
	private LocalDateTime orderDate;
	
	// Getters and Setters
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

}
