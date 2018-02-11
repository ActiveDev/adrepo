package com.activedevsolutions.instrument;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class ClassLogger implements ClassFileTransformer {
	private static final Logger logger = LoggerFactory.getLogger(ClassFileTransformer.class);

	private static final String PROP_WHITE_LIST = "whiteList";
	private static final String PROP_BLACK_LIST = "blackList";
	private static final String PROP_IMPORT_PKG = "importPackage";
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] result = null;
		String whiteList = System.getProperty(PROP_WHITE_LIST);
		String blackList = System.getProperty(PROP_BLACK_LIST);
		String importPackage = System.getProperty(PROP_IMPORT_PKG);
		
		try {			
			// Blacklist (Not really a list yet)
			if (className.startsWith(blackList)) {
				return null;
			}

			// Whitelist (Not really a list yet)
			if (className.startsWith(whiteList)) {
				ClassPool cp = ClassPool.getDefault();
				cp.importPackage(importPackage);
				
				CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
	
				CtMethod[] declaredMethods = ct.getDeclaredMethods();
				for (CtMethod method : declaredMethods) {
					String timingName = "\"" + className + "." + method.getName() + "\"";
					
					method.insertBefore(
							" { " + "com.activedevsolutions.instrument.TimingStack.INSTANCE.push(" + timingName + ");" + "}");
					method.insertAfter("{ com.activedevsolutions.instrument.TimingStack.INSTANCE.pop(); }", true);			
				}

				result = ct.toBytecode();
			}
			else {
				result = classfileBuffer;
			} // end if else
		} 
		catch (Throwable e) {
			logger.error("An error occurred.", e);
		} // end try catch

		return result;
	}
}
