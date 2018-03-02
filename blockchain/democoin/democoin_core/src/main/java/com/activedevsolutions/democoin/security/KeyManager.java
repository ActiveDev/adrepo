package com.activedevsolutions.democoin.security;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles all functionality relating to public and private keys.
 *
 */
public class KeyManager {
	private static Logger logger = LoggerFactory.getLogger(KeyManager.class);
		
	private PrivateKey privateKey;
	private PublicKey publicKey;

	private Encoder encoder = Base64.getEncoder();

	/**
	 * Setup the security provide just once for the jvm.
	 */
	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		logger.info("Security provider initialized.");
	}

	/**
	 * Private constructor to set the keys from wihtin the createKeyManager
	 * method. This is necessary as we want to make the class immutable.
	 * 
	 * @param publicKey is the publickey to use
	 * @param privateKey is the privatekey to use
	 */
	private KeyManager(PrivateKey privateKey, PublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	
	// Getters and Setters
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Saves the generated private key to a string. 
	 * This is used for marshalling and unmarshalling of keys.
	 * 
	 * @throws GeneralSecurityException when any exception occurs dealing with the key
	 */
	public String getPrivateKeyString() throws GeneralSecurityException {
		KeyFactory fact = KeyFactory.getInstance(SecurityUtil.KEY_ALGO);
		PKCS8EncodedKeySpec spec = fact.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
		byte[] packed = spec.getEncoded();
		String key64 = encoder.encodeToString(packed);

		Arrays.fill(packed, (byte) 0);
		
		return key64;
	}

	/**
	 * Saves the generated public key to a string. 
	 * This is used for marshalling and unmarshalling of keys.
	 * 
	 * @throws GeneralSecurityException when any exception occurs dealing with the key
	 */
	public String getPublicKeyString() throws GeneralSecurityException {
		KeyFactory fact = KeyFactory.getInstance(SecurityUtil.KEY_ALGO);
		X509EncodedKeySpec spec = fact.getKeySpec(publicKey, X509EncodedKeySpec.class);
		
		return encoder.encodeToString(spec.getEncoded());
	}

	/**
	 * Generates a key pair based on the algo and provider.
	 */
	public static KeyManager createKeyManager() throws GeneralSecurityException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(SecurityUtil.KEY_ALGO, SecurityUtil.PROVIDER);
		SecureRandom random = SecureRandom.getInstance(SecurityUtil.RANDOM_ALGO);
		ECGenParameterSpec ecSpec = new ECGenParameterSpec(SecurityUtil.ECGEN_NAME);
		
		// Initialize the key generator and generate a KeyPair
		keyGen.initialize(ecSpec, random); // 256
		KeyPair keyPair = keyGen.generateKeyPair();
		logger.info("Keypair generated.");
		
		// Set the public and private keys from the keyPair
		return new KeyManager(keyPair.getPrivate(), keyPair.getPublic());
	}
	
	/**
	 * Creates a key manager from public and private key strings.
	 * 
	 * @param publicKey is a string representation of the public key
	 * @param privateKey is a string representation of the private key
	 * @return KeyManager is an instance of this class with the keys loaded
	 * @throws GeneralSecurityException when the public or private key cannot be created
	 */
	public static KeyManager createKeyManager(String privateKey, String publicKey) throws GeneralSecurityException {
		PrivateKey privateKeyObject = KeyManager.getPrivateKey(privateKey);
		PublicKey publicKeyObject = KeyManager.getPublicKey(publicKey);
		
		return new KeyManager(privateKeyObject, publicKeyObject);
	}
	
	/**
	 * Loads a private key from a string. 
	 * This is used for marshalling and unmarshalling of keys.
	 * 
	 * @param key64 is the string representation of the key
	 * @throws GeneralSecurityException when any exception occurs dealing with the key
	 */
	protected static PrivateKey getPrivateKey(String key64) throws GeneralSecurityException {
		Decoder decoder = Base64.getDecoder();
		byte[] clear = decoder.decode(key64);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
		KeyFactory fact = KeyFactory.getInstance(SecurityUtil.KEY_ALGO);
		PrivateKey localPrivateKey = fact.generatePrivate(keySpec);
		Arrays.fill(clear, (byte) 0);
		
		return localPrivateKey;
	}

	/**
	 * Loads a public key from a string. 
	 * This is used for marshalling and unmarshalling of keys.
	 * 
	 * @param key64 is the string representation of the key
	 * @throws GeneralSecurityException when any exception occurs dealing with the key
	 */
	protected static PublicKey getPublicKey(String stored) throws GeneralSecurityException {
		Decoder decoder = Base64.getDecoder();
		byte[] data = decoder.decode(stored);
		
		X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
		KeyFactory fact = KeyFactory.getInstance(SecurityUtil.KEY_ALGO);
		return fact.generatePublic(spec);
	}
}
