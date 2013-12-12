package kuleuven.group2.policy;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A class of composite policies.
 * 
 * @author	Group 2
 * @version	12 December 2013
 */
public abstract class CompositePolicy implements TestSortingPolicy {
	
	/**
	 * Creates a new composite policy.
	 */
	protected CompositePolicy() {
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Test sorting policies management
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The weighted policies of this composite sorting policy.
	 */
	protected LinkedList<WeightedPolicy> policies = new LinkedList<WeightedPolicy>();
	
	/**
	 * Appends the given test sorting policy to the
	 * weightedPolicys of this composite sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the  weighted policies of
	 * 			this composite sorting policy.
	 */
	public void addLastPolicy(TestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addLast(new WeightedPolicy(policy));
	}
	
	/**
	 * Appends the given weighted policy to the
	 * weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	weightedPolicy
	 * 			The weighted policy that has to be
	 * 			added to the weighted policies of
	 * 			this composite sorting policy.
	 */
	public void addLastWeightedPolicy(WeightedPolicy weightedPolicy) {
		checkNotNull(weightedPolicy);
		this.policies.addLast(weightedPolicy);
	}
	
	/**
	 * Adds the given test sorting policy to the front
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the weighted policies of
	 * 			this composite sorting policy.
	 */
	public void addFirstPolicy(TestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addFirst(new WeightedPolicy(policy));
	}
	
	/**
	 * Adds the given weighted policy to the front
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	weightedPolicy
	 * 			The test sorting policy that has to be
	 * 			added to the weighted policies of
	 * 			this composite sorting policy.
	 */
	public void addFirstWeightedPolicy(WeightedPolicy weightedPolicy) {
		checkNotNull(weightedPolicy);
		this.policies.addFirst(weightedPolicy);
	}
	
	/**
	 * Adds the given test sorting policy at the given index
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added at the given index to the policy
	 * 			weightedPolicys of this composite sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addPolicyAt(int index, TestSortingPolicy policy) 
			throws IndexOutOfBoundsException {
		checkNotNull(policy);
		this.policies.add(index, new WeightedPolicy(policy));
	}
	
	/**
	 * Adds the given weighted policy at the given index
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	weightedPolicy
	 * 			The weighted policy that has to be
	 * 			added at the given index to the
	 * 			weighted policies of this composite sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addWeightedPolicyAt(int index, WeightedPolicy weightedPolicy) 
			throws IndexOutOfBoundsException {
		checkNotNull(weightedPolicy);
		this.policies.add(index, weightedPolicy);
	}

	/**
	 * Replaces the weighted policy at the given index
	 * with the given weighted policy.
	 * 
	 * @param	index
	 * 			The index at which to replace.
	 * @param	weightedPolicy
	 * 			The replacement weighted policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void setWeightedPolicyAt(int index, WeightedPolicy weightedPolicy) 
			throws IndexOutOfBoundsException {
		checkNotNull(weightedPolicy);
		this.policies.set(index, weightedPolicy);
	}
	
	/**
	 * Removes the given test sorting policy.
	 * 
	 * @param	weightedPolicy
	 * 			The weighted policy that has to be
	 * 			removed from the weighted policies
	 * 			of this composite sorting policy.	
	 */
	public void removeWeightedPolicy(WeightedPolicy weightedPolicy) {
		checkNotNull(weightedPolicy);
		this.policies.remove(weightedPolicy);
	}
	
	/**
	 * Removes the weighted policy at the given index.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void removeWeightedPolicyAt(int index) 
			throws IndexOutOfBoundsException {
		this.policies.remove(index);
	}
	
	/**
	 * Checks if this composite test sorting policy
	 * contains the given weighted policy as one
	 * of its weighted policies.
	 * [only first level considered]
	 * 
	 * @param	weightedPolicy
	 * 			The weighted policy that has to
	 * 			be checked.
	 */
	public boolean contains(WeightedPolicy weightedPolicy) {
		return this.policies.contains(weightedPolicy);
	}
	
	/**
	 * Returns the weighted policy at the given
	 * index of this composite test sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public WeightedPolicy getWeightedPolicyAt(int index)
			throws IndexOutOfBoundsException {
		return this.policies.get(index);
	}
	
	/**
	 * Returns the number weighted policies
	 * of this composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	Returns the number weighted policies
	 * 			of this composite test sorting policy.
	 */
	public int getNbOfWeightedPolicies() {
		return this.policies.size();
	}
	
	/**
	 * Returns the weighted policies of this
	 * composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	The weighted policies of this
	 * 			composite test sorting policy.
	 */
	public List<WeightedPolicy> getWeightedPolicies() {
		return ImmutableList.copyOf(this.policies);
	}
}
