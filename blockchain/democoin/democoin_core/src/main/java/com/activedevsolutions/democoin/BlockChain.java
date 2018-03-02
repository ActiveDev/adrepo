package com.activedevsolutions.democoin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.exception.DemoCoinException;
import com.activedevsolutions.democoin.security.SecurityUtil;
import com.activedevsolutions.democoin.txn.Transaction;
import com.activedevsolutions.democoin.txn.TransactionOutput;
import com.activedevsolutions.democoin.wallet.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents the block chain itself. It contains a list of
 * blocks along with the unspent transactions.
 *
 */
public final class BlockChain {
	private static Logger logger = LoggerFactory.getLogger(BlockChain.class);
		
	private List<Block> blockList = new ArrayList<>();

	private String hashTarget;
	private Block genesisBlock;
	private Wallet coinbase;
	private Wallet startingWallet;
	private CurrencyFormat startingCoins;

	/**
	 * Constructor used when the block chain has not yet been created.
	 * In other words, this is the very first node of the blockchain.
	 * 
	 * @param difficulty is the difficulty of the proof of work
	 * @param startingCoins is the total number of coins that will ever be available
	 * @param coinbase is the wallet that initiates the starting coins
	 * @param startingWallet is the wallet where all of the coins for circulation will be held
	 */
	public BlockChain(int difficulty, double startingCoins, Wallet coinbase, Wallet startingWallet) {
		this(difficulty);
		this.startingCoins = new CurrencyFormat(startingCoins);
		this.startingWallet = startingWallet;
		this.coinbase = coinbase;
	}

	/**
	 * Constructor used when the blockchain has already been established.
	 */
	public BlockChain(int difficulty) {
		//TODO Add UnspentTxns here so that it can be serialized and de-serialized
		hashTarget = SecurityUtil.getDificultyString(difficulty);
	}

	// Getters
	public String getGenesisBlockHash() {
		return genesisBlock.getHash();
	}
	public List<Block> getBlockList() {
		return blockList;
	}
	public String getHashTarget() {
		return hashTarget;
	}
	public Block getGenesisBlock() {
		return genesisBlock;
	}
	public Wallet getCoinbase() {
		return coinbase;
	}
	public Wallet getStartingWallet() {
		return startingWallet;
	}	
	public CurrencyFormat getStartingCoins() {
		return startingCoins;
	}

	/**
	 * Creates the genesis block to start the blockchain.
	 * 
	 * @param coinbase is a seed wallet to act as the sender
	 * @param startingWallet is the wallet that will receive funds. It will contain the total circulating supply.
	 * 
	 * @throws DemoCoinException
	 */
	public void createGenesisBlock() throws DemoCoinException {
		if (!blockList.isEmpty()) {
			throw new DemoCoinException("Cannot create genesis block. Blocks already exist.");
		} // end if
				
		// create genesis transaction, which sends coins to the starting wallet:
		Transaction genesisTransaction = new Transaction(coinbase.getKeyManager().getPublicKeyString(), 
				startingWallet.getKeyManager().getPublicKeyString(), startingCoins, new ArrayList<>());

		// manually sign the genesis txn
		genesisTransaction.generateSignature(coinbase.getKeyManager().getPrivateKey());
		
		// manually set the transaction id
		genesisTransaction.setTransactionId("0"); 
		
		// manually add the Transactions Output
		genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), 
				genesisTransaction.getValue(), genesisTransaction.getTransactionId())); 
		
		// its important to store our first txn in the UTXOs list
		UTXOCache.INSTANCE.getUTXOs().add(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); 

		logger.info("Creating and Mining Genesis block... ");
		genesisBlock = new Block("0");
		genesisBlock.addTransaction(genesisTransaction);
		addBlock(genesisBlock);
	}
	
	/**
	 * Adds a block to the chain.
	 * 
	 * @param newBlock is the block to add
	 */
	public void addBlock(Block newBlock) {
		newBlock.mineBlock(hashTarget);
		blockList.add(newBlock);
	}

	/**
	 * Determines if the chain is valid.
	 * 
	 * @return boolean indicating if the chain is valid
	 */
	public boolean validate() {
		Block currentBlock;
		Block previousBlock;
		
		// a temp working list of unspent txns at a given block state
		Map<String, TransactionOutput> tempUTXOs = new HashMap<>();
		if (genesisBlock == null) {
			logger.info("#No genesis block found.");
			return false;
		} // end if

		Transaction genesisTxn = genesisBlock.getTransactions().get(0);
		tempUTXOs.put(genesisTxn.getOutputs().get(0).getId(), genesisTxn.getOutputs().get(0));	

		// loop through blockchain to check hashes:
		for (int i = 1; i < blockList.size(); i++) {
			currentBlock = blockList.get(i);
			previousBlock = blockList.get(i - 1);
			
			// compare registered hash and calculated hash:
			if (!currentBlock.validate(hashTarget, tempUTXOs)) {
				logger.info("#Current Block not valid");
				return false;
			}
			
			// compare previous hash and registered previous hash
			if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				logger.info("#Previous Hashes not equal");
				return false;
			}
		}
		logger.info("Blockchain is valid");
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} 
		catch (JsonProcessingException e) {
			logger.error("Unable to generate json.", e);
		} // end try catch
		
		return result;
	}
}