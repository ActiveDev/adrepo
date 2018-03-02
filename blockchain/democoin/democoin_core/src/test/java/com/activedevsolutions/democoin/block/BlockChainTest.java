package com.activedevsolutions.democoin.block;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.activedevsolutions.democoin.Block;
import com.activedevsolutions.democoin.BlockChain;
import com.activedevsolutions.democoin.CurrencyFormat;
import com.activedevsolutions.democoin.wallet.Wallet;

public class BlockChainTest {

	@Test
	public void testOverall() {
		Wallet coinbase = new Wallet();
		Wallet startingWallet = new Wallet();
		
		try {
			BlockChain blockChain = new BlockChain(4, 100, coinbase, startingWallet);
			coinbase.create();
			startingWallet.create();
			blockChain.createGenesisBlock();

			// Create wallets:
			Wallet realUserWallet = new Wallet();
			realUserWallet.create();

			// testing
			Block block1 = new Block(blockChain.getGenesisBlockHash());
			System.out.println("\nWalletA's balance is: " + startingWallet.getBalance());
			System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
			block1.addTransaction(blockChain.getStartingWallet().sendFunds(realUserWallet.getKeyManager().getPublicKeyString(), new CurrencyFormat(40)));
			blockChain.addBlock(block1);
			System.out.println("\nWalletA's balance is: " + blockChain.getStartingWallet().getBalance());
			System.out.println("WalletB's balance is: " + realUserWallet.getBalance());

			Block block2 = new Block(block1.getHash());
			System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
			block2.addTransaction(blockChain.getStartingWallet().sendFunds(realUserWallet.getKeyManager().getPublicKeyString(), new CurrencyFormat(1000)));
			blockChain.addBlock(block2);
			System.out.println("\nWalletA's balance is: " + blockChain.getStartingWallet().getBalance());
			System.out.println("WalletB's balance is: " + realUserWallet.getBalance());

			Block block3 = new Block(block2.getHash());
			System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
			block3.addTransaction(realUserWallet.sendFunds(blockChain.getStartingWallet().getKeyManager().getPublicKeyString(), new CurrencyFormat(20)));
			System.out.println("\nWalletA's balance is: " + blockChain.getStartingWallet().getBalance());
			System.out.println("WalletB's balance is: " + realUserWallet.getBalance());
			blockChain.addBlock(block3);
			
			blockChain.validate();
			System.out.println(blockChain.toString());
		} 
		catch (Exception e) {
			e.printStackTrace();
			fail();
		} // end try catch
	}


}
