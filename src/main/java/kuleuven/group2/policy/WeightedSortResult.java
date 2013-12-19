package kuleuven.group2.policy;

import java.util.List;

import kuleuven.group2.data.Test;

/**
 * A result from a {@link WeightedTestSortingPolicy}, containing a sorted list
 * of tests and a weight.
 * 
 * @author Group 2
 * @version 18 December 2013
 */
public class WeightedSortResult {

	private final List<Test> sortedTests;
	private final int weight;

	public WeightedSortResult(List<Test> sortedTests, int weight) {
		this.sortedTests = sortedTests;
		this.weight = weight;
	}

	public List<Test> getSortedTests() {
		return sortedTests;
	}

	public int getWeight() {
		return weight;
	}

}
