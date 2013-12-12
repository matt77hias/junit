package kuleuven.group2.data;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;

import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDatabaseTest {

	protected final static String TESTED_METHOD_CLASS_NAME_SIGNATURE = "test/Test";
	protected final static String TESTED_METHOD_CLASS_NAME = "test.Test";
	
	protected final static String TESTED_METHOD_SIGNATURE_STRING
		=  TESTED_METHOD_CLASS_NAME_SIGNATURE + ".test()V";
	protected final static JavaSignature TESTED_METHOD_SIGNATURE
		= new JavaSignatureParser(TESTED_METHOD_SIGNATURE_STRING).parseSignature();
	protected final static TestedMethod TESTED_METHOD = new TestedMethod(TESTED_METHOD_SIGNATURE_STRING);

	protected static kuleuven.group2.data.Test TEST;
	protected static kuleuven.group2.data.Test TEST2;

	protected static TestRun TEST_RUN_1_1;
	protected static TestRun TEST_RUN_1_2;
	
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
		/*
		 * Resetting the Test variables is necessary because the test runs will be added to these
		 * And the list of test runs must be reset between each test
		 */
		TEST = new kuleuven.group2.data.Test("testClass", "testMethod");
		TEST2 = new kuleuven.group2.data.Test("testClass", "testMethod2");
		
		TEST_RUN_1_1 = TestRun.createSuccessful(TEST, new Date(0));
		TEST_RUN_1_2 = TestRun.createSuccessful(TEST2, new Date(1));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEmptyMethodTestLinksContainsMethodTestLink() {
		assertFalse(testDatabase.containsMethodTestLink(TESTED_METHOD, TEST));
	}

	@Test
	public void testEmptyMethodTestLinksGetLinkedMethods() {
		assertEquals(0, testDatabase.getLinkedMethods(TEST).size());
	}

	@Test
	public void testEmptyMethodTestLinksGetLinkedTests() {
		assertEquals(0, testDatabase.getLinkedTests(TESTED_METHOD).size());
	}

	@Test
	public void testEmptyDbContainsMethod() {
		assertFalse(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
	}
	
	@Test
	public void testAddMethod() {
		testDatabase.addMethod(TESTED_METHOD);
		
		assertTrue(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
	}
	
	@Test
	public void testAddMethodDouble() {
		testDatabase.addMethod(TESTED_METHOD);
		testDatabase.addMethod(TESTED_METHOD);
		
		assertTrue(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
		assertEquals(1, testDatabase.methods.size());
	}

	@Test
	public void testRemoveMethod() {
		testDatabase.addMethod(TESTED_METHOD);
		
		testDatabase.removeMethod(TESTED_METHOD);
		
		assertFalse(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
	}

	@Test
	public void testRemoveMethodNotInDb() {
		testDatabase.removeMethod(TESTED_METHOD);
		
		assertFalse(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
	}
	
	@Test
	public void testGetMethod() {
		testDatabase.addMethod(TESTED_METHOD);
		
		TestedMethod testedMethod = testDatabase.getMethod(TESTED_METHOD_SIGNATURE);
		
		assertEquals(testedMethod, TESTED_METHOD);
	}
	
	@Test
	public void testGetMethodNotInDb() {
		TestedMethod testedMethod = testDatabase.getMethod(TESTED_METHOD_SIGNATURE);
		
		assertNull(testedMethod);
	}
	
	@Test
	public void testGetOrCreateMethod() {
		TestedMethod testedMethod = testDatabase.getOrCreateMethod(TESTED_METHOD_SIGNATURE);

		assertEquals(testedMethod, TESTED_METHOD);
		assertTrue(testDatabase.containsMethod(TESTED_METHOD_SIGNATURE));
	}
	
	@Test
	public void testGetOrCreateMethodAlreadyInDb() {
		testDatabase.addMethod(TESTED_METHOD);
		
		TestedMethod testedMethod = testDatabase.getOrCreateMethod(TESTED_METHOD_SIGNATURE);

		assertTrue(testedMethod == TESTED_METHOD);
	}
	
	@Test
	public void testGetMethodsIn() {
		testDatabase.addMethod(TESTED_METHOD);
		
		Collection<TestedMethod> testedMethods = testDatabase.getMethodsIn(TESTED_METHOD_CLASS_NAME);
		
		assertEquals(1, testedMethods.size());
	}
	
	@Test
	public void testGetMethodsInTestedMethodNotInDb() {
		Collection<TestedMethod> testedMethods = testDatabase.getMethodsIn(TESTED_METHOD_CLASS_NAME);
		
		assertEquals(0, testedMethods.size());
	}
	
	@Test
	public void testAddTest() {
		testDatabase.addTest(TEST);
		
		assertTrue(testDatabase.containsTest(TEST.getTestClassName(), TEST.getTestMethodName()));
	}
	
	@Test
	public void testAddTestDouble() {
		testDatabase.addTest(TEST);
		testDatabase.addTest(TEST);
		
		Collection<kuleuven.group2.data.Test> allTests = testDatabase.getAllTests();
		
		assertTrue(testDatabase.containsTest(TEST.getTestClassName(), TEST.getTestMethodName()));
		assertEquals(1, allTests.size());
	}
	
	@Test
	public void testRemoveTest() {
		testDatabase.addTest(TEST);
		
		testDatabase.removeTest(TEST);
		
		assertFalse(testDatabase.containsTest(TEST.getTestClassName(), TEST.getTestMethodName()));
	}
	
	@Test
	public void testGetTest() {
		testDatabase.addTest(TEST);
		
		kuleuven.group2.data.Test test = testDatabase.getTest(TEST.getTestClassName(), TEST.getTestMethodName());
		
		assertTrue(test == TEST);
	}
	
	@Test
	public void testGetTestNotInDb() {
		kuleuven.group2.data.Test test = testDatabase.getTest(TEST.getTestClassName(), TEST.getTestMethodName());
		
		assertNull(test);
	}
	
	@Test
	public void testGetOrCreateTest() {
		testDatabase.addTest(TEST);
		
		kuleuven.group2.data.Test test = testDatabase.getOrCreateTest(TEST.getTestClassName(), TEST.getTestMethodName());
		
		assertTrue(test == TEST);
	}
	
	@Test
	public void testGetOrCreateTestNotInDb() {
		kuleuven.group2.data.Test test = testDatabase.getOrCreateTest(TEST.getTestClassName(), TEST.getTestMethodName());
		
		assertEquals(TEST, test);
	}
	
	@Test
	public void testGetAllTests() {
		testDatabase.addTest(TEST);
		testDatabase.addTest(TEST2);
		
		Collection<kuleuven.group2.data.Test> allTests = testDatabase.getAllTests();
		
		assertTrue(allTests.contains(TEST));
		assertTrue(allTests.contains(TEST2));
	}
	
	@Test
	public void getTestsIn() {
		testDatabase.addTest(TEST);
		testDatabase.addTest(TEST2);
		
		Collection<kuleuven.group2.data.Test> allTests
			= testDatabase.getTestsIn(TEST.getTestClassName());
		
		assertEquals(2, allTests.size());
	}
	
	@Test
	public void getTestsInNotInDb() {
		Collection<kuleuven.group2.data.Test> allTests
			= testDatabase.getTestsIn(TEST.getTestClassName());
		
		assertEquals(0, allTests.size());
	}

	@Test
	public void testAddTestRun() {
		testDatabase.addTest(TEST);
		testDatabase.addTest(TEST2);
		
		TestBatch testBatch = testDatabase.createTestBatch(new Date(0));
		
		testDatabase.addTestRun(TEST_RUN_1_1, testBatch);
		
		testDatabase.finishTestBatch(testBatch, new Date(5));

		assertEquals(1, testDatabase.getAllTestRuns().size());
	}
	
	@Test
	public void testAddTestRun2() {
		testDatabase.addTest(TEST);
		testDatabase.addTest(TEST2);
		
		TestBatch testBatch = testDatabase.createTestBatch(new Date(0));
		
		testDatabase.addTestRun(TEST_RUN_1_1, testBatch);
		testDatabase.addTestRun(TEST_RUN_1_2, testBatch);
		
		testDatabase.finishTestBatch(testBatch, new Date(5));

		assertEquals(2, testDatabase.getAllTestRuns().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddTestRunTestNotInDb() {
		TestBatch testBatch = testDatabase.createTestBatch(new Date(0));
		
		testDatabase.addTestRun(TEST_RUN_1_1, testBatch);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddTestRunTestBatchNotInDb() {
		testDatabase.addTest(TEST);
		
		testDatabase.addTestRun(TEST_RUN_1_1, new TestBatch(new Date(0)));
	}
	
	@Test
	public void testAddLinkSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);

		assertTrue(testDatabase.containsMethodTestLink(TESTED_METHOD, TEST));
	}

	@Test
	public void testClearLinksSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);

		testDatabase.clearMethodTestLinks();

		assertFalse(testDatabase.containsMethodTestLink(TESTED_METHOD, TEST));
	}

	@Test
	public void testGetLinkedMethodsSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);

		Collection<TestedMethod> linkedMethods = testDatabase.getLinkedMethods(TEST);

		assertTrue(linkedMethods.contains(TESTED_METHOD));
		assertEquals(1, linkedMethods.size());
	}

	@Test
	public void testGetLinkedTestsSimple() {
		testDatabase.addMethodTestLink(TESTED_METHOD, TEST);

		Collection<kuleuven.group2.data.Test> linkedTests = testDatabase.getLinkedTests(TESTED_METHOD);

		assertTrue(linkedTests.contains(TEST));
		assertEquals(1, linkedTests.size());
	}

}
