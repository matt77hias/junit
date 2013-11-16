package kuleuven.group2.data;

import static org.junit.Assert.*;

import java.util.Date;

import kuleuven.group2.data.testrun.FailedTestRun;
import kuleuven.group2.data.testrun.SuccesfullTestRun;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTest {
	
	protected kuleuven.group2.data.Test test;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		test = new kuleuven.group2.data.Test("testClass", "testMethod");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void lastFailureTimeTest() {
		test.addTestRun(new FailedTestRun(new Date(0)));
		test.addTestRun(new FailedTestRun(new Date(1)));
		test.addTestRun(new FailedTestRun(new Date(2)));
		test.addTestRun(new SuccesfullTestRun(new Date(3)));
		
		assertEquals(new Date(2), test.getLastFailureTime());
	}

	@Test
	public void lastFailureTimeTestNoFailures() {
		test.addTestRun(new SuccesfullTestRun(new Date(3)));
		
		assertEquals(new Date(0), test.getLastFailureTime());
	}

	@Test
	public void lastFailureTimeTestNoTestRuns() {		
		assertEquals(new Date(0), test.getLastFailureTime());
	}

}
