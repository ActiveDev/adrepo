package com.activedevsolutions.democoin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Wrapper around BigDecimal to enforce scale and rounding
 * for the entire cryptocurrency. This could have been solved
 * multiple ways, but thought this would provide the most control.
 *
 */
public class CurrencyFormat {
	private BigDecimal value;

	/**
	 * Since BigDecimal is immutable, this constructor is required
	 * by internal methods.
	 * 
	 * @param value is the value to set
	 */
	private CurrencyFormat(BigDecimal value) {
		this.value = value;
	}
	
	/**
	 * Constructor that will set the value from a double.
	 * As well as enforcing the scale and rounding
	 * 
	 * @param initialValue is the value to set
	 */
	public CurrencyFormat(double initialValue) {
		value = BigDecimal.valueOf(initialValue);
		value.setScale(8, RoundingMode.UNNECESSARY);
	}
	
	// Getter
	public BigDecimal getValue() {
		return value;
	}
	
	public CurrencyFormat add(CurrencyFormat addValue) {
		BigDecimal newValue = value.add(addValue.getValue());
		return new CurrencyFormat(newValue);
	}

	public CurrencyFormat subtract(CurrencyFormat subtractValue) {
		BigDecimal newValue = value.subtract(subtractValue.getValue());
		return new CurrencyFormat(newValue);
	}

	public int compareTo(CurrencyFormat compareValue) {
		return value.compareTo(compareValue.getValue());
	}
	
	public String toPlainString() {
		return value.toPlainString();
	}
	
	@Override
	public String toString() {
		return value != null ? value.toString() : "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof CurrencyFormat)) return false;
		CurrencyFormat format = (CurrencyFormat) object;

		return value.equals(format.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
