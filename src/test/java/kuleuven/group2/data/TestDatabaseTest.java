package kuleuven.group2.data;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import kuleuven.group2.data.testrun.FailedTestRun;
import kuleuven.group2.data.testrun.TestRun;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDatabaseTest {
	
	protected final static TestedMethod TESTED_METHOD = new TestedMethod("test/Test.test()V");
	
	protected final static kuleuven.group2.data.Test TEST = new kuleuven.group2.data.Test("testClass", "testMethod");
	protected final static kuleuven.group2.data.Test TEST2 = new kuleuven.group2.data.Test("testClass", "testMethod2");
	
	protected TestDatabase testDatabase;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testDatabase = new TestDatabase();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addLinkTestSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);
		
		assertTrue(testDatabase.containsMethodTestLink(TESTED_METHOD, TEST));
	}
	
	@Test
	public void clearLinksTestSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);
		
		testDatabase.clearMethodLinks();
		
		assertFalse(testDatabase.containsMethodTestLink(TESTED_METHOD, TEST));
	}
	
	@Test
	public void getLinkedMethodsTestSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);
		
		Collection<TestedMethod> linkedMethods = testDatabase.getLinkedMethods(TEST);
		
		assertTrue(linkedMethods.contains(TESTED_METHOD));
		assertEquals(1, linkedMethods.size());
	}
	
	@Test
	public void getLinkedTestsTestSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);
		
		Collection<kuleuven.group2.data.Test> linkedTests = testDatabase.getLinkedTests(TESTED_METHOD);
		
		assertTrue(linkedTests.contains(TEST));
		assertEquals(1, linkedTests.size());
	}

	@Test
	public void lastFailedTestsTestSimple() {
		TestRun run1 = new FailedTestRun(new Date(1));
		testDatabase.addTestRun(run1, TEST.getTestClassName(), TEST.getTestMethodName());
		TestRun run2 = new FailedTestRun(new Date(2));
		testDatabase.addTestRun(run2, TEST2.getTestClassName(), TEST2.getTestMethodName());
		TestRun run3 = new FailedTestRun(new Date(3));
		testDatabase.addTestRun(run3, TEST.getTestClassName(), TEST.getTestMethodName());
		
		List<kuleuven.group2.data.Test> lastFailedTests = testDatabase.getLastFailedTests();
		
		assertEquals(TEST, lastFailedTests.get(0));
		assertEquals(TEST2, lastFailedTests.get(1));
	}
	
}
