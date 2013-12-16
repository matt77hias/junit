package kuleuven.group2.policy;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

/**
 * A class of weighted test sorting policies containing additional
 * policy information (e.g.: weight).
 * 
 * @author	Group 2
 * @version	4 December 2013
 */
public class WeightedTestSortingPolicy implements TestSortingPolicy {
	
	/**
	 * The default weight for weighted policies.
	 * 
	 * It's also the lowest possible weight.
	 * 
	 * If weight1 > weight2 then the weighted policy
	 * associated with weight1 has higher priority
	 * than the weighted policy associated with weight2.
	 */
	public static final int DEFAULT_WEIGHT = 1;
	
	/**
	 * Creates a new weighted policy with given single test sorting
	 * policy and default weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 */
	public WeightedTestSortingPolicy(NonWeightedTestSortingPolicy policy) {
		this(policy, DEFAULT_WEIGHT);
	}
	
	/**
	 * Creates a new weighted policy with given single test sorting
	 * policy and weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 * @param	weight
	 * 			The weight.
	 */
	public WeightedTestSortingPolicy(NonWeightedTestSortingPolicy policy, int weight) {
		checkNotNull(policy);
		this.policy = policy;
		setWeight(weight);
	}
	
	/**
	 * Returns the non-weighted test sorting policy of this weighted policy.
	 * 
	 * @return	The non-weighted test sorting policy of this weighted policy.
	 */
	public NonWeightedTestSortingPolicy getNonWeightedTestSortingPolicy() {
		return this.policy;
	}
	
	/**
	 * The non-weighted test sorting policy of this weighted policy.
	 */
	private final NonWeightedTestSortingPolicy policy;
	
	/**
	 * Returns the weight of this weighted policy.
	 * 
	 * @return	The weight of this weighted policy.
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Sets the weight of this weighted policy.
	 * 
	 * @param 	weight
	 *          The new weight of this weighted policy.
	 */
	public void setWeight(int weight) {
		this.weight = (weight >= 1) ? weight : DEFAULT_WEIGHT;
	}

	/**
	 * The weight of this weighted policy.
	 */
	private int weight;
	
	/**
	 * Sorts the tests of the given test database according to this
	 * weighted policy its non-weighted test sorting policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @return	The tests of the given test database according to this
	 * 			weighted policy its test sorting policy.
	 */
	public List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}
		
	/**
	 * Sorts the given tests according to this
	 * weighted policy its non-weighted test sorting policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @post	The given collection may be modified.
	 * @return	The tests of the given test database according to this
	 * 			weighted policy its test sorting policy.
	 */
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		return getNonWeightedTestSortingPolicy().getSortedTests(testDatabase, tests);
	}

	/**
	 * Checks if this weighted test sorting policy contains the given
	 * test sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be checked.
	 * @return	True if and only if this test sorting policy
	 * 			contains the given test sorting policy.
	 */
	public boolean contains(TestSortingPolicy policy) {
		return (this == policy) || (getNonWeightedTestSortingPolicy().contains(policy));
	}
}