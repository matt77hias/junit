package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRun;
import kuleuven.group2.util.ArrayUtils;

/**
 * A class representing the distinct failure first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public abstract class DistinctFailureFirstPolicy implements Policy {
	
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
	public DistinctFailureFirstPolicy() {
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
	public DistinctFailureFirstPolicy(int depth) {
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
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase) {
		Test[] result = testDatabase.getAllTests().toArray(new Test[0]);
		getSortedTestsAccordingToPolicy(testDatabase, result);
		return result;
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @post	The given array may be modified.
	 * @return	The tests of the given test database according to this policy.
	 */
	@Override
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase, Test[] tests) {
		Set<StackTraceElement> currentTraceElements = new HashSet<StackTraceElement>();
		List<Test> priority = new ArrayList<Test>();
		List<Test> postponed = new ArrayList<Test>();
		
		int nb = tests.length;
		for (int i=0; i<nb; i++) {
			Test current = tests[i];
			
			int d = 0;
			for (TestRun testRun : current.getTestRuns()) {
				
				if (d == getDepth()) {
					break;
				}
				
				if (testRun.isFailedRun()) {
					StackTraceElement e = testRun.getTrace()[0];
					if (!currentTraceElements.contains(e)) {
						currentTraceElements.add(e);
						if (!priority.contains(current)) {
							priority.add(current);
						}
					}
				}
				
				d++;
			}
			
			if (!priority.contains(current) && !postponed.contains(current)) {
				postponed.add(current);
			}
		}
		
		return ArrayUtils.concat(priority.toArray(new Test[0]), postponed.toArray(new Test[0]));
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @post	The given collection may be modified.
	 * @return	The tests of the given test database according to this policy.
	 */
	@Override
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase, List<Test> tests) {
		Test[] result = tests.toArray(new Test[0]);
		return getSortedTestsAccordingToPolicy(testDatabase, result);
	}
}
