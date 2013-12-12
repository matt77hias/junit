package kuleuven.group2.policy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A class of weighted policy containing additional
 * policy information.
 * 
 * @author	Group 2
 * @version	4 December 2013
 */
public class WeightedPolicy {
	
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
	 * Creates a new weighted policy with given test sorting
	 * policy and default weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 */
	public WeightedPolicy(TestSortingPolicy policy) {
		this(policy, DEFAULT_WEIGHT);
	}
	
	/**
	 * Creates a new weighted policy with given test sorting
	 * policy and weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 * @param	weight
	 * 			The weight.
	 */
	public WeightedPolicy(TestSortingPolicy policy, int weight) {
		checkNotNull(policy);
		this.policy = policy;
		setWeight(weight);
	}
	
	/**
	 * Returns the test sorting policy of this weighted policy.
	 * 
	 * @return	The test sorting policy of this weighted policy.
	 */
	public TestSortingPolicy getTestSortingPolicy() {
		return this.policy;
	}
	
	/**
	 * The test sorting policy of this weighted policy.
	 */
	private final TestSortingPolicy policy;
	
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
	 * @param weight
	 *            The new weight of this weighted policy.
	 */
	public void setWeight(int weight) {
		this.weight = (weight >= 1) ? weight : DEFAULT_WEIGHT;
	}

	/**
	 * The weight of this weighted policy.
	 */
	private int weight;

}