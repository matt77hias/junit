package kuleuven.group2.data;

import kuleuven.group2.store.StoreListener;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * The updater class listens to changes in the code as well as
 * the running of tests, and updates the necessary data accordingly.
 * 
 * @author vital.dhaveloose
 */

// TODO: is het wel ok dat deze klasse dat alletwee doet?
public class DatabaseUpdater extends RunListener implements StoreListener{
	
	private final TestRunDatabase testDatabase;
	private final TestedMethodDatabase methodDatabase;

	public DatabaseUpdater(TestRunDatabase testDatabase, TestedMethodDatabase methodDatabase) {
		this.testDatabase = testDatabase;
		this.methodDatabase = methodDatabase;
	}

	/**
	 * LISTENING TO CHANGES IN THE SOURCECODE
	 */
	
	
	public void resourceAdded(String resourceName) {
		// TODO Auto-generated method stub
		
	}

	public void resourceChanged(String resourceName) {
		// TODO Auto-generated method stub
		
	}

	public void resourceRemoved(String resourceName) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * LISTENING TO TEST RESULTS
	 */
	
    /**
     * Called before any tests have been run.
     *
     * @param description describes the tests to be run
     */
	@Override
    public void testRunStarted(Description description) throws Exception {
		//TODO
    }

    /**
     * Called when all tests have finished
     *
     * @param result the summary of the test run, including all the tests that failed
     */
	@Override
    public void testRunFinished(Result result) throws Exception {
		//TODO
    }

    /**
     * Called when an atomic test is about to be started.
     *
     * @param description the description of the test that is about to be run
     * (generally a class and method name)
     */
	@Override
    public void testStarted(Description description) throws Exception {
		//TODO
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     *
     * @param description the description of the test that just ran
     */
	@Override
    public void testFinished(Description description) throws Exception {
		//TODO
    }

    /**
     * Called when an atomic test fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
	@Override
    public void testFailure(Failure failure) throws Exception {
		//TODO
    }

    /**
     * Called when an atomic test flags that it assumes a condition that is
     * false
     *
     * @param failure describes the test that failed and the
     * {@link AssumptionViolatedException} that was thrown
     */
	@Override
    public void testAssumptionFailure(Failure failure) {
		//TODO
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated
     * with {@link org.junit.Ignore}.
     *
     * @param description describes the test that will not be run
     */
	@Override
    public void testIgnored(Description description) throws Exception {
		//TODO
    }

}
