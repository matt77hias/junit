package kuleuven.group2.policy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A class of composite test sorting policies.
 * 
 * @author	Group 2
 * @version	12 December 2013
 */
public abstract class CompositeTestSortingPolicy implements NonWeightedTestSortingPolicy {
	
	/**
	 * Creates a new composite test sorting policy.
	 */
	protected CompositeTestSortingPolicy() {
	}
	
	/**
	 * Checks if this composite test sorting policy contains the given
	 * test sorting policy.
	 * 
	 * @return	True if and only if this composite test sorting policy
	 * 			contains the given test sorting policy or refers itself
	 * 			to the given test sorting policy.
	 */
	@Override
	public boolean contains(TestSortingPolicy policy) {
		if (this == policy) {
			return true;
		}
		for (WeightedTestSortingPolicy wpolicy : policies) {
			if (wpolicy.contains(policy)) {
				return true;
			}
		}
		return false;
	}
	
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
	public final List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Test sorting policies management
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The weighted test sorting policies of this composite test sorting policy.
	 */
	protected LinkedList<WeightedTestSortingPolicy> policies = new LinkedList<WeightedTestSortingPolicy>();
	
	/**
	 * Appends the given non-weighted test sorting policy to the
	 * weighted test sorting policies of this composite test sorting policy.
	 * 
	 * @param	policy
	 * 			The non-weighted test sorting policy that has to be
	 * 			added to the  weighted policies of this composite
	 * 			test sorting policy.
	 */
	public void addLastNonWeightedTestSortingPolicy(NonWeightedTestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addLast(new WeightedTestSortingPolicy(policy));
	}
	
	/**
	 * Appends the given weighted test sorting policy to the
	 * weighted test sorting policies of this composite test sorting
	 * policy.
	 * 
	 * @param	WeightedTestSortingPolicy
	 * 			The weighted test sorting policy that has to be
	 * 			added to the weighted test sorting policies of
	 * 			this composite test sorting policy.
	 */
	public void addLastWeightedTestSortingPolicy(WeightedTestSortingPolicy WeightedTestSortingPolicy) {
		checkNotNull(WeightedTestSortingPolicy);
		this.policies.addLast(WeightedTestSortingPolicy);
	}
	
	/**
	 * Adds the given non-weighted test sorting policy to the front
	 * of the weighted test sorting policies of this composite test
	 * sorting policy.
	 * 
	 * @param	policy
	 * 			The non-weighted test sorting policy that has to be
	 * 			added to the weighted policies of
	 * 			this composite test sorting policy.
	 */
	public void addFirstNonWeightedTestSortingPolicy(NonWeightedTestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addFirst(new WeightedTestSortingPolicy(policy));
	}
	
	/**
	 * Adds the given weighted test sorting policy to the front
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	WeightedTestSortingPolicy
	 * 			The test sorting policy that has to be
	 * 			added to the weighted test sorting policies
	 * 			of this composite test sorting policy.
	 */
	public void addFirstWeightedTestSortingPolicy(WeightedTestSortingPolicy WeightedTestSortingPolicy) {
		checkNotNull(WeightedTestSortingPolicy);
		this.policies.addFirst(WeightedTestSortingPolicy);
	}
	
	/**
	 * Adds the given non-weighted test sorting policy at the
	 * given index of the weighted test sorting policies of
	 * this composite test sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	policy
	 * 			The non-weighted test sorting policy that has
	 * 			to be added at the given index to the weighted
	 * 			test sorting policies of this composite test
	 * 			sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addNonWeightedTestSortingPolicyAt(int index, NonWeightedTestSortingPolicy policy) 
			throws IndexOutOfBoundsException {
		checkNotNull(policy);
		this.policies.add(index, new WeightedTestSortingPolicy(policy));
	}
	
	/**
	 * Adds the given weighted test sorting policy at the given index
	 * of the weighted test sorting policies of this composite test
	 * sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	WeightedTestSortingPolicy
	 * 			The weighted test sorting policy that has to be
	 * 			added at the given index to the weighted test
	 * 			sorting policies of this composite test sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addWeightedTestSortingPolicyAt(int index, WeightedTestSortingPolicy WeightedTestSortingPolicy) 
			throws IndexOutOfBoundsException {
		checkNotNull(WeightedTestSortingPolicy);
		this.policies.add(index, WeightedTestSortingPolicy);
	}

	/**
	 * Replaces the weighted test sorting policy at the given index
	 * with the given weighted test sorting policy.
	 * 
	 * @param	index
	 * 			The index at which to replace.
	 * @param	WeightedTestSortingPolicy
	 * 			The replacement weighted test sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void setWeightedTestSortingPolicyAt(int index, WeightedTestSortingPolicy WeightedTestSortingPolicy) 
			throws IndexOutOfBoundsException {
		checkNotNull(WeightedTestSortingPolicy);
		this.policies.set(index, WeightedTestSortingPolicy);
	}
	
	/**
	 * Removes the given weighted test sorting policy.
	 * 
	 * @param	WeightedTestSortingPolicy
	 * 			The weighted test sorting policy that has to be
	 * 			removed from the weighted test sorting policies
	 * 			of this composite test sorting policy.	
	 */
	public void removeWeightedTestSortingPolicy(WeightedTestSortingPolicy WeightedTestSortingPolicy) {
		checkNotNull(WeightedTestSortingPolicy);
		this.policies.remove(WeightedTestSortingPolicy);
	}
	
	/**
	 * Removes the weighted test sorting policy at the given index.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void removeWeightedTestSortingPolicyAt(int index) 
			throws IndexOutOfBoundsException {
		this.policies.remove(index);
	}
	
	/**
	 * Checks if this composite test sorting policy
	 * contains the given weighted test sorting policy
	 * as one of its weighted test sorting policies.
	 * [only first level considered]
	 * 
	 * @param	WeightedTestSortingPolicy
	 * 			The weighted test sorting policy that has to
	 * 			be checked.
	 */
	public boolean containsDirectly(WeightedTestSortingPolicy WeightedTestSortingPolicy) {
		return this.policies.contains(WeightedTestSortingPolicy);
	}
	
	/**
	 * Returns the weighted test sorting policy at the given
	 * index of this composite test sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public WeightedTestSortingPolicy getWeightedTestSortingPolicyAt(int index)
			throws IndexOutOfBoundsException {
		return this.policies.get(index);
	}
	
	/**
	 * Returns the number of weighted test sorting policies
	 * of this composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	Returns the number weighted test sorted
	 * 			policies of this composite test sorting
	 * 			policy.
	 */
	public int getNbOfWeightedTestSortingPolicies() {
		return this.policies.size();
	}
	
	/**
	 * Returns the weighted test sorting policies of this
	 * composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	The weighted test sorting policies of this
	 * 			composite test sorting policy.
	 */
	public List<WeightedTestSortingPolicy> getWeightedTestSortingPolicies() {
		return ImmutableList.copyOf(this.policies);
	}
	
	/**
	 * Returns the non-weighted test sorting policies of this
	 * composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	The non-weighted test sorting policies of this
	 * 			composite test sorting policy.
	 */
	public List<NonWeightedTestSortingPolicy> getNonWeightedTestSortingPolicies() {
		List<NonWeightedTestSortingPolicy> temp = new ArrayList<NonWeightedTestSortingPolicy>();
		for (WeightedTestSortingPolicy wpolicy : this.policies) {
			temp.add(wpolicy.getNonWeightedTestSortingPolicy());
		}
		return ImmutableList.copyOf(temp);
	}
}
