package kuleuven.group2.data.updating;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

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

	protected TestDatabase testDatabase;
	protected Test currentTest;

	public MethodTestLinkUpdater(TestDatabase testDatabase, OssRewriterLoader ossRewriterLoader, JUnitCore core) {
		super();
		this.testDatabase = testDatabase;

		ossRewriterLoader.registerMonitor(this);
		core.addListener(new MethodTestLinkRunListener());
	}

	@Override
	public void enterMethod(String methodName) {
		JavaSignature signature = new JavaSignatureParser(methodName).parseSignature();
		TestedMethod enteredMethod = testDatabase.getMethod(signature);
		if (enteredMethod != null) {
			enteredMethod.addTest(currentTest);
			testDatabase.addMethod(enteredMethod);
		}
	}

	public void printMethodLinks() {
		testDatabase.printMethodLinks();
	}
	
	private class MethodTestLinkRunListener extends RunListener {
		
		protected MethodTestLinkRunListener() {
			// do nothing
		}

	    /**
	     * Called when an atomic test is about to be started.
	     *
	     * @param description the description of the test that is about to be run
	     * (generally a class and method name)
	     */
	    public void testStarted(Description description) throws Exception {
	    	String testClassName = description.getClassName();
	    	String testMethodName = description.getMethodName();
	    	currentTest = testDatabase.getTest(testClassName, testMethodName);
	    	
	    	// remove old links
	    	testDatabase.removeMethodLinks(currentTest);
	    }

	    /**
	     * Called when an atomic test has finished, whether the test succeeds or fails.
	     *
	     * @param description the description of the test that just ran
	     */
	    public void testFinished(Description description) throws Exception {
	    	currentTest = null;
	    }
	    
	}

}
