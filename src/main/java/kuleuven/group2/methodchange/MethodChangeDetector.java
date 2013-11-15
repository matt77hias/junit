package kuleuven.group2.methodchange;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Map;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;

public class MethodChangeDetector {

	protected final TestDatabase database;

	public MethodChangeDetector(TestDatabase database) {
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
			detectChanges(entry.getKey(), entry.getValue(), timestamp);
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
		for (Map.Entry<String, MethodHash> entry : methodHashes.entrySet()) {
			JavaSignature signature = new JavaSignatureParser(entry.getKey()).parseSignature();
			updateMethodHash(signature, entry.getValue(), timestamp);
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
		TestedMethod method = database.getMethod(signature);
		if (method == null) {
			method = new TestedMethod(signature);
			database.addMethod(method);
		}
		// Update hash
		if (!newHash.equals(method.getHash())) {
			method.setHash(newHash);
			method.setLastChange(timestamp);
		}
	}

}
