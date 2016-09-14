package com.activedevsolutions.rules.business;

/**
 * A sample order manager that will setup the rules for the
 * SampleOrder. This is just a setup. It would be just as
 * easy to implement the rules in SampleOrder.
 * 
 * One advantage to this is that it keeps your SampleOrder
 * as a pure VO and allows the rules to be in one spot which
 * seems cleaner.
 * 
 * @author techguy
 *
 */
public class SampleOrderManager extends AbstractValidatable<SampleOrder> {
	public static final String NOT_NULL = "notNull";
	public static final String ORDER_ID_NOT_NULL = "orderIdNotNull";
	public static final String AMOUNT_GREATER_THAN_ZERO = "amountGreaterThanZero";
	public static final String PRODUCT_ID_NOT_NULL = "productIdNotNull" ;

	/**
	 * Sets rules to validate Sample Orders.
	 */
	@Override
	protected void setupRules() {
		// It's passing in the predicate using lambda expressions
		// The p is the parameter representing the SampleOrder
		// The code after the -> is what should be executed to return a
		// true or false result as expected by the Predicate
		super.registerRules(NOT_NULL, p -> p != null);
		
		// Not checking if p != null on these for a reason. I want to show
		// you stacktraces when using lambda expressions. 
		// SPOILER ALERT: You won't like it.
		super.registerRules(AMOUNT_GREATER_THAN_ZERO, p -> p.getAmount() > 0);
		super.registerRules(ORDER_ID_NOT_NULL, p -> p.getOrderId() != null);
		super.registerRules(PRODUCT_ID_NOT_NULL, p -> p.getProductId() != null);
	}
}
