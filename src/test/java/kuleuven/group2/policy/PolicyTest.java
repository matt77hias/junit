package kuleuven.group2.policy;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * A class of tests for policies.
 * 
 * @author 	Group 2
 * @version	21 November 2013
 */
public class PolicyTest {
	protected TestDatabase testDatabase;
	protected kuleuven.group2.data.Test test1;
	protected kuleuven.group2.data.Test test2;
	protected kuleuven.group2.data.Test test3;
	protected kuleuven.group2.data.Test test4;
	
	@SuppressWarnings("unused")
	private class A {
		public void mA1() {
		}
		public void mA2() {
		}
	}
	
	@SuppressWarnings("unused")
	private class B {
		public void mB1() {
		}
		public void mB2() {
		}
	}
	
	@SuppressWarnings("unused")
	private class C {
		public void mC1() {
		}
		public void mC2() {
		}
	}

	@SuppressWarnings("unused")
	@Before
	public void setUp() throws Exception {
		// Create a TestDatabase
		testDatabase = new TestDatabase();
		
		// Create Test records
		test1 = new kuleuven.group2.data.Test("C1", "M1");
		test2 = new kuleuven.group2.data.Test("C2", "M2");
		test3 = new kuleuven.group2.data.Test("C3", "M3");
		test4 = new kuleuven.group2.data.Test("C1", "M4");
		
		// Create StackTraceElements
		StackTraceElement e1 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mA1", null, 1);
		StackTraceElement e2 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mA2", null, 1);
		StackTraceElement e3 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mB1", null, 1);
		StackTraceElement e4 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mB2", null, 1);
		StackTraceElement e5 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mB1", null, 1);
		StackTraceElement e6 = new StackTraceElement("kuleuven.group2.policy.PolicyTest$A", "mB2", null, 1);
		
		// Create Exceptions and add an StackTraceElement array to each of them
		Exception ex1 = new Exception();
		ex1.setStackTrace(new StackTraceElement[] {e1});
		Exception ex2 = new Exception();
		ex2.setStackTrace(new StackTraceElement[] {e2});
		Exception ex3 = new Exception();
		ex3.setStackTrace(new StackTraceElement[] {e3});
		Exception ex4 = new Exception();
		ex4.setStackTrace(new StackTraceElement[] {e4});
		Exception ex5 = new Exception();
		ex5.setStackTrace(new StackTraceElement[] {e5});
		Exception ex6 = new Exception();
		ex6.setStackTrace(new StackTraceElement[] {e6});
		
		// Create Failures and add an Exception to each of them
		Failure failure1 = new Failure(Description.EMPTY, ex1);
		Failure failure2 = new Failure(Description.EMPTY, ex2);
		Failure failure3 = new Failure(Description.EMPTY, ex3);
		Failure failure4 = new Failure(Description.EMPTY, ex4); 
		Failure failure5 = new Failure(Description.EMPTY, ex5);
		Failure failure6 = new Failure(Description.EMPTY, ex6);
		
		// Add test runs
		test1.addTestRun(TestRun.createFailed(new Date(1), failure1));
		test1.addTestRun(TestRun.createFailed(new Date(2), failure2));
		test1.addTestRun(TestRun.createSuccessful(new Date(3)));
		test1.addTestRun(TestRun.createFailed(new Date(4), failure3));
		test1.addTestRun(TestRun.createSuccessful(new Date(5)));
		
		test2.addTestRun(TestRun.createFailed(new Date(6), failure5));
		test2.addTestRun(TestRun.createSuccessful(new Date(7)));
		test2.addTestRun(TestRun.createSuccessful(new Date(8)));
		test2.addTestRun(TestRun.createFailed(new Date(9), failure6));
		test2.addTestRun(TestRun.createSuccessful(new Date(10)));
		
		test3.addTestRun(TestRun.createFailed(new Date(11), failure6));
		test3.addTestRun(TestRun.createFailed(new Date(12), failure5));
		test3.addTestRun(TestRun.createSuccessful(new Date(13)));
		test3.addTestRun(TestRun.createFailed(new Date(14), failure6));
		test3.addTestRun(TestRun.createSuccessful(new Date(15)));
		test3.addTestRun(TestRun.createFailed(new Date(16), failure5));
		
		test4.addTestRun(TestRun.createSuccessful(new Date(17)));
		test4.addTestRun(TestRun.createSuccessful(new Date(18)));
		test4.addTestRun(TestRun.createSuccessful(new Date(19)));
		test4.addTestRun(TestRun.createSuccessful(new Date(20)));
		test4.addTestRun(TestRun.createSuccessful(new Date(21)));
		
		// Use reflection for updating the test database.
		Method method = TestDatabase.class.getDeclaredMethod("addTest", kuleuven.group2.data.Test.class);
		method.setAccessible(true);
		method.invoke(testDatabase, test1);
		method.invoke(testDatabase, test2);
		method.invoke(testDatabase, test3);
		method.invoke(testDatabase, test4);
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void distinctFailureFirstTest() {
		TestSortingPolicy policy = new DistinctFailureFirst();
		kuleuven.group2.data.Test[] result = policy.getSortedTests(testDatabase);
		
		if (result[0] == test1) {
			if (result[1] == test2) {
				assertTrue(result[2] == test3);
				assertTrue(result[3] == test4);
			} else if (result[1] == test3) {
				assertTrue(result[2] == test2);
				assertTrue(result[3] == test4);
			} else {
				fail();
			}
		} else if (result[0] == test2) {
			assertTrue(result[1] == test1);
			assertTrue(result[2] == test3);
			assertTrue(result[3] == test4);
		} else if (result[0] == test3) {
			assertTrue(result[1] == test1);
			assertTrue(result[2] == test2);
			assertTrue(result[3] == test4);
		} else {
			fail();
		}
		
		result = policy.getSortedTests(testDatabase, new kuleuven.group2.data.Test[] {test2, test3, test4});
		
		if (result[0] == test2) {
			assertTrue(result[1] == test3);
			assertTrue(result[2] == test4);
		} else if (result[0] == test3) {
			assertTrue(result[1] == test2);
			assertTrue(result[2] == test4);
		} else {
			fail();
		}
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		result = policy.getSortedTests(testDatabase, list);
		
		if (result[0] == test2) {
			assertTrue(result[1] == test3);
			assertTrue(result[2] == test4);
		} else if (result[0] == test3) {
			assertTrue(result[1] == test2);
			assertTrue(result[2] == test4);
		} else {
			fail();
		}
	}
	
	@Test
	public void frequentFailureFirstTest() {
		TestSortingPolicy policy = new FrequentFailureFirst();
		kuleuven.group2.data.Test[] result = policy.getSortedTests(testDatabase);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test1);
		assertTrue(result[2] == test2);
		assertTrue(result[3] == test4);
		
		result = policy.getSortedTests(testDatabase, new kuleuven.group2.data.Test[] {test2, test3, test4});
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		result = policy.getSortedTests(testDatabase, list);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
	}
	
