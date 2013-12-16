package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

/**
 * A test sorting policy which sorts the tests using a {@link Comparator}.
 * 
 * @author Group 2
 * @version 12 December 2013
 */
public abstract class ComparingTestSortingPolicy extends SingleTestSortingPolicy {

	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		List<Test> result = new ArrayList<Test>(tests);
		Collections.sort(result, getComparator());
		return result;
	}

	/**
	 * Get the comparator to use for sorting the tests.
	 * 
	 * @return The comparator.
	 */
	protected abstract Comparator<? super Test> getComparator();

}
