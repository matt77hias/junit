package kuleuven.group2.policy;

import java.util.Comparator;
import java.util.List;

import kuleuven.group2.data.Test;

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
