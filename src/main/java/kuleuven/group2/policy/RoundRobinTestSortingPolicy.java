package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Collections;
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

	@Override
	protected List<Test> combineSortedTests(List<WeightedSortResult> weightedResults) {
		// Create lists of sets and weighted repetitions of sets 
		List<LinkedHashSet<Test>> sets = new ArrayList<LinkedHashSet<Test>>(weightedResults.size());
		List<Iterable<LinkedHashSet<Test>>> weightedSets = new ArrayList<Iterable<LinkedHashSet<Test>>>(weightedResults.size());
		for (WeightedSortResult subResult : weightedResults) {
			LinkedHashSet<Test> sortedSet = new LinkedHashSet<Test>(subResult.getSortedTests());
			sets.add(sortedSet);
			weightedSets.add(Collections.nCopies(subResult.getWeight(), sortedSet));
		}

		// Prepare result
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
