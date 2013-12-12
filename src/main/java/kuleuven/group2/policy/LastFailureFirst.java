package kuleuven.group2.policy;

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
public class LastFailureFirst extends ComparingPolicy implements Comparator<Test> {
	
	/**
	 * Creates a new last failure first policy.
	 */
	public LastFailureFirst() {
		
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

	@Override
	protected Comparator<? super Test> getComparator(TestDatabase testDatabase) {
		return this;
	}

}
