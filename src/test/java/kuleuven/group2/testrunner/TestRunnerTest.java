package kuleuven.group2.testrunner;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class TestRunnerTest {
	
	protected TestRunner testRunner;
	protected kuleuven.group2.data.Test testMethod2Arg;
	protected kuleuven.group2.data.Test testMethodFail;
	protected kuleuven.group2.data.Test testMethodNonExisting;
	protected JUnitCore junitCore;
	
	protected boolean listenerVisited;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		junitCore = new JUnitCore();
		
		testRunner = new TestRunner(getClass().getClassLoader(), junitCore);
		
		testMethod2Arg = new kuleuven.group2.data.Test(
				kuleuven.group2.data.signature.JavaSignatureParserTest.class.getName(),
				"testMethod2Arg"
				);
		
		testMethodFail = new kuleuven.group2.data.Test(
				kuleuven.group2.testrunner.FailTest.class.getName(),
				"fail"
				);
		
		testMethodNonExisting = new kuleuven.group2.data.Test(
				"nonExistingClass",
				"nonExistingMethod"
				);
		
		listenerVisited = false;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSucceededSimple() throws Exception {
		List<Result> result = testRunner.runTestMethods(testMethod2Arg);
		Result testMethod2ArgResult = result.get(0);
		
		assertTrue(testMethod2ArgResult.wasSuccessful());
		assertEquals(1, testMethod2ArgResult.getRunCount());
	}
	
    @Test
	public void testFailedSimple() throws Exception {
		List<Result> result = testRunner.runTestMethods(testMethodFail);
		Result testMethodFailResult = result.get(0);
		
		assertFalse(testMethodFailResult.wasSuccessful());
		assertEquals(1, testMethodFailResult.getRunCount());
		assertEquals(1, testMethodFailResult.getFailureCount());
	}
	
    @Test
	public void testFailedClassNotFound() throws Exception {
		List<Result> result = testRunner.runTestMethods(testMethodNonExisting);
		
		assertNull(result.get(0));
	}
    
    @Test
	public void testMultiple() throws Exception {
		List<Result> result = testRunner.runTestMethods(testMethodNonExisting, testMethod2Arg, testMethodFail);
		Result testMethodResult1 = result.get(0);
		Result testMethodResult2 = result.get(1);
		Result testMethodResult3 = result.get(2);
	
		assertNull(testMethodResult1);

		assertTrue(testMethodResult2.wasSuccessful());
		assertEquals(1, testMethodResult2.getRunCount());
		
		assertFalse(testMethodResult3.wasSuccessful());
		assertEquals(1, testMethodResult3.getRunCount());
		assertEquals(1, testMethodResult3.getFailureCount());
	}
   
	@Test
	public void testRunListener() throws Exception {
		testRunner.addRunListener(new RunListener() {
			@Override
			public void testStarted(Description description) throws Exception {
				TestRunnerTest.this.listenerVisited();
		    }
		});

		testRunner.runTestMethods(testMethod2Arg);
		
		assertTrue(listenerVisited);
	}
	
	public void listenerVisited() {
		listenerVisited = true;
	}

}
