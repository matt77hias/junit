package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * A composite policy using a Round Robin strategy.
 * 
 * @author Group 2
 * @version 12 December 2013
 */
public class RoundRobinTestSortingPolicy extends CompositeTestSortingPolicy {

	/**
	 * Sorts the tests of the given test database according to this composite
	 * policy.
	 * 
	 * @param testDatabase
	 *            The test database which contains the given tests.
	 * @return The tests of the given test database according to this composite
	 *         policy.
	 */
	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}

	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		List<LinkedHashSet<Test>> sets = new ArrayList<>(this.policies.size());
		List<Iterable<LinkedHashSet<Test>>> weightedSets = new ArrayList<>(this.policies.size());
		List<Test> result = new ArrayList<>(tests.size());

		// Collect sorted tests from weighted policies
		for (WeightedPolicy wp : this.policies) {
			List<Test> sorted = wp.getTestSortingPolicy().getSortedTests(testDatabase, tests);
			LinkedHashSet<Test> sortedSet = new LinkedHashSet<Test>(sorted);
			sets.add(sortedSet);
			weightedSets.add(Collections.nCopies(wp.getWeight(), sortedSet));
		}

		// Create cyclic weighted queue
		Iterable<LinkedHashSet<Test>> queue = Iterables.concat(weightedSets);
		Iterator<LinkedHashSet<Test>> it = Iterators.cycle(queue);

		for (int i = 0; i < tests.size(); i++) {
			// Get next test
			LinkedHashSet<Test> currentSet = it.next();
			Test test = currentSet.iterator().next();
			// Add to result
			result.add(test);
			// Remove from all tests
			for (LinkedHashSet<Test> set : sets) {
				set.remove(test);
			}
		}

		return result;
	}

}
