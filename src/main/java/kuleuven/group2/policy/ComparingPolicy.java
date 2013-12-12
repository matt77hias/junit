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
public abstract class ComparingPolicy implements TestSortingPolicy {

	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}

	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		List<Test> result = new ArrayList<Test>(tests);
		Collections.sort(result, getComparator(testDatabase));
		return result;
	}

	/**
	 * Get the comparator to use for sorting the tests.
	 * 
	 * @param testDatabase
	 *            The test database.
	 * @return The comparator.
	 */
	protected abstract Comparator<? super Test> getComparator(TestDatabase testDatabase);

}
