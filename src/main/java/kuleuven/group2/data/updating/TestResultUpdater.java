package kuleuven.group2.data.updating;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Updates the data with the latest results when tests are run.
 * 
 * @author Vital D'haveloose, Ruben Pieters
 */
public class TestResultUpdater extends RunListener {

	private TestDatabase testDatabase;
	private Map<String, TestRun> successfulTestRuns = new HashMap<String, TestRun>();

	public TestResultUpdater(TestDatabase testDatabase) {
		this.testDatabase = testDatabase;
	}

	/**
	 * Creates a new testResultUpdater and registers it to the given JUnitCore
	 * as a listener.
	 */
	public TestResultUpdater(TestDatabase testDatabase, JUnitCore core) {
		this.testDatabase = testDatabase;
		core.addListener(this);
	}

	/**
	 * Called before any tests have been run.
	 * 
	 * @param description
	 *            describes the tests to be run
	 */
	@Override
	public void testRunStarted(Description description) throws Exception {
		// do nothing
	}

	/**
	 * Called when all tests have finished
	 * 
	 * @param result
	 *            the summary of the test run, including all the tests that
	 *            failed
	 */
	@Override
	public void testRunFinished(Result result) throws Exception {
		for (String key : successfulTestRuns.keySet()) {
			String[] parts = key.split(":");
			Test test = new Test(parts[0], parts[1]);
			testDatabase.addTestRun(successfulTestRuns.get(key), test);
		}
		successfulTestRuns.clear();
	}

	/**
	 * Called when an atomic test is about to be started.
	 * 
	 * @param description
	 *            the description of the test that is about to be run (generally
	 *            a class and method name)
	 */
	@Override
	public void testStarted(Description description) throws Exception {
		String key = getKeyFromDescription(description);
		TestRun testRun = TestRun.createSuccessful(new Date());

		successfulTestRuns.put(key, testRun);
	}

	/**
	 * Called when an atomic test has finished, whether the test succeeds or
	 * fails.
	 * 
	 * @param description
	 *            the description of the test that just ran
	 */
	@Override
	public void testFinished(Description description) throws Exception {
		// do nothing
	}

	/**
	 * Called when an atomic test fails.
	 * 
	 * @param failure
	 *            describes the test that failed and the exception that was
	 *            thrown
	 */
	@Override
	public void testFailure(Failure failure) throws Exception {
		generalTestFailure(failure);
	}

	/**
	 * Called when an atomic test flags that it assumes a condition that is
	 * false
	 * 
	 * @param failure
	 *            describes the test that failed and the
	 *            {@link AssumptionViolatedException} that was thrown
	 */
	@Override
	public void testAssumptionFailure(Failure failure) {
		generalTestFailure(failure);
	}

	private void generalTestFailure(Failure failure) {
		TestRun testRun = TestRun.createFailed(new Date(), failure);
		String key = getKeyFromDescription(failure.getDescription());
		Test test = getTestFromDescription(failure.getDescription());

		testDatabase.addTestRun(testRun, test);
		successfulTestRuns.remove(key);
	}

	/**
	 * Called when a test will not be run, generally because a test method is
	 * annotated with {@link org.junit.Ignore}.
	 * 
	 * @param description
	 *            describes the test that will not be run
	 */
	@Override
	public void testIgnored(Description description) throws Exception {
		// do nothing
	}

	private Test getTestFromDescription(Description description) {
		String testClassName = description.getClassName();
		String testMethodName = description.getMethodName();
		return new Test(testClassName, testMethodName);
	}

	private String getKeyFromDescription(Description description) {
		return description.getClassName() + ":" + description.getMethodName();
	}

}
