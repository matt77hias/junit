package kuleuven.group2.testrunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kuleuven.group2.data.Test;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * A class of test runners used for running single test methods in a predefined
 * order. This only violates the JUnit's fixed order execution. Set-ups and
 * tear-downs of test methods and test fixture are executed properly.
 * 
 * @author Group 2
 * @version 16 November 2013
 */
public class TestRunner {

	private final JUnitCore junitCore;
	private final ClassLoader classLoader;
	private final List<RunListener> listeners = new ArrayList<RunListener>();

	/**
	 * Creates a new test runner with no class loader.
	 */
	public TestRunner() {
		this(null);
	}

	/**
	 * Creates a new test runner with the given class loader as its class
	 * loader.
	 * 
	 * @param classLoader
	 *            The new class loader of this test runner.
	 */
	public TestRunner(ClassLoader classLoader) {
		this(classLoader, new JUnitCore());
	}

	/**
	 * Creates a new test runner with the given class loader as its class
	 * loader.
	 * 
	 * @param classLoader
	 *            The new class loader of this test runner.
	 * @param jUnitCore
	 *            The JUnitCore for this test runner. You can give your own
	 *            JUnitCore so that you don't need to register to another
	 *            JUnitCore.
	 */
	public TestRunner(ClassLoader classLoader, JUnitCore jUnitCore) {
		this.classLoader = classLoader;
		this.junitCore = jUnitCore;
		getJUnitCore().addListener(new TestRunListener());
	}

	/**
	 * Get the class loader used by this test runner.
	 */
	protected final ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * Get the JUnit instance used by this test runner.
	 */
	protected final JUnitCore getJUnitCore() {
		return this.junitCore;
	}

	/**
	 * Runs the given tests. This means that all the methods referred to in the
	 * Test objects will be ran separately.
	 * 
	 * @param tests
	 *            The tests that has to be ran.
	 */
	public List<Result> runTestMethods(List<Test> tests) throws Exception {
		return runTestMethods(getClassLoader(), tests);
	}

	/**
	 * Runs the given tests. This means that all the methods referred to in the
	 * Test objects will be ran separately.
	 * 
	 * @param ClassLoader
	 *            The class loader needed to obtain the class references.
	 * @param tests
	 *            The tests that has to be ran.
	 */
	public List<Result> runTestMethods(Test... tests) throws Exception {
		return runTestMethods(getClassLoader(), Arrays.asList(tests));
	}

	/**
	 * Runs the given tests. This means that all the methods referred to in the
	 * Test objects will be ran separately.
	 * 
	 * If class cannot be found, a null reference is stored.
	 * 
	 * @param ClassLoader
	 *            The class loader needed to obtain the class references.
	 * @param tests
	 *            The tests that has to be ran.
	 */
	public List<Result> runTestMethods(ClassLoader classLoader, List<Test> tests) throws Exception {
		// Test run started
		fireTestRunStarted(createDescription(getClass().getSimpleName(), tests));
		// Collect total result
		Result totalResult = new Result();
		RunListener totalResultListener = totalResult.createListener();
		addRunListener(totalResultListener);
		// Collect individual results
		List<Result> results = new ArrayList<Result>(tests.size());
		for (Test test : tests) {
			try {
				// Obtain the class with the specified binary name in the test
				// object.
				Class<?> klass = classLoader.loadClass(test.getTestClassName());
				// Create a request for a single test method
				Request request = requestTestMethod(klass, test.getTestMethodName());
				// Run the request and obtain the result.
				Result result = runTestMethod(request);
				// Store the result.
				results.add(result);
			} catch (ClassNotFoundException e) {
				// Class not found, store null result
				results.add(null);
			}
		}
		// Total result collected
		removeRunListener(totalResultListener);
		// Test run stopped
		fireTestRunFinished(totalResult);
		return results;
	}

	/**
	 * Create a Request that, when processed, will run a single test. This is
	 * done by filtering out all other tests. This method is used to support
	 * rerunning single tests.
	 * 
	 * @param clazz
	 *            The class of the test.
	 * @param methodName
	 *            The name of the test
	 * @return A Request that will cause a single test be run
	 */
	public static Request requestTestMethod(Class<?> clazz, String methodName) {
		return Request.method(clazz, methodName);
	}

	/**
	 * Runs all the tests contained in request.
	 * 
	 * @param request
	 *            The request describing tests.
	 * @return A {@link Result} describing the details of the test run and the
	 *         failed tests.
	 */
	public Result runTestMethod(Request request) {
		return getJUnitCore().run(request);
	}

	/**
	 * Adds a listener to be notified as the tests run.
	 * 
	 * @param runListener
	 *            The listener to add.
	 */
	public void addRunListener(RunListener runListener) {
		listeners.add(runListener);
	}

	/**
	 * Removes a listener which should no longer be notified as the tests run.
	 * 
	 * @param runListener
	 *            The listener to remove.
	 */
	public void removeRunListener(RunListener runListener) {
		listeners.remove(runListener);
	}

	/**
	 * Creates a description for the given tests.
	 * 
	 * @param name
	 *            The name for the returned description.
	 * @param tests
	 *            The tests to describe.
	 * @return A description describing all given tests.
	 */
	protected Description createDescription(String name, List<Test> tests) {
		Description description = Description.createSuiteDescription(name);
		for (Test test : tests) {
			Description testDescription = Description.createTestDescription(test.getTestClassName(),
					test.getTestMethodName());
			description.addChild(testDescription);
		}
		return description;
	}

	protected void fireTestRunStarted(Description description) throws Exception {
		for (RunListener listener : listeners) {
			listener.testRunStarted(description);
		}
	}

	protected void fireTestRunFinished(Result result) throws Exception {
		for (RunListener listener : listeners) {
			listener.testRunFinished(result);
		}
	}

	protected void fireTestStarted(Description description) throws Exception {
		for (RunListener listener : listeners) {
			listener.testStarted(description);
		}
	}

	protected void fireTestFinished(Description description) throws Exception {
		for (RunListener listener : listeners) {
			listener.testFinished(description);
		}
	}

	protected void fireTestFailure(Failure failure) throws Exception {
		for (RunListener listener : listeners) {
			listener.testFailure(failure);
		}
	}

	protected void fireTestAssumptionFailure(Failure failure) {
		for (RunListener listener : listeners) {
			listener.testAssumptionFailure(failure);
		}
	}

	protected void fireTestIgnored(Description description) throws Exception {
		for (RunListener listener : listeners) {
			listener.testIgnored(description);
		}
	}

	/**
	 * This {@link RunListener} forwards all calls to the registered listeners
	 * on the {@link TestRunner} expect for the
	 * {@link #testRunStarted(Description)} and {@link #testRunFinished(Result)}
	 * calls, as those are called by the {@link TestRunner} itself.
	 */
	protected class TestRunListener extends RunListener {

		@Override
		public void testRunStarted(Description description) throws Exception {
			// Ignore
		}

		@Override
		public void testRunFinished(Result result) throws Exception {
			// Ignore
		}

		@Override
		public void testStarted(Description description) throws Exception {
			fireTestStarted(description);
		}

		@Override
		public void testFinished(Description description) throws Exception {
			fireTestFinished(description);
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			fireTestFailure(failure);
		}

		@Override
		public void testAssumptionFailure(Failure failure) {
			fireTestAssumptionFailure(failure);
		}

		@Override
		public void testIgnored(Description description) throws Exception {
			fireTestIgnored(description);
		}

	}

}
