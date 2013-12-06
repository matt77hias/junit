package kuleuven.group2.data.updating;

import java.util.Date;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestBatch;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Updates the test database with the latest test results when tests are run.
 * 
 * @author Group2
 * @version 12 November 2013
 */
public class TestResultUpdater extends RunListener {

	protected final TestDatabase testDatabase;
	protected TestBatch currentTestBatch;
	protected boolean isCurrentTestSuccessful = true;

	public TestResultUpdater(TestDatabase testDatabase) {
		this.testDatabase = testDatabase;
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		currentTestBatch = testDatabase.createTestBatch(new Date());
	}

	@Override
	public void testStarted(Description description) throws Exception {
		isCurrentTestSuccessful = true;
	}

	@Override
	public void testFinished(Description description) throws Exception {
		if (isCurrentTestSuccessful) {
			Test test = getTest(description);
			TestRun testRun = TestRun.createSuccessful(test, new Date());
			testDatabase.addTestRun(testRun, currentTestBatch);
		}
	}

	private Test getTest(Description description) {
		String testClassName = description.getClassName();
		String testMethodName = description.getMethodName();
		return testDatabase.getOrCreateTest(testClassName, testMethodName);
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		generalTestFailure(failure);
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		generalTestFailure(failure);
	}

	private void generalTestFailure(Failure failure) {
		Test test = getTest(failure.getDescription());
		TestRun testRun = TestRun.createFailed(test, new Date(), failure);
		testDatabase.addTestRun(testRun, currentTestBatch);
		isCurrentTestSuccessful = false;
	}

}
