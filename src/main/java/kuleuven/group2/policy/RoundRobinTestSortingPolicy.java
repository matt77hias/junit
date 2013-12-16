package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import kuleuven.group2.data.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * A composite policy using a Round Robin strategy.
 * 
 * @author 	Group 2
 * @version 16 December 2013
 */
public class RoundRobinTestSortingPolicy extends CompositeTestSortingPolicy {

	/**
	 * Combines the given sorted tests and returns the resulting tests
	 * according to this round robin test sorting policy.
	 * 
	 * @pre		This round robin test sorting policy must have at
	 * 			least one weighted test sorting policy.
	 * 			| getNbOfWeightedTestSortingPolicies() != 0
	 * @param 	sets
	 * 			A list containing all the sorted tests from
	 * 			this round robin test sorting policies direct weighted
	 * 			test sorting policies' non-weighted test sorting policy
	 * 			in the order of the appearance of the direct weighted
	 * 			test sorting policies of this round robin test sorting
	 * 			policy.
	 * @param 	weightedSets
	 * 			A list containing an iterable collection for this
	 * 			round robin test sorting policies direct weighted
	 * 			test sorting policies' non-weighted test sorting policy
	 * 			in the order of the appearance of the direct weighted
	 * 			test sorting policies of this round robin test sorting
	 * 			policy and as many times as the weighted test sorting
	 * 			policies weight.
	 * @return	The tests that needs to be sorted.
	 */
	@Override
	protected List<Test> combineSortedTests(List<LinkedHashSet<Test>> sets, List<Iterable<LinkedHashSet<Test>>> weightedSets) {
		int size = sets.get(0).size();
		List<Test> result = new ArrayList<Test>(size);

		// Create cyclic weighted queue
		Iterable<LinkedHashSet<Test>> queue = Iterables.concat(weightedSets);
		Iterator<LinkedHashSet<Test>> it = Iterators.cycle(queue);

		for (int i = 0; i < size; i++) {
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
