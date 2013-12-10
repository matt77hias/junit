package kuleuven.group2.data.updating;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.hash.MethodHash;
import kuleuven.group2.data.hash.MethodHasher;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;

/**
 * Detects method changes (using hashes) and updates the
 * test database accordingly.
 * 
 * @author Group2
 * @version 14 November 2013
 */
public class MethodChangeUpdater {
	
	private final static String CLASS_METHOD_DELIMITER = ".";

	protected final TestDatabase database;

	public MethodChangeUpdater(TestDatabase database) {
		this.database = checkNotNull(database);
	}

	/**
	 * Detect method changes in all given classes.
	 * 
	 * @param compiledClasses
	 *            Map from class names to class bytecodes.
	 */
	public void detectChanges(Map<String, byte[]> compiledClasses) {
		detectChanges(compiledClasses, new Date());
	}

	/**
	 * Detect method changes in all given classes.
	 * 
	 * @param compiledClasses
	 *            Map from class names to class bytecodes.
	 * @param timestamp
	 *            The time at which the changes where made.
	 */
	public void detectChanges(Map<String, byte[]> compiledClasses, Date timestamp) {
		for (Map.Entry<String, byte[]> entry : compiledClasses.entrySet()) {
			String className = entry.getKey();
			byte[] classBytes = entry.getValue();
			detectChanges(className, classBytes, timestamp);
		}
	}

	/**
	 * Detect method changes in the given class.
	 * 
	 * @param className
	 *            Name of of the class.
	 * @param classBytes
	 *            Bytecode of the class.
	 * @param timestamp
	 *            The time at which the changes where made.
	 */
	public void detectChanges(String className, byte[] classBytes, Date timestamp) {
		// Calculate all method hashes
		Map<String, MethodHash> methodHashes = new MethodHasher(classBytes).getHashes();
		// Remove old methods
		removeOldMethodNames(className, methodHashes.keySet());
		// Update hashes
		for (Map.Entry<String, MethodHash> entry : methodHashes.entrySet()) {
			String methodName = entry.getKey();
			MethodHash methodHash = entry.getValue();
			JavaSignature signature = new JavaSignatureParser(className + CLASS_METHOD_DELIMITER + methodName).parseSignature();
			updateMethodHash(signature, methodHash, timestamp);
		}
	}

	/**
	 * Remove old methods in the given class from the database.
	 * 
	 * @param className
	 *            The class name.
	 * @param newMethodNames
	 *            The set of new method names.
	 */
	protected void removeOldMethodNames(String className, Set<String> newMethodNames) {
		Set<JavaSignature> newSignatures = new HashSet<>();
		for (String methodName : newMethodNames) {
			JavaSignature signature = new JavaSignatureParser(className + CLASS_METHOD_DELIMITER + methodName).parseSignature();
			newSignatures.add(signature);
		}
		removeOldMethods(className, newSignatures);
	}

	/**
	 * Remove old methods in the given class from the database.
	 * 
	 * @param className
	 *            The class name.
	 * @param newMethodNames
	 *            The set of new method signatures.
	 */
	protected void removeOldMethods(String className, Set<JavaSignature> newSignatures) {
		Collection<TestedMethod> oldMethods = database.getMethodsIn(className);
		for (TestedMethod method : oldMethods) {
			if (! newSignatures.contains(method.getSignature())) {
				database.removeMethod(method);
			}
		}
	}

	/**
	 * Update the hash of a method in the database.
	 * 
	 * @param signature
	 *            The signature of the method.
	 * @param newHash
	 *            The newly computed hash of the method.
	 * @param timestamp
	 *            The time at which the changes where made.
	 */
	protected void updateMethodHash(JavaSignature signature, MethodHash newHash, Date timestamp) {
		// Get or create method
		TestedMethod method = database.getOrCreateMethod(signature);

		// Update hash
		if (!newHash.equals(method.getMethodHash())) {
			method.setMethodHash(newHash);
			method.setLastChange(timestamp);
		}
	}

	/**
	 * Remove all methods from the given classes in the database.
	 * 
	 * @param removedClassNames
	 *            The names of removed classes.
	 */
	public void removeClasses(Collection<String> removedClassNames) {
		for (String className : removedClassNames) {
			Collection<TestedMethod> classMethods = database.getMethodsIn(className);
			for (TestedMethod method : classMethods) {
				database.removeMethod(method);
			}
		}
	}

}
