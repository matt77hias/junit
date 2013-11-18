package kuleuven.group2.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestRemoveUpdater {

	protected final TestDatabase database;

	public TestRemoveUpdater(TestDatabase database) {
		this.database = checkNotNull(database);
	}

	/**
	 * Remove all tests from the given test classes in the database.
	 * 
	 * @param removedTestClassNames
	 *            The names of removed classes.
	 */
	public void removeTestClasses(Collection<String> removedTestClassNames) {
		for (String testClassName : removedTestClassNames) {
			// TODO
			// Set<Test> classTests = database.getTestsIn(testClassName);
			Set<Test> classTests = new HashSet<>();
			for (Test test : classTests) {
				database.removeTest(test);
			}
		}
	}

}
