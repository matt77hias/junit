package kuleuven.group2.policy;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

public class CompositePolicy implements TestSortingPolicy {
	
	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Test> getSortedTests(TestDatabase testDatabase, Collection<Test> tests) {
		// TODO Auto-generated method stub
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Test sorting policies management
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The policy records of this composite sorting policy.
	 */
	protected LinkedList<PolicyRecord> policies = new LinkedList<PolicyRecord>();
	
	/**
	 * Appends the given test sorting policy to the
	 * records of this composite sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the  policy records of
	 * 			this composite sorting policy.
	 */
	public void addLastPolicy(TestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addLast(new PolicyRecord(policy));
	}
	
	/**
	 * Appends the given policy record to the
	 * policy records of this composite sorting
	 * policy.
	 * 
	 * @param	record
	 * 			The policy record that has to be
	 * 			added to the policy records of
	 * 			this composite sorting policy.
	 */
	public void addLastPolicyRecord(PolicyRecord record) {
		checkNotNull(record);
		this.policies.addLast(record);
	}
	
	/**
	 * Adds the given test sorting policy to the front
	 * of the policy records of this composite sorting
	 * policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added to the policy records of
	 * 			this composite sorting policy.
	 */
	public void addFirstPolicy(TestSortingPolicy policy) {
		checkNotNull(policy);
		this.policies.addFirst(new PolicyRecord(policy));
	}
	
	/**
	 * Adds the given policy record to the front
	 * of the policy records of this composite sorting
	 * policy.
	 * 
	 * @param	record
	 * 			The test sorting policy that has to be
	 * 			added to the policy records of
	 * 			this composite sorting policy.
	 */
	public void addFirstPolicyRecord(PolicyRecord record) {
		checkNotNull(record);
		this.policies.addFirst(record);
	}
	
	/**
	 * Adds the given test sorting policy at the given index
	 * of the policy records of this composite sorting
	 * policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	policy
	 * 			The test sorting policy that has to be
	 * 			added at the given index to the policy
	 * 			records of this composite sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addPolicyAt(int index, TestSortingPolicy policy) 
			throws IndexOutOfBoundsException {
		checkNotNull(policy);
		this.policies.add(index, new PolicyRecord(policy));
	}
	
	/**
	 * Adds the given policy record at the given index
	 * of the policy records of this composite sorting
	 * policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @param	record
	 * 			The policy record that has to be
	 * 			added at the given index to the
	 * 			policy records of this composite sorting policy.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void addPolicyRecordAt(int index, PolicyRecord record) 
			throws IndexOutOfBoundsException {
		checkNotNull(record);
		this.policies.add(index, record);
	}
	
	/**
	 * Removes the given test sorting policy.
	 * 
	 * @param	record
	 * 			The policy record that has to be
	 * 			removed from the policy records
	 * 			of this composite sorting policy.	
	 */
	public void removePolicy(PolicyRecord record) {
		checkNotNull(record);
		this.policies.remove(record);
	}
	
	/**
	 * Removes the policy record at the given index.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public void removePolicyAt(int index) 
			throws IndexOutOfBoundsException {
		this.policies.remove(index);
	}
	
	/**
	 * Checks if this composite test sorting policy
	 * contains the given policy record as one
	 * of its policy records.
	 * [only first level considered]
	 * 
	 * @param	record
	 * 			The policy record that has to
	 * 			be checked.
	 */
	public boolean contains(PolicyRecord record) {
		return this.policies.contains(record);
	}
	
	/**
	 * Returns the policy record at the given
	 * index of this composite test sorting policy.
	 * 
	 * @param	index
	 * 			The index.
	 * @throws	IndexOutOfBoundsException
	 * 			If the index is out of range.
	 * 			| (index < 0 || index > getNbOfPolicies())
	 */
	public PolicyRecord getPolicyAt(int index)
			throws IndexOutOfBoundsException {
		return this.policies.get(index);
	}
	
	/**
	 * Returns the number policy records
	 * of this composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	Returns the number policy records
	 * 			of this composite test sorting policy.
	 */
	public int getNbOfPolicyRecords() {
		return this.policies.size();
	}
	
	/**
	 * Returns the policy records of this
	 * composite test sorting policy.
	 * [only first level considered]
	 * 
	 * @return	The policy records of this
	 * 			composite test sorting policy.
	 */
	public List<PolicyRecord> getPolicyRecords() {
		return ImmutableList.copyOf(this.policies);
	}
}
