package com.activedevsolutions.instrument;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ClassLogger implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, 
			byte[] classfileBuffer) throws IllegalClassFormatException {
		try {
			System.out.println("Classname: " + className);
		} 
		catch (Throwable ignored) { 
			ignored.printStackTrace();
		} 

		return classfileBuffer;
	}
}
