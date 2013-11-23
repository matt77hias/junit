package kuleuven.group2.policy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

/**
 * A class representing the last failure first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public class LastFailureFirst implements TestSortingPolicy, Comparator<Test> {
	
	/**
	 * Creates a new last failure first policy.
	 */
	public LastFailureFirst() {
		
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
	 * Sorts the given tests according to this last failure first policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @return	The tests of the given test database according to this policy.
	 */
	@Override
	public Test[] getSortedTestsAccordingToPolicy(TestDatabase testDatabase, Test[] tests) {
		Test[] results = tests.clone();
		Arrays.sort(results, 0, results.length, this);
		return results;
	}
	
	/**
	 * Sorts the given tests according to this last failure first policy.
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

	/**
	 * Compares the two given tests according to the last failure first policy.
	 * 
	 * @pre		The given Test objects may not refer the null reference.
	 * @param	o1
	 * 			The first test.
	 * @param	o2
	 * 			The second test.
	 * @return	The result is less than zero if the last failure of o1 happened
	 * 			more recently than the last failure of o2.
	 * @return	The result is equal to zero if the last failure of o1 happened
	 * 			at the same time as the last failure of o2. This will be very
	 * 			rare if o1 != o2.
	 * @return	The result is greater than zero if the last failure of o2 happened
	 * 			more recently than the last failure of o2.
	 */
	@Override
	public int compare(Test o1, Test o2) {
		// o2.getLastFailureTime() < o1.getLastFailureTime() => result < 0 
		// o2.getLastFailureTime() = o1.getLastFailureTime() => result == 0
		// o1.getLastFailureTime() < o2.getLastFailureTime() => result > 0
		return o2.getLastFailureTime().compareTo(o1.getLastFailureTime());
	}
}
