package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
public class ChangedCodeFirst extends SingleTestSortingPolicy {

	/**
	 * Creates a new changed code first policy.
	 */
	public ChangedCodeFirst() {
		
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
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		int nb = tests.size();
		Tuple[] tuples = new Tuple[nb];
		int i=0;
		for (Test test : tests) {
			long optimal = Long.MIN_VALUE;
			Test current = test;
			for(TestedMethod t : testDatabase.getLinkedMethods(current)) {
				long temp = t.getLastChange().getTime();
				if (temp > optimal) {
					optimal = temp;
				}
			}
			tuples[i] = new Tuple(current, optimal);
			i++;
		}
		Arrays.sort(tuples);
		List<Test> result = new ArrayList<Test>();
		for (i=0; i<nb; i++) {
			result.add(tuples[i].test);
		}
		return result;
	}
}
