package kuleuven.group2.policy;

import java.util.Comparator;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

/**
 * A class representing the frequent failure first policy.
 * 
 * @author	Group 2
 * @version	17 November 2013
 * 
 */
public class FrequentFailureFirst extends ComparingTestSortingPolicy implements Comparator<Test> {
	
	/**
	 * The default depth of the level of history that's
	 * still taken into account for frequent failure
	 * first policies. 
	 * 
	 * The history of test runs before the 'depth' last test
	 * runs are not taken into account.
	 */
	public static final int DEFAULT_DEPTH = 20;
	
	/**
	 * Creates a new frequent failure first policy
	 * with default depth.
	 */
	public FrequentFailureFirst() {
		this(DEFAULT_DEPTH);
	}
	
	/**
	 * Creates a new frequent failure first policy
	 * with given depth.
	 * 
	 * @param	depth
	 * 			The depth of the level of history that's
	 * 			still taken into account. The history of
	 * 			test runs before the 'depth' last test runs
	 * 			are not taken into account.
	 */
	public FrequentFailureFirst(int depth) {
		setDepth(depth);
	}
	
	/**
	 * Returns the depth of the level of history that's
	 * still taken into account of this frequent failure
	 * first policy. 
	 * 
	 * @return 	The depth of the level of history that's
	 * 			still taken into account of this frequent failure
	 * 			first policy. 
	 */
	public int getDepth() {
		return this.depth;
	}
	
	/**
	 * Sets the depth of the level of history that's
	 * still taken into account of this frequent failure
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
	 * still taken into account of this frequent failure
	 * first policy. 
	 * 
	 * The history of test runs before the 'depth' last test
	 * runs are not taken into account.
	 */
	private int depth;

	/**
	 * Compares the two given tests according to the frequent failure first policy.
	 * 
	 * @pre		The given Test objects may not refer the null reference.
	 * @param	o1
	 * 			The first test.
	 * @param	o2
	 * 			The second test.
	 * @return	The result is less than zero if o1 failed 
	 * 			more frequently than o2.
	 * @return	The result is equal to zero if o1 and o2 have
	 * 			the same frequency of failure.
	 * @return	The result is greater than zero if o2 failed
	 * 			more frequently than o2.
	 */
	@Override
	public int compare(Test o1, Test o2) {
		// If mathematics is our concern we can write our own floating point value compare method.
		return Float.compare(o2.getFailurePercentage(getDepth()), o1.getFailurePercentage(getDepth()));
	}

	@Override
	protected Comparator<? super Test> getComparator(TestDatabase testDatabase) {
		return this;
	}

}
