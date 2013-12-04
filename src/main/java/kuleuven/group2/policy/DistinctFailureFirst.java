package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;
import kuleuven.group2.util.ArrayUtils;

import com.google.common.collect.Iterables;

/**
 * A class representing the distinct failure first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public class DistinctFailureFirst implements TestSortingPolicy {
	
	/**
	 * The default depth of the level of history that's
	 * still taken into account for distinct failure
	 * first policies. 
	 * 
	 * The history of test runs before the 'depth' last test
	 * runs are not taken into account.
	 * 
	 * @note	The level of history refers only
	 * 			to the history for the test runs
	 * 			assosciated with a single test.
	 * 			Not the history of all test runs.
	 */
	public static final int DEFAULT_DEPTH = 20;
	
	/**
	 * Creates a new distinct failure first policy
	 * with default depth.
	 */
	public DistinctFailureFirst() {
		this(DEFAULT_DEPTH);
	}
	
	/**
	 * Creates a new distinct failure first policy
	 * with given depth.
	 * 
	 * @param	depth
	 * 			The depth of the level of history that's
	 * 			still taken into account. The history of
	 * 			test runs before the 'depth' last test runs
	 * 			are not taken into account.
	 */
	public DistinctFailureFirst(int depth) {
		setDepth(depth);
	}
	
	/**
	 * Returns the depth of the level of history that's
	 * still taken into account of this distinct failure
	 * first policy. 
	 * 
	 * @return 	The depth of the level of history that's
	 * 			still taken into account of this distinct failure
	 * 			first policy. 
	 */
	public int getDepth() {
		return this.depth;
	}
	
	/**
	 * Sets the depth of the level of history that's
	 * still taken into account of this distinct failure
	 * first policy to the given depth.
	 * 
	 * @param	depth
	 * 			The depth.
	 */
	public void setDepth(int depth) {
		this.depth = Math.abs(depth);
	}
	
	/**
	 * The depth of the level of history that's
	 * still taken into account of this distinct failure
	 * first policy. 
	 * 
	 * The history of test runs before the 'depth' last test
	 * runs are not taken into account.
	 */
	private int depth;

	/**
	 * Sorts the tests of the given test database according to this distinct failure first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @return	The tests of the given test database according to this
	 * 			distinct failure first policy.
	 */
	@Override
	public Test[] getSortedTests(TestDatabase testDatabase) {
		Test[] result = testDatabase.getAllTests().toArray(new Test[0]);
		return getSortedTests(testDatabase, result);
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @return	The tests of the given test database according to this policy.
	 */
	@Override
	public Test[] getSortedTests(TestDatabase testDatabase, Test[] tests) {
		Set<StackTraceElement> distinctCauses = new HashSet<StackTraceElement>();
		List<Test> priority = new ArrayList<Test>();
		List<Test> postponed = new ArrayList<Test>();

		for (Test test : tests) {
			boolean isPrioritized = false;
			// Select at most depth test runs
			Iterable<TestRun> limitedTestRuns = Iterables.limit(test.getTestRuns(), getDepth());
			for (TestRun testRun : limitedTestRuns) {
				if (testRun.isFailedRun()) {
					// Find cause of failure in tested code
					StackTraceElement element = findTestedFailureCause(testDatabase, testRun.getTrace());
					if (element != null && !distinctCauses.contains(element)) {
						// New distinct failure cause
						// Prioritize this test
						distinctCauses.add(element);
						if (!isPrioritized) {
							priority.add(test);
							isPrioritized = true;
						}
					}
				}
			}
			// Postpone if not prioritized
			if (!isPrioritized) {
				postponed.add(test);
			}
		}

		return ArrayUtils.concat(priority.toArray(new Test[0]), postponed.toArray(new Test[0]));
	}

	/**
	 * Find the cause of a failure in the tested code. That is, find the first
	 * stack trace element occurring inside a tested class.
	 * 
	 * @param testDatabase
	 *            The test database.
	 * @param trace
	 *            The stack trace.
	 * @return The first element inside a tested class.
	 */
	protected StackTraceElement findTestedFailureCause(TestDatabase testDatabase, StackTraceElement[] trace) {
		for (StackTraceElement element : trace) {
			if (!testDatabase.getMethodsIn(element.getClassName()).isEmpty()) {
				return element;
			}
		}
		return null;
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @return	The tests of the given test database according to this policy.
	 */
	@Override
	public Test[] getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		return getSortedTests(testDatabase, tests.toArray(new Test[0]));
	}
}
