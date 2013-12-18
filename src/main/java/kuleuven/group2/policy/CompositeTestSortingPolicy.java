package kuleuven.group2.policy;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import com.google.common.collect.ImmutableList;

/**
 * A class of composite test sorting policies.
 * 
 * @author	Group 2
 * @version	12 December 2013
 */
public abstract class CompositeTestSortingPolicy implements TestSortingPolicy {
	
	/**
	 * Creates a new composite test sorting policy.
	 */
	protected CompositeTestSortingPolicy() {
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Loop prevention
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Checks if this composite test sorting policy contains the given
	 * test sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be checked.
	 * @return	True if and only if this composite test sorting policy
	 * 			contains the given test sorting policy or refers itself
	 * 			to the given test sorting policy.
	 */
	@Override
	public boolean contains(TestSortingPolicy policy) {
		if (this == policy) {
			return true;
		}
		for (WeightedTestSortingPolicy wp : policies) {
			if (wp.contains(policy)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if this composite test sorting policy can have the
	 * given test sorting policy as one of its test sorting policies.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be checked.
	 * @return	True if and only if this composite test sorting policy
	 * 			can have the given test sorting policy as one of its
	 * 			test sorting policies. This is always the case for non
	 * 			composite test sorting policies. A composite test sorting
	 * 			policy is allowed if this composite test sorting policy
	 * 			doesn't contain that composite test sorting policy.
	 */
	public boolean canHaveAsTestSortingPolicy(TestSortingPolicy policy) {
		return !contains(policy);
	}
	
	/**
	 * Checks if this composite test sorting policy can have the
	 * given test sorting policy as one of its test sorting policies.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be checked.
	 * @throws	IllegalArgumentException
	 * 			If and only if this composite test sorting policy
	 * 			cannot have the given test sorting policy as one of its
	 * 			test sorting policies. This is never the case for non
	 * 			composite test sorting policies. A composite test sorting
	 * 			policy is allowed if this composite test sorting policy
	 * 			doesn't contain that composite test sorting policy.
	 * 			Otherwise this exception is thrown.
	 */
	protected void checkCanHaveAsTestSortingPolicy(TestSortingPolicy policy) 
			throws IllegalArgumentException {
		if (!canHaveAsTestSortingPolicy(policy)) {
			throw new IllegalArgumentException(
				"This composite test sorting policy cannot contain the test sorting policy.");
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Test sorting
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sorts the tests of the given test database according to this composite
	 * policy.
	 * 
	 * @param 	testDatabase
	 *          The test database which contains the given tests.
	 * @return 	The tests of the given test database according to this composite
	 *         	policy.
	 */
	@Override
	public final List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}
	
	/**
	 * Sorts the given tests according to this composite
	 * test sorting policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 * @post	The given collection may be modified.
	 * @return	The tests of the given test database according to
	 * 			this composite test sorting policy.
	 */
	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		if (getNbOfWeightedTestSortingPolicies() == 0) {
			return ImmutableList.copyOf(tests);
		}
		return getCombinedSortedTests(testDatabase, tests);
	}
	
	/**
	 * Sorts the given tests according to this composite
	 * test sorting policy.
	 * 
	 * @pre		This composite test sorting policy must have at
	 * 			least one weighted test sorting policy.
	 * 			| getNbOfWeightedTestSortingPolicies() != 0
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @param 	tests
	 * 			The tests that needs to be sorted.
	 */
	protected List<Test> getCombinedSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		// Collect results from weighted policies
		List<WeightedSortResult> results = new ArrayList<WeightedSortResult>();
		for (WeightedTestSortingPolicy wp : this.policies) {
			results.add(wp.getSortedTests(testDatabase, tests));
		}
		// Combine results
		return combineSortedTests(results);
	}
	
	/**
	 * Combines the given sorted tests and returns the resulting tests
	 * according to this composite test sorting policy.
	 * 
	 * @pre		This composite test sorting policy must have at
	 * 			least one weighted test sorting policy.
	 * 			| getNbOfWeightedTestSortingPolicies() != 0
	 * @param 	weightedResults
	 * 			A list containing all sorted tests and weights.
	 * @return	The tests that needs to be sorted.
	 */
	protected abstract List<Test> combineSortedTests(List<WeightedSortResult> weightedResults);
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Test sorting policies management
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The weighted test sorting policies of this composite test sorting policy.
	 */
	protected LinkedList<WeightedTestSortingPolicy> policies = new LinkedList<WeightedTestSortingPolicy>();
	
	/**
	 * Appends the given test sorting policy to the
	 * weighted test sorting policies of this composite test sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the  weighted policies of this composite
	 * 			test sorting policy.
	 */
	public void addLastTestSortingPolicy(TestSortingPolicy policy) 
			throws IllegalArgumentException{
		addLastWeightedTestSortingPolicy(encapsulate(policy));
	}
	
	/**
	 * Appends the given weighted test sorting policy to the
	 * weighted test sorting policies of this composite test sorting
	 * policy.
	 * 
	 * @param	policy
	 * 			The weighted test sorting policy that has to be
	 * 			added to the weighted test sorting policies of
	 * 			this composite test sorting policy.
	 */
	public void addLastWeightedTestSortingPolicy(WeightedTestSortingPolicy policy) 
			throws IllegalArgumentException {
		checkNotNull(policy);
		checkCanHaveAsTestSortingPolicy(policy.getTestSortingPolicy());
		this.policies.addLast(policy);
	}
	
	/**
	 * Adds the given test sorting policy to the front
	 * of the weighted test sorting policies of this composite test
	 * sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the weighted policies of
	 * 			this composite test sorting policy.
	 */
	public void addFirstTestSortingPolicy(TestSortingPolicy policy) 
			throws IllegalArgumentException {
		addFirstWeightedTestSortingPolicy(encapsulate(policy));
	}
	
	/**
	 * Adds the given weighted test sorting policy to the front
	 * of the weighted policies of this composite sorting
	 * policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the weighted test sorting policies
	 * 			of this composite test sorting policy.
	 */
	public void addFirstWeightedTestSortingPolicy(WeightedTestSortingPolicy policy) 
			throws IllegalArgumentException {
		checkNotNull(policy);
		checkCanHaveAsTestSortingPolicy(policy.getTestSortingPolicy());
		this.policies.addFirst(policy);
	}
	
	/**
	 * Adds the given test sorting policy at the
	 * given index of the weighted test sorting policies of
	 * this composite test sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	policy
	 * 			The test sorting policy that has
	 * 			to be added at the given index to the weighted
	 * 			test sorting policies of this composite test
	 * 			sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addTestSortingPolicyAt(int index, TestSortingPolicy policy) 
			throws IllegalArgumentException, IndexOutOfBoundsException {
		addWeightedTestSortingPolicyAt(index, encapsulate(policy));
	}
	
	/**
	 * Adds the given weighted test sorting policy at the given index
	 * of the weighted test sorting policies of this composite test
	 * sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	policy
	 * 			The weighted test sorting policy that has to be
	 * 			added at the given index to the weighted test
	 * 			sorting policies of this composite test sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addWeightedTestSortingPolicyAt(int index, WeightedTestSortingPolicy policy) 
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkNotNull(policy);
		checkCanHaveAsTestSortingPolicy(policy.getTestSortingPolicy());
		this.policies.add(index, policy);
	}
	
	/**
	 * Replaces the weighted test sorting policy at the given index
	 * with the given test sorting policy.
	 * 
	 * @param	index
	 * 			The index at which to replace.
	 * @param	policy
	 * 			The replacement test sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void setTestSortingPolicyAt(int index, TestSortingPolicy policy) 
			throws IllegalArgumentException, IndexOutOfBoundsException {
		setWeightedTestSortingPolicyAt(index, encapsulate(policy));
	}

	/**
	 * Replaces the weighted test sorting policy at the given index
	 * with the given weighted test sorting policy.
	 * 
	 * @param	index
	 * 			The index at which to replace.
	 * @param	policy
	 * 			The replacement weighted test sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index >= getNbOfPolicies())
	 */
	public void setWeightedTestSortingPolicyAt(int index, WeightedTestSortingPolicy policy) 
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkNotNull(policy);
		checkCanHaveAsTestSortingPolicy(policy.getTestSortingPolicy());
		this.policies.set(index, policy);
	}
	
	/**
	 * Removes the given weighted test sorting policy.
	 * 
	 * @param	policy
	 * 			The weighted test sorting policy that has to be
	 * 			removed from the weighted test sorting policies
	 * 			of this composite test sorting policy.	
	 */
	public void removeWeightedTestSortingPolicy(WeightedTestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.remove(policy);
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
	 * @param	policy
	 * 			The weighted test sorting policy that has to
	 * 			be checked.
	 */
	public boolean containsDirectly(WeightedTestSortingPolicy policy) {
		return this.policies.contains(policy);
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
	 * Returns the test sorting policies of this
	 * composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	The test sorting policies of this
	 * 			composite test sorting policy.
	 */
	public List<TestSortingPolicy> getTestSortingPolicies() {
		List<TestSortingPolicy> temp = new ArrayList<TestSortingPolicy>();
		for (WeightedTestSortingPolicy wpolicy : this.policies) {
			temp.add(wpolicy.getTestSortingPolicy());
		}
		return ImmutableList.copyOf(temp);
	}
	
	/**
	 * Encapsulates the given test sorting policy
	 * in a weighted test sorting policy with default weight.
	 * 
	 * @param 	policy
	 * 			The test sorting policy that has
	 * 			to be encapsulated.
	 * @return	A weighted test sorting policy with default weight
	 * 			and the given test sorting policy as
	 * 			its test sorting policy.
	 */
	public static WeightedTestSortingPolicy encapsulate(TestSortingPolicy policy) {
		return new WeightedTestSortingPolicy(policy);
	}
}
