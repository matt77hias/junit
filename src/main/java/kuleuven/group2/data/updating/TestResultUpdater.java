package kuleuven.group2.data.updating;

import java.util.Date;

import kuleuven.group2.data.Test;
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
	protected boolean isCurrentTestSuccessful = true;

	public TestResultUpdater(TestDatabase testDatabase) {
		this.testDatabase = testDatabase;
	}
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		testDatabase.testRunStarted(description);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		isCurrentTestSuccessful = true;
	}

	@Override
	public void testFinished(Description description) throws Exception {
		if (isCurrentTestSuccessful) {
			TestRun testRun = TestRun.createSuccessful(new Date());
			testDatabase.addTestRun(testRun, getTest(description));
		}
	}

	private Test getTest(Description description) {
		String testClassName = description.getClassName();
		String testMethodName = description.getMethodName();
		Test test = new Test(testClassName, testMethodName);
		return test;
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
		TestRun testRun = TestRun.createFailed(new Date(), failure);
		testDatabase.addTestRun(testRun, getTest(failure.getDescription()));
		isCurrentTestSuccessful = false;
	}

}
