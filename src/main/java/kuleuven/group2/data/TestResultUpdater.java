package kuleuven.group2.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kuleuven.group2.data.testrun.FailedTestRun;
import kuleuven.group2.data.testrun.SuccesfullTestRun;
import kuleuven.group2.data.testrun.TestRun;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Updates the data with the latest results when tests are run. To do this, this class
 * extends RunListener.
 * @author vital.dhaveloose
 *
 */
public class TestResultUpdater extends RunListener{
	
	private TestDatabase testDatabase;
	private Map<String[], TestRun> successfulTestRuns = new HashMap<String[], TestRun>();
	
    public TestResultUpdater(TestDatabase testDatabase) {
		this.testDatabase = testDatabase;
	}

	/**
     * Called before any tests have been run.
     *
     * @param description describes the tests to be run
     */
    public void testRunStarted(Description description) throws Exception {
    	// do nothing
    }

    /**
     * Called when all tests have finished
     *
     * @param result the summary of the test run, including all the tests that failed
     */
    public void testRunFinished(Result result) throws Exception {
    	for(String[] pseudoSignature : successfulTestRuns.keySet()) {
    		testDatabase.addTestRun(successfulTestRuns.get(pseudoSignature), pseudoSignature[0] , pseudoSignature[1]);
    	}
    	successfulTestRuns.clear(); //TODO: nodig, of bij volgende run nieuwe updater?
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
    	String[] key = {testClassName, testMethodName};
    	
    	TestRun testRun = new SuccesfullTestRun(new Date());
    	successfulTestRuns.put(key, testRun);
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     *
     * @param description the description of the test that just ran
     */
    public void testFinished(Description description) throws Exception {
    	// do nothing
    }

    /**
     * Called when an atomic test fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    public void testFailure(Failure failure) throws Exception {
    	generalTestFailure(failure);
    }

    /**
     * Called when an atomic test flags that it assumes a condition that is
     * false
     *
     * @param failure describes the test that failed and the
     * {@link AssumptionViolatedException} that was thrown
     */
    public void testAssumptionFailure(Failure failure) {
    	generalTestFailure(failure);
    }
    
    private void generalTestFailure(Failure failure) {
    	String testClassName = failure.getDescription().getClassName();
    	String testMethodName = failure.getDescription().getMethodName();
    	
    	TestRun testRun = new FailedTestRun(new Date());
    	
    	testDatabase.addTestRun(testRun, testClassName, testMethodName);
    	successfulTestRuns.remove(testClassName + "." + testMethodName);    	
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated
     * with {@link org.junit.Ignore}.
     *
     * @param description describes the test that will not be run
     */
    public void testIgnored(Description description) throws Exception {
    	// do nothing
    }
	
}
