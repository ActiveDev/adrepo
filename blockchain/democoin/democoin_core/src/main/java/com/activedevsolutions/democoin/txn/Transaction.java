package com.activedevsolutions.democoin.txn;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.BlockChain;
import com.activedevsolutions.democoin.CurrencyFormat;
import com.activedevsolutions.democoin.UTXOCache;
import com.activedevsolutions.democoin.security.SecurityUtil;

/**
 * Represents a single transaction.
 *
 */
public class Transaction {
	private static Logger logger = LoggerFactory.getLogger(Transaction.class);
	
	// Constants
	private static final CurrencyFormat MIN_TXN = new CurrencyFormat(0.1);

	// Senders address/public key.
	private final PublicKey sender;
	
	// Recipients address/public key.
	private final PublicKey recipient; 

	// Contains the amount we wish to send to the recipient.
	private final CurrencyFormat value; 
	
	// Inputs and Outputs
	private final List<TransactionInput> inputs;
	private final List<TransactionOutput> outputs = new ArrayList<>();

	// Contains a hash of transaction*
	private String transactionId; 
	// This is to prevent anybody else from spending funds in our wallet.
	private byte[] signature; 

	// Avoid 2 identical transactions having the same hash
	private final String sequence; 

	/**
	 * Constructor to create Transaction with mandatory fields.
	 * 
	 * @param from public key of the sender
	 * @param to public key of the recipient
	 * @param value is the amount to send
	 * @param inputs are the transaction inputs
	 */
	public Transaction(PublicKey from, PublicKey to, CurrencyFormat value, List<TransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
		sequence = UUID.randomUUID().toString();
	}

	// Getters and Setters
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public PublicKey getSender() {
		return sender;
	}
	public PublicKey getRecipient() {
		return recipient;
	}
	public CurrencyFormat getValue() {
		return value;
	}
	public List<TransactionInput> getInputs() {
		return inputs;
	}
	public List<TransactionOutput> getOutputs() {
		return outputs;
	}
	public byte[] getSignature() {
		return signature;
	}
	
	/**
	 * Processes the transaction to ensure it is valid.
	 * 
	 * @return boolean indicating success/failure
	 */
	public boolean processTransaction() {
		if (!verifySignature()) {
			logger.info("#Transaction Signature failed to verify");
			return false;
		}

		// Gathers transaction inputs (Making sure they are unspent):
		for (TransactionInput i : inputs) {
			i.setUTXO(UTXOCache.INSTANCE.getUTXOs().get(i.getTransactionOutputId()));
			//TODO this isn't really doing anything
		}

		// Checks if transaction is valid:
		if (getInputsValue().compareTo(MIN_TXN) == -1) {
			logger.info("Transaction Inputs to small: " + getInputsValue());
			return false;
		}

		// Generate transaction outputs:
		// get value of inputs then the left over change:
		CurrencyFormat leftOver = getInputsValue().subtract(value); 
		transactionId = calulateHash();
		
		// send value to recipient
		outputs.add(new TransactionOutput(this.recipient, value, transactionId));
		
		// send the left over change back to sender
		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); 

		// Add outputs to Unspent list
		for (TransactionOutput o : outputs) {
			UTXOCache.INSTANCE.getUTXOs().add(o.getId(), o);
		}

		// Remove transaction inputs from UTXO lists as spent:
		for (TransactionInput i : inputs) {
			if (i.getUTXO() == null)
				// if Transaction can't be found skip it
				continue;
			UTXOCache.INSTANCE.getUTXOs().remove(i.getUTXO().getId());
		}

