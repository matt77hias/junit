package kuleuven.group2.policy;

import java.util.Arrays;
import java.util.Collection;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;

/**
 * A class representing the changed code first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public class ChangedCodeFirst implements TestSortingPolicy {

	/**
	 * Creates a new changed code first policy.
	 */
	public ChangedCodeFirst() {
		
	}
	
	/**
	 * Sorts the tests of the given test database according to this last failure policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @return	The tests of the given test database according to this
	 * 			last failure policy.
	 */
	@Override
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase) {
		Test[] result = testDatabase.getAllTests().toArray(new Test[0]);
		return getSortedTestsAccordingToPolicy(testDatabase, result);
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
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase, Test[] tests) {
		int nb = tests.length;
		Tuple[] tuples = new Tuple[nb];
		for (int i=0; i<nb; i++) {
			long optimal = Long.MIN_VALUE;
			Test current = tests[i];
			for(TestedMethod t : testDatabase.getLinkedMethods(current)) {
				long temp = t.getLastChange().getTime();
				if (temp > optimal) {
					optimal = temp;
				}
			}
			tuples[i] = new Tuple(current, optimal);
		}
		Arrays.sort(tuples);
		Test[] result = new Test[nb];
		for (int i=0; i<nb; i++) {
			result[i] = tuples[i].test;
		}
		return result;
	}
	
	/**
	 * A class of tuples.
	 * 
	 * @author 	Group 2
	 * @version	18 November 2013
	 */
	private class Tuple implements Comparable<Tuple> {
		
		/**
		 * The test of this tuple.
		 */
		private Test test;
		
		/**
		 * The time in milliseconds of the last change of methods
		 * used in the test of this tuple.
		 */
		private long time;
		
		/**
		 * Creates a new tuple with given test and time.
		 * 
		 * @param	test
		 * 			The test for this new tuple.
		 * @param	time
		 * 			The time for this new tuple.
		 */
		public Tuple(Test test, long time) {
			this.test = test;
			this.time = time;
		}

		/**
		 * Compares the times of the given tuple to this
		 * tuple's time.
		 * 
		 * @param	t2
		 * 			The other tuple.
		 * @return	Returns a number less than zero
		 * 			if the time of this tuple is greater
		 * 			than the time of the given tuple.
		 * @return	Returns zero if the time of this
		 * 			tuple and the given tuple are equal.
		 * @return	Returns a number greater than zero
		 * 			if the time of the given tuple is greater
		 * 			than the time of this tuple.
		 */
		@Override
		public int compareTo(Tuple t2) {
			return Long.compare(t2.time, this.time);
		}
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
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase, Collection<Test> tests) {
		return getSortedTestsAccordingToPolicy(testDatabase, tests.toArray(new Test[0]));
	}
}
