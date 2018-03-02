package com.activedevsolutions.democoin.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.GeneralSecurityException;

import org.junit.Test;

import com.activedevsolutions.democoin.security.KeyManager;

public class KeyManagerTest {
	@Test
	public void testCreateKeyManager() {
		try {
			KeyManager keyManager = KeyManager.createKeyManager();
			assertTrue(keyManager.getPublicKey() != null);
			assertTrue(keyManager.getPrivateKey() != null);
		} 
		catch (GeneralSecurityException e) {
			e.printStackTrace();
			fail();
		}		
	}

	@Test
	public void testCreateKeyManagerWithKeys() {
		try {
			KeyManager keyManager = KeyManager.createKeyManager();
			String origPublicKey = keyManager.getPublicKeyString();
			String origPrivateKey = keyManager.getPrivateKeyString();

			KeyManager derivedKeyManager = KeyManager.createKeyManager(origPrivateKey, origPublicKey);
			String newPublicKey = derivedKeyManager.getPublicKeyString();
			String newPrivateKey = derivedKeyManager.getPrivateKeyString();
			
			assertTrue(origPublicKey.equals(newPublicKey));
			assertTrue(origPrivateKey.equals(newPrivateKey));
		} 
		catch (GeneralSecurityException e) {
			e.printStackTrace();
			fail();
		}		
	}
	
	@Test
	public void testGetPrivateKeyString() {
		KeyManager keyManager;
		try {
			keyManager = KeyManager.createKeyManager();
			String generatedKey = keyManager.getPrivateKeyString();
			assertTrue(generatedKey!= null);
		} 
		catch (GeneralSecurityException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	public void testGetPublicKeyString() {
		KeyManager keyManager;
		try {
			keyManager = KeyManager.createKeyManager();
			String generatedKey = keyManager.getPublicKeyString();
			assertTrue(generatedKey!= null);
		} 
		catch (GeneralSecurityException e) {
			e.printStackTrace();
			fail();
		}		
	}

}
