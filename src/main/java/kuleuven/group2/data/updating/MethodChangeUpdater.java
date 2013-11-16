package kuleuven.group2.data.updating;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Map;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.hash.MethodHash;
import kuleuven.group2.data.hash.MethodHasher;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;

public class MethodChangeUpdater {

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
		for (Map.Entry<String, MethodHash> entry : methodHashes.entrySet()) {
			String methodName = entry.getKey();
			MethodHash methodHash = entry.getValue();
			JavaSignature signature = new JavaSignatureParser(className + "." + methodName).parseSignature();
			updateMethodHash(signature, methodHash, timestamp);
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
