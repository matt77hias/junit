package kuleuven.group2.policy;

import java.util.Date;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;

import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * A class of tests for policies.
 * 
 * @author 	Group 2
 * @version	21 November 2013
 */
public abstract class TestSortingPolicyTest {
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
		
		// Add tests
		testDatabase.addTest(test1);
		testDatabase.addTest(test2);
		testDatabase.addTest(test3);
		testDatabase.addTest(test4);

		// Add test runs
		testDatabase.addTestRun(TestRun.createFailed(new Date(1), failure1), test1);
		testDatabase.addTestRun(TestRun.createFailed(new Date(1), failure1), test1);
		testDatabase.addTestRun(TestRun.createFailed(new Date(2), failure2), test1);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(3)), test1);
		testDatabase.addTestRun(TestRun.createFailed(new Date(4), failure3), test1);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(5)), test1);

		testDatabase.addTestRun(TestRun.createFailed(new Date(6), failure5), test2);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(7)), test2);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(8)), test2);
		testDatabase.addTestRun(TestRun.createFailed(new Date(9), failure6), test2);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(10)), test2);

		testDatabase.addTestRun(TestRun.createFailed(new Date(11), failure6), test3);
		testDatabase.addTestRun(TestRun.createFailed(new Date(12), failure5), test3);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(13)), test3);
		testDatabase.addTestRun(TestRun.createFailed(new Date(14), failure6), test3);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(15)), test3);
		testDatabase.addTestRun(TestRun.createFailed(new Date(16), failure5), test3);

		testDatabase.addTestRun(TestRun.createSuccessful(new Date(17)), test4);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(18)), test4);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(19)), test4);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(20)), test4);
		testDatabase.addTestRun(TestRun.createSuccessful(new Date(21)), test4);
	}
	
	public abstract void correct_order_test();
	
	public abstract void immutable_input_test();
}
