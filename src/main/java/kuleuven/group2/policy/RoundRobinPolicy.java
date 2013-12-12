package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultiset;

/**
 * A composite policy using a Round Robin strategy.
 * 
 * @author Group 2
 * @version 12 December 2013
 */
public class RoundRobinPolicy extends CompositePolicy {

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
//		List<LinkedHashSet<Test>> rrqueue = new ArrayList<>();
//		List<Test> result = new ArrayList<Test>();
//		for (WeightedPolicy wp : this.policies) {
//			List<Test> sorted = wp.getTestSortingPolicy().getSortedTests(testDatabase, tests);
//			rrqueue.add(new LinkedHashSet<Test>(sorted), wp.getWeight());
//		}
//
//		Iterator<LinkedHashSet<Test>> it = Iterators.cycle(rrqueue);
//		for (int i = 0; i < tests.size(); i++) {
//			Test victim = null;
//			while (it.hasNext()) {
//				LinkedHashSet<Test> set = it.next();
//				if (!set.isEmpty()) {
//					victim = set.iterator().next();
//					break;
//				}
//			}
//			result.add(victim);
//			for (LinkedHashSet<Test> set : rrqueue) {
//				set.remove(victim);
//			}
//		}

		return null;
	}

}
