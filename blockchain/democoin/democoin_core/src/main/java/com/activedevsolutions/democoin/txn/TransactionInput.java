package com.activedevsolutions.democoin.txn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class will be used to reference TransactionOutputs that have not yet been spent. 
 * The transactionOutputId will be used to find the relevant TransactionOutput, 
 * allowing miners to check your ownership.
 * 
 * Transaction inputs are references to previous transaction outputs.
 *
 */
public class TransactionInput {
	private static Logger logger = LoggerFactory.getLogger(TransactionInput.class);
	
	// Reference to TransactionOutputs -> transactionId
	private final String transactionOutputId;
	
	// Contains the Unspent transaction output
	private TransactionOutput UTXO; 

	/**
	 * Constructor.
	 * 
	 * @param transactionOutputId is the output to be used
	 */
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	
	// Getters and Setters
	public String getTransactionOutputId() {
		return transactionOutputId;
	}
	public TransactionOutput getUTXO() {
		return UTXO;
	}
	public void setUTXO(TransactionOutput uTXO) {
		UTXO = uTXO;
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
