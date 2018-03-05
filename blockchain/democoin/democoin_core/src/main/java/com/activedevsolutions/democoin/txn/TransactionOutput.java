package com.activedevsolutions.democoin.txn;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.CurrencyFormat;
import com.activedevsolutions.democoin.security.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Transaction outputs will show the final amount sent to each party from the transaction. 
 * These, when referenced as inputs in new transactions, act as proof that you have coins to send.
 *
 */
public class TransactionOutput {
	private static Logger logger = LoggerFactory.getLogger(TransactionOutput.class);
	
	private String id;
	
	// also known as the new owner of these coins.
	private final String recipient;
	
	// the amount of coins they own
	private final CurrencyFormat value;
	
	// the id of the transaction this output was created in
	private final String parentTransactionId; 

	/**
	 * Constructor.
	 * 
	 * @param reciepient is the public key of the recipient
	 * @param value is the value transferred
	 * @param parentTransactionId is the originator
	 */
	public TransactionOutput(String reciepient, CurrencyFormat value, String parentTransactionId) {
		this.recipient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = SecurityUtil.applySha256(reciepient + value.toPlainString() + parentTransactionId);
	}

	// Getters and Setters
	public String getId() {
		return id;
	}
	public String getRecipient() {
		return recipient;
	}
	public CurrencyFormat getValue() {
		return value;
	}
	public String getParentTransactionId() {
		return parentTransactionId;
	}

	/**
	 * Determine if the coin belongs to the public key.
	 * 
	 * @param publicKey is the public key to look for
	 * @return boolean indicating if the this is owned by the public key
	 */
	public boolean isMine(String publicKey) {
		return (publicKey.equals(recipient));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof TransactionOutput)) return false;
		TransactionOutput output = (TransactionOutput) object;

		return id.equals(output.getId()) && recipient.equals(output.getRecipient()) && 
				value.equals(output.getValue()) && parentTransactionId.equals(output.getParentTransactionId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, recipient, value, parentTransactionId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			result = objectMapper.writeValueAsString(this);
		} 
		catch (JsonProcessingException e) {
			logger.error("Unable to generate json.", e);
		} // end try catch
		
		return result;
	}
}
