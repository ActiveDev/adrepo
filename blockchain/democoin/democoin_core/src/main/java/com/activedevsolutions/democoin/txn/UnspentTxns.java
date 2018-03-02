package com.activedevsolutions.democoin.txn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds collection of UTXOs for the blockchain.
 * This class is used to limit the colletion's exposure
 *
 */
public class UnspentTxns {
	private Map<String, TransactionOutput> UTXOs = new HashMap<>();
	
	public TransactionOutput get(String key) {
		//TODO Clone before returning
		return UTXOs.get(key);
	}
	
	public void add(String key, TransactionOutput output) {
		UTXOs.put(key, output);
	}
	
	public boolean remove(String key) {
		return (UTXOs.remove(key) != null);
	}
	
	public Collection<TransactionOutput> getValues() {
		//TODO Clone before returning
		return UTXOs.values();
	}
}
