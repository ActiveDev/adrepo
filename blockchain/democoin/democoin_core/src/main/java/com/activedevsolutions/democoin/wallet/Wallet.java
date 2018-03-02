package com.activedevsolutions.democoin.wallet;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.CurrencyFormat;
import com.activedevsolutions.democoin.UTXOCache;
import com.activedevsolutions.democoin.security.KeyManager;
import com.activedevsolutions.democoin.txn.Transaction;
import com.activedevsolutions.democoin.txn.TransactionInput;
import com.activedevsolutions.democoin.txn.TransactionOutput;

/**
 * Represents a wallet for the blockchain.
 *
 */
public class Wallet {
	private static Logger logger = LoggerFactory.getLogger(Wallet.class);
	
	private KeyManager keyManager;
	private Map<String, TransactionOutput> UTXOs = new HashMap<>();

	/**
	 * Creates a new key value pair.
	 * @throws GeneralSecurityException when the key pair cannot be generated
	 */
	public void create() throws GeneralSecurityException {
		//TODO Generate key value pairs based off of a secret key
		keyManager = KeyManager.createKeyManager();
	}

	/**
	 * Opens the wallet with the key pair
	 * @throws GeneralSecurityException when the key pair cannot be loaded from strings
	 */	
	public void open(String privateKey, String publicKey) throws GeneralSecurityException {
		keyManager = KeyManager.createKeyManager(privateKey, publicKey);
	}
	
	// Getters and Setters
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public Map<String, TransactionOutput> getUTXOs() {
		return UTXOs;
	}

	public void setUTXOs(Map<String, TransactionOutput> uTXOs) {
		UTXOs = uTXOs;
	}
	
	/**
	 * Get the balance of the wallet based on the public key.
	 * 
	 * @return CurrencyFormat containing the value of the wallet
	 */
	public CurrencyFormat getBalance() {
		CurrencyFormat total = new CurrencyFormat(0);
		for (TransactionOutput UTXO : UTXOCache.INSTANCE.getUTXOs().getValues()) {			
			// if output belongs to me ( if coins belong to me )
			if (UTXO.isMine(keyManager.getPublicKey())) {
				// add it to our list of unspent transactions
				UTXOs.put(UTXO.getId(), UTXO); 
				total = total.add(UTXO.getValue());
			}
		}
		return total;
	}

	/**
	 * Sends funds to a recipient using their public key.
	 * 
	 * @param recipient is the public key of the wallet to send the funds to
	 * @param value is the amount to send
	 * @return Transaction containing the details of the transfer
	 */
	public Transaction sendFunds(PublicKey recipient, CurrencyFormat value) {
		if (getBalance().compareTo(value) == -1) {
			logger.info("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		ArrayList<TransactionInput> inputs = new ArrayList<>();

		CurrencyFormat total = new CurrencyFormat(0);
		for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			total = total.add(UTXO.getValue());
			inputs.add(new TransactionInput(UTXO.getId()));
			if (total.compareTo(value) == 1)
				break;
		}

		Transaction newTransaction = new Transaction(keyManager.getPublicKey(), recipient, value, inputs);
		newTransaction.generateSignature(keyManager.getPrivateKey());

		for (TransactionInput input : inputs) {
			UTXOs.remove(input.getTransactionOutputId());
		}

		return newTransaction;
	}
}