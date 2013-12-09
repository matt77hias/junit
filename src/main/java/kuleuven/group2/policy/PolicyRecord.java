package kuleuven.group2.policy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A class of policy records containing additional
 * policy information.
 * 
 * @author	Group 2
 * @version	4 December 2013
 */
public class PolicyRecord {
	
	/**
	 * The default weight for policy records.
	 * 
	 * It's also the lowest possible weight.
	 * 
	 * If weight1 > weight2 then the record
	 * associated with weight1 has higher priority
	 * then the record associated with weight2.
	 */
	public static final int DEFAULT_WEIGHT = 1;
	
	/**
	 * Creates a new policy record with given test sorting
	 * policy and default weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 */
	public PolicyRecord(TestSortingPolicy policy) {
		this(policy, DEFAULT_WEIGHT);
	}
	
	/**
	 * Creates a new policy record with given test sorting
	 * policy and weight.
	 * 
	 * @param	policy
	 * 			The test sorting policy.
	 * @param	weight
	 * 			The weight.
	 */
	public PolicyRecord(TestSortingPolicy policy, int weight) {
		checkNotNull(policy);
		this.policy = policy;
		setWeight(weight);
	}
	
	/**
	 * Returns the test sorting policy of this policy record.
	 * 
	 * @return	The test sorting policy of this policy record.
	 */
	public TestSortingPolicy getTestSortingPolicy() {
		return this.policy;
	}
	
	/**
	 * The test sorting policy of this test
	 * sorting record.
	 */
	private final TestSortingPolicy policy;
	
	/**
	 * Returns the weight of this policy record.
	 * 
	 * @return	The weight of this policy record.
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Sets the weight of this polcy record.
	 * 
	 * @param weight
	 *            The new weight of this policy record.
	 */
	public void setWeight(int weight) {
		this.weight = (weight >= 1) ? weight : DEFAULT_WEIGHT;
	}

	/**
	 * The weight of this policy record.
	 */
	private int weight;
}