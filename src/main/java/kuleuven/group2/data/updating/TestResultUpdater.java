package kuleuven.group2.data.updating;

import java.util.Set;

import kuleuven.group2.data.Test;

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
	
	// ATTRIBUTES
	
	protected Set<Test> tests;

	// CONSTRUCTION
	
    public TestResultUpdater(Set<Test> tests) {
		this.tests = tests;
	}
    
    // LISTENING TO TEST RUNNING

	/**
     * Called before any tests have been run.
     *
     * @param description describes the tests to be run
     */
    public void testRunStarted(Description description) throws Exception {
    	// TODO
    }

    /**
     * Called when all tests have finished
     *
     * @param result the summary of the test run, including all the tests that failed
     */
    public void testRunFinished(Result result) throws Exception {
    	// TODO
    }

    /**
     * Called when an atomic test is about to be started.
     *
     * @param description the description of the test that is about to be run
     * (generally a class and method name)
     */
    public void testStarted(Description description) throws Exception {
    	// TODO
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     *
     * @param description the description of the test that just ran
     */
    public void testFinished(Description description) throws Exception {
    	// TODO
    }

    /**
     * Called when an atomic test fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    public void testFailure(Failure failure) throws Exception {
    	// TODO
    }

    /**
     * Called when an atomic test flags that it assumes a condition that is
     * false
     *
     * @param failure describes the test that failed and the
     * {@link AssumptionViolatedException} that was thrown
     */
    public void testAssumptionFailure(Failure failure) {
    	// TODO
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated
     * with {@link org.junit.Ignore}.
     *
     * @param description describes the test that will not be run
     */
    public void testIgnored(Description description) throws Exception {
    	// TODO
    }
	
}
