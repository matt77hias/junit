package kuleuven.group2.data;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDatabaseTest {
	
	protected final static TestedMethod TESTED_METHOD = new TestedMethod("test/Test.test()V");
	protected final static kuleuven.group2.data.Test TEST = new kuleuven.group2.data.Test("testClass", "testMethod");
	
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

}
