package kuleuven.group2.policy;

import java.util.List;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

public abstract class SingleTestSortingPolicy implements TestSortingPolicy {
	
	/**
	 * Sorts the tests of the given test database according to this last failure policy.
	 * 
	 * @param	testDatabase
	 * 			The test database which contains the given tests.
	 * @return	The tests of the given test database according to this
	 * 			last failure policy.
	 */
	@Override
	public final List<Test> getSortedTests(TestDatabase testDatabase) {
		return getSortedTests(testDatabase, testDatabase.getAllTests());
	}
	
	/**
	 * Checks if this test sorting policy contains the given
	 * test sorting policy.
	 * 
	 * @param	policy
	 * 			The test sorting policy that has to be checked.
	 * @return	True if and only if this test sorting policy
	 * 			contains the given test sorting policy. This
	 * 			means the given test sorting policy refers to
	 * 			this single test sorting policy.
	 */
	@Override
	public boolean contains(TestSortingPolicy policy) {
		return (this == policy);
	}
}