		return true;
	}

	/**
	 * Gets the value of the inputs.
	 * 
	 * @return float holding the value
	 */
	public CurrencyFormat getInputsValue() {
		CurrencyFormat total = new CurrencyFormat(0);
		for (TransactionInput i : inputs) {
			if (i.getUTXO() == null) {
				 // if Transaction can't be found skip it, This behavior may not be optimal.
				continue;
			}
			
			total = total.add(i.getUTXO().getValue());
		}
		return total;
	}

	/**
	 * Gets the value of the outputs.
	 * 
	 * @return float holding the value
	 */
	public CurrencyFormat getOutputsValue() {
		CurrencyFormat total = new CurrencyFormat(0);
		for (TransactionOutput o : outputs) {
			total = total.add(o.getValue());
		}
		return total;
	}

	/**
	 * Generate a signature based on a private key
	 * @param privateKey is the key to use for the signature
	 */
	public void generateSignature(PrivateKey privateKey) {
		String data = SecurityUtil.getStringFromKey(sender) + SecurityUtil.getStringFromKey(recipient)
				+ value.toPlainString();
		signature = SecurityUtil.applyECDSASig(privateKey, data);
	}

	/**
	 * Verify the signature based on the public key of the sender.
	 * 
	 * @return boolean indicating success/failure
	 */
	public boolean verifySignature() {
		String data = SecurityUtil.getStringFromKey(sender) + SecurityUtil.getStringFromKey(recipient)
				+ value.toPlainString();
		return SecurityUtil.verifyECDSASig(sender, data, signature);
	}

	/**
	 * Calculate the hash of the transaction.
	 * 
	 * @return String containing the hash
	 */
	private String calulateHash() {		
		return SecurityUtil.applySha256(getContents());
	}
	
	/**
	 * Gets the contents that are used to create the hash for the txn.
	 * 
	 * @return String containing the contents of the txn
	 */
	private String getContents() {
		StringBuilder sb = new StringBuilder();
		sb.append(SecurityUtil.getStringFromKey(sender));
		sb.append(SecurityUtil.getStringFromKey(recipient));
		sb.append(value);
		sb.append(sequence);
		
		return sb.toString();
	}
	
	/**
	 * Validate this transaction to ensure the signature and values match.
	 * 
	 * @param tempUTXOs is a list of unspent txns passed down from the blockchain
	 * 					to include the genesis txn
	 * @return boolean indicating if the transaction is valid
	 */
	public boolean validate(Map<String, TransactionOutput> tempUTXOs) {
		boolean result = true;
		
		if (!verifySignature()) {
			logger.info("#Signature on Transaction({}) is Invalid", transactionId);
			result = false;
		} // end if
		if (getInputsValue().compareTo(getOutputsValue()) != 0) {
			logger.info("#Inputs are not equal to outputs on Transaction({})", transactionId);
			result = false;
		} // end if

		TransactionOutput tempOutput;
		for (TransactionInput input : getInputs()) {
			tempOutput = tempUTXOs.get(input.getTransactionOutputId());

			if (tempOutput == null) {
				logger.info("#Referenced input on Transaction({}) is Missing", transactionId);
				result = false;
			} 
			else if (input.getUTXO().getValue() != tempOutput.getValue()) {
				logger.info("#Referenced input Transaction({}) value is Invalid", transactionId);
				result = false;
			} // end if
			tempUTXOs.remove(input.getTransactionOutputId());
		} // end for

		for (TransactionOutput output : getOutputs()) {
			tempUTXOs.put(output.getId(), output);
		} // end for

		if (!getOutputs().get(0).getRecipient().equals(getRecipient())) {
			logger.info("#Transaction() output reciepient is not who it should be", transactionId);
			result = false;
		} // end if
		if (!getOutputs().get(1).getRecipient().equals(getSender())) {
			logger.info("#Transaction({}) output 'change' is not sender.", transactionId);
			result = false;
		} // end if

		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	
		sb.append(BlockChain.PRINT_NEWLINE);
		sb.append("Transaction: ");
		sb.append(BlockChain.PRINT_NEWLINE);
		sb.append("To: ");
		sb.append(sender.toString());
		sb.append("From: ");
		sb.append(recipient.toString());
		sb.append("Amount: ");
		sb.append(value);
		sb.append(BlockChain.PRINT_NEWLINE);
		sb.append("Inputs: ");
		sb.append(inputs);
		sb.append(BlockChain.PRINT_NEWLINE);
		sb.append("Outputs: ");
		sb.append(outputs);
		
		return sb.toString();
	}
}
