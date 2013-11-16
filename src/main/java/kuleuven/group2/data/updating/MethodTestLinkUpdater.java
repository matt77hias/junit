package kuleuven.group2.data.updating;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;
import be.kuleuven.cs.ossrewriter.Monitor;

/**
 * This class updates the link between methods and tests, when tests are run.
 * 
 * @author Vital D'haveloose, Ruben Pieters
 */
public class MethodTestLinkUpdater extends Monitor {

	// TODO: doen met een RunListener (inner class), en als een test start: alle
	// links met die test flushen

	protected ICurrentRunningTestHolder currentRunningTestHolder;
	protected TestDatabase testDatabase;

	public MethodTestLinkUpdater(ICurrentRunningTestHolder currentRunningTestHolder, TestDatabase testDatabase,
			OssRewriterLoader ossRewriterLoader) {
		super();
		this.currentRunningTestHolder = currentRunningTestHolder;
		this.testDatabase = testDatabase;

		ossRewriterLoader.registerMonitor(this);
	}

	@Override
	public void enterMethod(String methodName) {
		Test currentRunningTest = currentRunningTestHolder.getCurrentRunningTest();
		JavaSignature signature = new JavaSignatureParser(methodName).parseSignature();
		TestedMethod enteredMethod = testDatabase.getMethod(signature);
		if (enteredMethod != null) {
			enteredMethod.addTest(currentRunningTest);
			testDatabase.addMethod(enteredMethod);
		}
	}

	public void printMethodLinks() {
		testDatabase.printMethodLinks();
	}

}
