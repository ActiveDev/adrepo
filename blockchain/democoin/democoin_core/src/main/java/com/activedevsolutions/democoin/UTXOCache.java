package com.activedevsolutions.democoin;

import com.activedevsolutions.democoin.txn.UnspentTxns;

/**
 * Provides a single copy of the UnspentTxns to the jvm. The real reference is passed to
 * the blockchain itself because it needs to be marshalled and un-marshalled across nodes.
 * 
 * I'm not crazy about this at all. The only reason it is here is because we are 
 * caching the unspent transactions which also need to be part of the blockchain
 * and passed around between nodes.
 * 
 * More thought needs to be put in place to determine how to handle the unspent txns.
 * 
 */
public enum UTXOCache {
	INSTANCE;
	
	private UnspentTxns UTXOs = new UnspentTxns();
	
	// Getter
	public UnspentTxns getUTXOs() {
		return UTXOs;
	}
}
