package com.activedevsolutions.democoin;

import com.activedevsolutions.democoin.txn.UnspentTxns;

/**
 * Provides a single copy of the blockchain to the jvm. This is not done in the
 * blockchain itself because it needs to be marshalled and un-marshalled across nodes.
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
	
	public UnspentTxns getUTXOs() {
		return UTXOs;
	}
}
