package kuleuven.group2.policy;

import java.util.List;

import kuleuven.group2.data.Test;

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
