package com.activedevsolutions.democoin.security;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.activedevsolutions.democoin.txn.Transaction;

/**
 * Contains common security functions.
 *
 */
public final class SecurityUtil {
	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	//TODO Make all of these properties
	public static final String KEY_ALGO = "ECDSA";
	public static final String PROVIDER = "BC";
	public static final String RANDOM_ALGO = "SHA1PRNG";
	public static final String ECGEN_NAME = "prime192v1";
	
	private static final String DIGEST = "SHA-256";
	private static final String ENCODING = "UTF-8";
	
	/**
	 * Prevent this class from being created.
	 */
	private SecurityUtil() {
		return;
	}

	/**
	 * Applies Sha256 to a string and returns the result.
	 * 
	 * @param input is the data to hash
	 * @return String containing the hashed data
	 */
	public static String applySha256(String input) {

		try {
			MessageDigest digest = MessageDigest.getInstance(DIGEST);

			// Applies sha256 to our input,
			byte[] hash = digest.digest(input.getBytes(ENCODING));

			// This will contain hash as hexidecimal
			StringBuilder hexString = new StringBuilder(); 
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Applies ECDSA Signature and returns the result ( as bytes ).
	 * 
	 * @param privateKey is the key to use for the signature
	 * @param input is the data to sign
	 * @return byte array containing the the signed data
	 */
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance(KEY_ALGO, PROVIDER);
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	/**
	 * Verifies a String signature.
	 * 
	 * @param publicKey is the key to use for verification
	 * @param data is the data to verify
	 * @param signature is the signature to verify against
	 * 
	 * @return boolean indicating if the signature is valid or not
	 */
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance(KEY_ALGO, PROVIDER);
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns difficulty string target, to compare to hash. eg difficulty of 5
	 * will return "00000".
	 * 
	 * @param difficulty is how many zeros to match against
	 * @return String containing the zeros to look for in the hash
	 */
	public static String getDificultyString(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

	/**
	 * There can be many transactions. This method helps create a string
	 * that can be used as part of the contents of the block to be hashed.
	 * 
	 * @param transactions is the list of transactions to "simplify"
	 * @return String containing a representation of the transactions
	 */
	public static String getMerkleRoot(List<Transaction> transactions) {
		int count = transactions.size();

		List<String> previousTreeLayer = new ArrayList<>();
		for (Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.getTransactionId());
		}
		List<String> treeLayer = previousTreeLayer;

		while (count > 1) {
			treeLayer = new ArrayList<>();
			for (int i = 1; i < previousTreeLayer.size(); i += 2) {
				treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}

		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		logger.debug("Merkle Root: {}", merkleRoot);
		
		return merkleRoot;
	}
}