	@Test
	public void lastFailureFirstTest() {
		TestSortingPolicy policy = new LastFailureFirst();
		kuleuven.group2.data.Test[] result = policy.getSortedTests(testDatabase);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test1);
		assertTrue(result[3] == test4);
		
		result = policy.getSortedTests(testDatabase, new kuleuven.group2.data.Test[] {test2, test3, test4});
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		result = policy.getSortedTests(testDatabase, list);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
	}
	
	@Test
	public void distinctFailureFirstTest_immutable_input() {
		TestSortingPolicy policy = new DistinctFailureFirst();
		
		kuleuven.group2.data.Test[] input = new kuleuven.group2.data.Test[] {test2, test3, test4};
		policy.getSortedTests(testDatabase, input);
		assertTrue(input[0] == test2);
		assertTrue(input[1] == test3);
		assertTrue(input[2] == test4);
		assertEquals(input.length, 3);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		policy.getSortedTests(testDatabase, list);
		assertTrue(list.get(0) == test2);
		assertTrue(list.get(1) == test3);
		assertTrue(list.get(2) == test4);
		assertEquals(list.size(), 3);
	}
	
	@Test
	public void frequentFailureFirstTest_immutable_input() {
		TestSortingPolicy policy = new FrequentFailureFirst();
		
		kuleuven.group2.data.Test[] input = new kuleuven.group2.data.Test[] {test2, test3, test4};
		policy.getSortedTests(testDatabase, input);
		assertTrue(input[0] == test2);
		assertTrue(input[1] == test3);
		assertTrue(input[2] == test4);
		assertEquals(input.length, 3);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		policy.getSortedTests(testDatabase, list);
		assertTrue(list.get(0) == test2);
		assertTrue(list.get(1) == test3);
		assertTrue(list.get(2) == test4);
		assertEquals(list.size(), 3);
	}
	
	@Test
	public void lastFailureFirstTest_immutable_input() {
		TestSortingPolicy policy = new LastFailureFirst();
		
		kuleuven.group2.data.Test[] input = new kuleuven.group2.data.Test[] {test2, test3, test4};
		policy.getSortedTests(testDatabase, input);
		assertTrue(input[0] == test2);
		assertTrue(input[1] == test3);
		assertTrue(input[2] == test4);
		assertEquals(input.length, 3);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		policy.getSortedTests(testDatabase, list);
		assertTrue(list.get(0) == test2);
		assertTrue(list.get(1) == test3);
		assertTrue(list.get(2) == test4);
		assertEquals(list.size(), 3);
	}
}
