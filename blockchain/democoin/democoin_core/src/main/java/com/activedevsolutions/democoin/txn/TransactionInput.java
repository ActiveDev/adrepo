package com.activedevsolutions.democoin.txn;

import com.activedevsolutions.democoin.BlockChain;

/**
 * This class will be used to reference TransactionOutputs that have not yet been spent. 
 * The transactionOutputId will be used to find the relevant TransactionOutput, 
 * allowing miners to check your ownership.
 * 
 * Transaction inputs are references to previous transaction outputs.
 *
 */
public class TransactionInput {
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
		StringBuilder sb = new StringBuilder();
		sb.append(transactionOutputId);
		sb.append(BlockChain.PRINT_DELIMITER);
		sb.append(UTXO);
		
		return sb.toString();
	}
}
