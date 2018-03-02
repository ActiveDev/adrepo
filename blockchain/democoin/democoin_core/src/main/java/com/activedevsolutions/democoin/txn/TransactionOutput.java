package com.activedevsolutions.democoin.txn;

import java.security.PublicKey;

import com.activedevsolutions.democoin.BlockChain;
import com.activedevsolutions.democoin.CurrencyFormat;
import com.activedevsolutions.democoin.security.SecurityUtil;

/**
 * Transaction outputs will show the final amount sent to each party from the transaction. 
 * These, when referenced as inputs in new transactions, act as proof that you have coins to send.
 *
 */
public class TransactionOutput {
	private String id;
	
	// also known as the new owner of these coins.
	private final PublicKey recipient;
	
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
	public TransactionOutput(PublicKey reciepient, CurrencyFormat value, String parentTransactionId) {
		this.recipient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = SecurityUtil.applySha256(SecurityUtil.getStringFromKey(reciepient) + value.toPlainString() + 
				parentTransactionId);
	}

	// Getters and Setters
	public String getId() {
		return id;
	}
	public PublicKey getRecipient() {
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
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == recipient);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(BlockChain.PRINT_DELIMITER);
		sb.append(recipient);
		sb.append(BlockChain.PRINT_DELIMITER);
		sb.append(value);
		sb.append(BlockChain.PRINT_DELIMITER);
		sb.append(parentTransactionId);
		
		return sb.toString();
	}
}
