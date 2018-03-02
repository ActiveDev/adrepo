package com.activedevsolutions.democoin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.security.SecurityUtil;
import com.activedevsolutions.democoin.txn.Transaction;
import com.activedevsolutions.democoin.txn.TransactionOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a block in the blockchain.
 *
 */
public class Block {
	private static Logger logger = LoggerFactory.getLogger(Block.class);
	
	private final String previousHash;
	private final List<Transaction> transactions = new ArrayList<>();
	private final long timeStamp;
	private String hash;
	private String merkleRoot;
	private int nonce;

	/**
	 * Block Constructor.
	 * 
	 * @param previousHash is the hash from the previous block
	 */
	public Block(String previousHash) {
		this.previousHash = previousHash;
		
		// Calculate timestamp
		Instant instant = Instant.now();
		this.timeStamp = instant.toEpochMilli();
	}

	// Getters and Setters
	public String getPreviousHash() {
		return previousHash;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public String getHash() {
		return hash;
	}
	public String getMerkleRoot() {
		return merkleRoot;
	}
	public int getNonce() {
		return nonce;
	}
	
	/**
	 * Applies a proof of work in order to determine the correct
	 * hash for this block.
	 * 
	 * @param target is the number of 0s to look for in the hash
	 * 				the number is determined by the difficulty
	 * 				set in the blockchain
	 */
	public void mineBlock(String target) {
		merkleRoot = SecurityUtil.getMerkleRoot(transactions);
				
		// Keep hashing the contents of the block until
		// the first x number of chars in the hash start with "0"'s
		do {
			nonce++;
			hash = SecurityUtil.applySha256(getContents());			
		} while (!hash.startsWith(target));

		logger.info("Block Mined!!! : {}", hash);
	}

	/**
	 * Gets the contents of this block to be used as part of the hash.
	 * 
	 * @return String containing the contents of the block
	 */
	private String getContents() {
		StringBuilder sb = new StringBuilder();
		sb.append(previousHash);
		sb.append(timeStamp);
		sb.append(nonce);
		sb.append(merkleRoot);
		
		return sb.toString();
	}

	/**
	 * Adds a transaction to the block.
	 * 
	 * @param transaction is the transaction to add
	 * @return boolean indicating if the txn was added to the block
	 */
	public boolean addTransaction(Transaction transaction) {
		boolean result = false;
		
		// process transaction and check if valid, unless block is genesis block
		if (transaction !=null && (previousHash.equals("0") || transaction.processTransaction())) {
			transactions.add(transaction);
			logger.info("Transaction Successfully added to Block");
			
			result = true;
		}
		else {
			logger.info("Transaction failed to process. Discarded.");
		} // end if else
					
		return result;
	}
	
	/**
	 * Determines if this block's hash matches a hash
	 * that is recalculated.
	 * 
	 * @param target is the number of 0s to look for in the hash
	 * 				the number is determined by the difficulty
	 * 				set in the blockchain
	 *  
	 * @return boolean indicating success/failure
	 */
	public boolean validate(String target, Map<String, TransactionOutput> tempUTXOs) {
		boolean result = true;
		String calculatedHash = SecurityUtil.applySha256(getContents());
		
		if (!calculatedHash.equals(this.hash)) {
			logger.info("#This block's hash does not match");
			result = false;
		} // end if
		
		// check if hash is solved
		if (!hash.startsWith(target)) {
			logger.info("#This block hasn't been mined");
			result = false;
		} // end if
		
		// loop through transactions for this block
		for (int t = 0; t < getTransactions().size(); t++) {
			Transaction currentTransaction = getTransactions().get(t);
			result = currentTransaction.validate(tempUTXOs);
		} // end for
		
		return result;
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
