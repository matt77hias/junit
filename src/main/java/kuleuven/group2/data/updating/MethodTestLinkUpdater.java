package kuleuven.group2.data.updating;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;
import kuleuven.group2.testrunner.TestRunner;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import be.kuleuven.cs.ossrewriter.Monitor;

/**
 * Updates the link between methods and tests when tests are run.
 * 
 * @author Group2
 * @version 15 November 2013
 */
public class MethodTestLinkUpdater extends Monitor {

	protected final TestDatabase testDatabase;
	protected CurrentRunningTestHolder currentTestHolder;
	protected final OssRewriterLoader ossRewriterLoader;

	public MethodTestLinkUpdater(TestDatabase testDatabase, OssRewriterLoader ossRewriterLoader) {
		this.testDatabase = testDatabase;
		this.ossRewriterLoader = ossRewriterLoader;
	}

	public void registerTestHolder(CurrentRunningTestHolder currentTestHolder) {
		this.currentTestHolder = currentTestHolder;
	}

	public void registerTestHolder(TestRunner testRunner) {
		MethodTestLinkRunListener listener = new MethodTestLinkRunListener();
		testRunner.addRunListener(listener);
		this.currentTestHolder = listener;
	}

	@Override
	public void enterMethod(String methodName) {
		JavaSignature signature = new JavaSignatureParser(methodName).parseSignature();
		TestedMethod enteredMethod = testDatabase.getMethod(signature);
		if (enteredMethod != null) {
			Test currentTest = currentTestHolder.getCurrentRunningTest();
			testDatabase.addMethodTestLink(enteredMethod, currentTest);
		}
	}

	private class MethodTestLinkRunListener extends RunListener implements CurrentRunningTestHolder {

		private Test currentTest = null;

		@Override
		public Test getCurrentRunningTest() {
			return currentTest;
		}

		@Override
		public void testRunStarted(Description description) throws Exception {
			// Clear all links before test run
			testDatabase.clearMethodTestLinks();
		}

		@Override
		public void testStarted(Description description) throws Exception {
			// Store current test
			String testClassName = description.getClassName();
			String testMethodName = description.getMethodName();
			currentTest = new Test(testClassName, testMethodName);
			// Monitor method calls
			ossRewriterLoader.registerMonitor(MethodTestLinkUpdater.this);
		}

		@Override
		public void testFinished(Description description) throws Exception {
			// Clear current test
			currentTest = null;
			// Stop monitoring method calls
			ossRewriterLoader.unregisterMonitor(MethodTestLinkUpdater.this);
		}

	}

}
