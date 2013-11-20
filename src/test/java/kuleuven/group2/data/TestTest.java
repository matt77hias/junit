package kuleuven.group2.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestTest {

	protected kuleuven.group2.data.Test test;

	protected final Failure failure = new Failure(Description.EMPTY, new Exception());

	@Before
	public void setUp() throws Exception {
		test = new kuleuven.group2.data.Test("testClass", "testMethod");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void lastFailureTimeTest() {
		test.addTestRun(TestRun.createFailed(new Date(1), failure));
		test.addTestRun(TestRun.createFailed(new Date(2), failure));
		test.addTestRun(TestRun.createFailed(new Date(3), failure));
		test.addTestRun(TestRun.createSuccessful(new Date(4)));

		assertEquals(new Date(3), test.getLastFailureTime());
	}

	@Test
	public void lastFailureTimeTestNoFailures() {
		test.addTestRun(TestRun.createSuccessful(new Date(3)));

		assertEquals(new Date(0), test.getLastFailureTime());
	}

	@Test
	public void lastFailureTimeTestNoTestRuns() {
		assertEquals(new Date(0), test.getLastFailureTime());
	}

	@Test
	public void failurePercentageTest() {
		test.addTestRun(TestRun.createSuccessful(new Date(1)));
		test.addTestRun(TestRun.createFailed(new Date(2), failure));
		test.addTestRun(TestRun.createFailed(new Date(3), failure));
		test.addTestRun(TestRun.createFailed(new Date(4), failure));
		test.addTestRun(TestRun.createSuccessful(new Date(5)));
		test.addTestRun(TestRun.createSuccessful(new Date(6)));
		test.addTestRun(TestRun.createFailed(new Date(7), failure));

		assertEquals(0.5f, test.getFailurePercentage(4), 0.001f);
	}

	@Test
	public void failurePercentageTestTooHighDepth() {
		test.addTestRun(TestRun.createSuccessful(new Date(1)));
		test.addTestRun(TestRun.createFailed(new Date(2), failure));
		test.addTestRun(TestRun.createFailed(new Date(3), failure));
		test.addTestRun(TestRun.createFailed(new Date(4), failure));
		test.addTestRun(TestRun.createSuccessful(new Date(5)));
		test.addTestRun(TestRun.createSuccessful(new Date(6)));

		assertEquals(0.5f, test.getFailurePercentage(20), 0.001f);
	}

	@Test
	public void failurePercentageTestTooLowDepth() {
		test.addTestRun(TestRun.createSuccessful(new Date(1)));
		test.addTestRun(TestRun.createFailed(new Date(2), failure));
		test.addTestRun(TestRun.createFailed(new Date(3), failure));
		test.addTestRun(TestRun.createFailed(new Date(4), failure));
		test.addTestRun(TestRun.createSuccessful(new Date(5)));
		test.addTestRun(TestRun.createSuccessful(new Date(6)));
		test.addTestRun(TestRun.createFailed(new Date(7), failure));

		assertEquals(0f, test.getFailurePercentage(-1), 0.001f);
	}
}
