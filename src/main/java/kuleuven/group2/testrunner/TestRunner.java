package kuleuven.group2.testrunner;

import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import kuleuven.group2.data.Test;

/**
 * A class of test runners used for running single test methods
 * in a predefined order. This only violates the JUnit's fixed
 * order execution. Set-ups and tear-downs of test methods and
 * test fixture are executed properly.
 * 
 * @author	Group 2
 * @version	16 November 2013
 */
public class TestRunner {
	
	/**
	 * Creates a new test runner with no class loader.
	 */
	public TestRunner() {
		this(null);
	}
	
	/**
	 * Creates a new test runner with the given class loader
	 * as its class loader.
	 * 
	 * @param	classLoader
	 * 			The new class loader of this test runner.
	 */
	public TestRunner(ClassLoader classLoader) {
		this(classLoader, new JUnitCore());
	}
	
	/**
	 * Creates a new test runner with the given class loader
	 * as its class loader.
	 * 
	 * @param	classLoader
	 * 			The new class loader of this test runner.
	 * @param	jUnitCore
	 * 			The JUnitCore for this test runner.
	 * 			You can give your own JUnitCore so
	 * 			that you don't need to regsiter to
	 * 			another JUnitCore.
	 */
	public TestRunner(ClassLoader classLoader, JUnitCore jUnitCore) {
		this.classLoader = classLoader;
		this.junitCore = jUnitCore;
	}
	
	/**
	 * Returns the class loader of this test runner.
	 * 
	 * @return	The class loader of this test runner.
	 */
	public ClassLoader getClassLoader() {
		return this.classLoader;
	}
	
	/**
	 * Sets the class loader of this test runner
	 * to the given class loader.
	 * 
	 * @param	classLoader
	 * 			The class loader that has the class
	 * 			loader of this test runner to be set to.
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * The class loader of this test runner.
	 */
	private ClassLoader classLoader;
	
//	/**
//	 * Returns the default test runner.
//	 */
//	public static TestRunner getDefaultRunner() {
//		return DEFAULT_RUNNER;
//	}
//	
//	/**
//	 * The default runner used for running tests.
//	 * In this way object creation can be decreased.
//	 */
//	private static final TestRunner DEFAULT_RUNNER = new TestRunner();
//	
//	/**
//	 * Runs the given tests. This means that all the methods referred to
//	 * in the Test objects will be ran separately.
//	 * 
//	 * @param	ClassLoader
//	 * 			The class loader needed to obtain the class references.
//	 * @param	tests
//	 * 			The tests that has to be ran.
//	 */
//	public static Result[] runTests(ClassLoader classLoader, Test[] tests) {
//		return DEFAULT_RUNNER.runTestMethods(classLoader, tests);
//	}
	
	/**
	 * Runs the given tests. This means that all the methods referred to
	 * in the Test objects will be ran separately.
	 * @param	tests
	 * 			The tests that has to be ran.
	 */
	public Result[] runTestMethods(List<Test> tests) {
		return runTestMethods(getClassLoader(), tests.toArray(new Test[tests.size()]));
	}
	
	/**
	 * Runs the given tests. This means that all the methods referred to
	 * in the Test objects will be ran separately.
	 * 
	 * @param	ClassLoader
	 * 			The class loader needed to obtain the class references.
	 * @param	tests
	 * 			The tests that has to be ran.
	 */
	public Result[] runTestMethods(Test... tests) {
		return runTestMethods(getClassLoader(), tests);
	}
	
	/**
	 * Runs the given tests. This means that all the methods referred to
	 * in the Test objects will be ran separately.
	 * 
	 * If class cannot be found, a null reference is stored.
	 * 
	 * @param	ClassLoader
	 * 			The class loader needed to obtain the class references.
	 * @param	tests
	 * 			The tests that has to be ran.
	 */
	public Result[] runTestMethods(ClassLoader classLoader, Test[] tests) {
		Result[] results = new Result[tests.length];
		for (int i=0; i<tests.length; i++) {
			try {
				// Obtain the class with the specified binary name in the test object.
				Class<?> klass = classLoader.loadClass(tests[i].getTestClassName());
				// Create a request for a single test method
				Request request = requestTestMethod(klass, tests[i].getTestMethodName());
				// Run the request and obtain the result.
				Result result = runTestMethod(request);
				// Store the result.
				results[i] = result;
			} catch (ClassNotFoundException e) {
				// If class cannot be found, a null reference is stored.
				// results[i] == null;
			}
		}
		return results;
	}
	
	/**
	 * The JUnitCore object (facade) used for running tests.
	 */
	private JUnitCore junitCore;
	
	/**
	 * Create a Request that, when processed, will run a single test.
	 * This is done by filtering out all other tests.
	 * This method is used to support rerunning single tests.
	 * 
	 * @param	clazz
	 * 			The class of the test.
	 * @param	methodName
	 * 			The name of the test
	 * @return	A Request that will cause a single test be run
	 */
	public static Request requestTestMethod(Class<?> clazz, String methodName) {
		return Request.method(clazz, methodName);
	}
	
	/**
	 * Returns the JUnitCore of this test runner.
	 */
	protected JUnitCore getJUnitCore() {
		return this.junitCore;
	}
	
	/**
	 * Runs all the tests contained in request.
	 * 
	 * @param	request
	 * 			The request describing tests.
	 * @return	A Result describing the details of
	 * 			the test run and the failed tests.
	 */
	public Result runTestMethod(Request request) {
		return getJUnitCore().run(request);
	}
	
	/**
	 * Adds a listener to be notified as the tests run.
	 * 
	 * @param	runListener
	 * 			The listener to add.
	 */
	public void addRunListener(RunListener runListener) {
		getJUnitCore().addListener(runListener);
	}
}
