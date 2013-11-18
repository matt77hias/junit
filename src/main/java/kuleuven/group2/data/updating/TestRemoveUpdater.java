package kuleuven.group2.data.updating;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

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
			Collection<Test> classTests = database.getTestsIn(testClassName);
			for (Test test : classTests) {
				database.removeTest(test);
			}
		}
	}

}
