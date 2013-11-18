package kuleuven.group2.policy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kuleuven.group2.data.Test;

/**
 * A class representing the changed code first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public class ChangedCodeFirst implements Policy {

	/**
	 * Creates a new changed code first policy.
	 */
	public ChangedCodeFirst() {
		
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 */
	@Override
	public void getSortedTestAccordingToPolicy(Test[] tests) {
		Arrays.sort(tests, 0, tests.length, this);
	}
	
	/**
	 * Sorts the given tests according to this changed code first policy.
	 * 
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 */
	@Override
	public void getSortedTestAccordingToPolicy(List<Test> tests) {
		Collections.sort(tests, this);
	}

	/**
	 * Compares the two given tests according to the changed code first policy.
	 * 
	 * @pre		The given Test objects may not refer the null reference.
	 * @param	o1
	 * 			The first test.
	 * @param	o2
	 * 			The second test.
	 * @return	The result is less than zero if the o1 tests more
	 * 			recently changed code than the o2.
	 * @return	The result is equal to zero if both o1 and o2 test
	 * 			code changed at the same time.
	 * @return	The result is greater than zero if the o2 tests more
	 * 			recently changed code than the o2.
	 */
	@Override
	public int compare(Test o1, Test o2) {
		// o2.getLastChangeTime() < o1.getLastChangeTime() => result < 0 
		// o2.getLastChangeTime() = o1.getLastChangeTime() => result == 0
		// o1.getLastChangeTime() < o2.getLastChangeTime() => result > 0
		
		//return o2.getLastChangeTime().compareTo(o1.getLastChangeTime());
		
		return 0; //TODO: add method getLastChangeTime() to Test
	}
}
