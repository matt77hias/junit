package kuleuven.group2.data.updating;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;

/**
 * A TestChangeUpdater detects additions or removal of test methods in a test
 * class and updates the database accordingly.
 * 
 * @author Mattias Buelens
 */
public class TestChangeUpdater {

	protected final TestDatabase database;
	protected final ClassLoader testClassLoader;

	public TestChangeUpdater(TestDatabase database, ClassLoader testClassLoader) {
		this.database = checkNotNull(database);
		this.testClassLoader = checkNotNull(testClassLoader);
	}

	/**
	 * Update all given test classes.
	 * 
	 * @param testClassNames
	 *            The names of the test classes.
	 */
	public void updateTestClasses(Collection<String> testClassNames) {
		for (String testClassName : testClassNames) {
			try {
				updateTestClass(testClassLoader.loadClass(testClassName));
			} catch (ClassNotFoundException e) {
				// Assume invalid, delete from database
				removeTests(testClassName);
			}
		}
	}

	/**
	 * Update the given class.
	 * 
	 * @param testClassName
	 *            The name of the test class.
	 */
	public void updateTestClass(Class<?> testClass) {
		// Create a runner
		Runner runner = Request.aClass(testClass).getRunner();
		// TODO: when building fails it returns an errorreportingrunner
		// we should probably do something with this so this can be catched
		if (runner == null) return;
		// Collect test method names
		Set<String> testNames = new HashSet<String>();
		for (Description description : runner.getDescription().getChildren()) {
			// Only consider atomic tests
			if (description.isTest()) {
				testNames.add(description.getMethodName());
			}
		}
		// Update database
		updateTests(testClass.getName(), testNames);
	}

	/**
	 * Update the database using the newly collected test method names.
	 * 
	 * <p>
	 * This removes the old test methods and adds the new test methods.
	 * </p>
	 * 
	 * @param testClassName
	 *            The class name.
	 * @param newTestNames
	 *            The set of new test method names.
	 */
	protected void updateTests(String testClassName, Collection<String> newTestNames) {
		// Remove old tests
		Collection<Test> oldTests = database.getTestsIn(testClassName);
		for (Test test : oldTests) {
			if (!newTestNames.contains(test.getTestMethodName())) {
				database.removeTest(test);
			}
		}
		// Add new tests
		for (String testMethodName : newTestNames) {
			database.getOrCreateTest(testClassName, testMethodName);
		}
	}

	/**
	 * Remove all tests from the given test classes.
	 * 
	 * @param removedTestClassNames
	 *            The names of removed classes.
	 */
	public void removeTests(Collection<String> removedTestClassNames) {
		for (String removedTestClassName : removedTestClassNames) {
			removeTests(removedTestClassName);
		}
	}

	/**
	 * Remove all tests from the given test class.
	 * 
	 * @param removedTestClassName
	 *            The name of the removed class.
	 */
	public void removeTests(String removedTestClassName) {
		Collection<Test> classTests = database.getTestsIn(removedTestClassName);
		for (Test test : classTests) {
			database.removeTest(test);
		}
	}

}
