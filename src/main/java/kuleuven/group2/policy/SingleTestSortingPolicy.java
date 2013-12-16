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

}
