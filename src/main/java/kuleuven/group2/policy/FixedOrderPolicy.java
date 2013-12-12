package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import com.google.common.collect.Ordering;

/**
 * A test sorting policy which sorts the test in the given order.
 * 
 * @author Group 2
 * @version 12 December 2013
 */
public class FixedOrderPolicy extends ComparingPolicy {

	protected final Ordering<Test> ordering;
	
	/**
	 * Creates a new fixed order policy with all the tests
	 * from the given test database.
	 * 
	 * @param	testDatabase
	 * 			The test database.
	 */
	public FixedOrderPolicy(TestDatabase testDatabase) {
		this(new ArrayList<Test>(testDatabase.getAllTests()));
	}

	/**
	 * Create a new fixed order policy using the given ordered list of tests.
	 * 
	 * <p>
	 * <strong>Only</strong> tests from the provided list may be sorted using
	 * this policy. Any attempt to sort other tests using this policy will
	 * result in a {@link ClassCastException} to be thrown.
	 * </p>
	 * 
	 * @param orderedTests
	 *            The ordered list of tests.
	 */
	public FixedOrderPolicy(List<Test> orderedTests) {
		this.ordering = Ordering.explicit(orderedTests);
	}

	@Override
	protected Comparator<? super Test> getComparator() {
		return ordering;
	}

}
