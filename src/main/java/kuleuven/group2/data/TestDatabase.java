package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.testrun.TestRun;

/**
 * A class that has keeps track of all tests (Test), tested methods
 * (TestedMethod) and links between them (TestMethodLink) for access by updaters
 * and users.
 * 
 * @author Vital D'haveloose, Ruben Pieters
 */
public class TestDatabase {

	// TODO: FASTER Use a Map with the signature as key?
	protected Collection<TestMethodLink> testMethodLinks = Collections
			.synchronizedCollection(new HashSet<TestMethodLink>());
	protected Set<TestedMethod> methods = Collections.synchronizedSet(new HashSet<TestedMethod>());
	protected Set<Test> tests = Collections.synchronizedSet(new HashSet<Test>());

	// METHODS

	public void addMethod(TestedMethod testedMethod) {
		methods.add(testedMethod);
	}

	public void removeMethod(TestedMethod testedMethod) {
		methods.remove(testedMethod);
	}
	
	public boolean containsMethod(JavaSignature signature) {
		for (TestedMethod testedMethod : methods) {
			if (testedMethod.getSignature().equals(signature)) {
				return true;
			}
		}
		return false;
		//TODO: FASTER: cache result
	}

	public TestedMethod getMethod(JavaSignature signature) {
		for (TestedMethod testedMethod : methods) {
			if (testedMethod.getSignature().equals(signature)) {
				return testedMethod;
			}
		}
		throw new IllegalArgumentException("Method with signature " + signature + " not in database.");
	}

	public Collection<TestedMethod> getMethodsIn(String className) {
		Set<TestedMethod> foundMethods = new HashSet<TestedMethod>();
		for(TestedMethod method : methods) {
			if( method.getClass().getSimpleName().equals(className) ) foundMethods.add(method);
		}
		return foundMethods;
	}

	// TESTS

	public void addTest(Test test) {
		tests.add(test);
	}

	public void removeTest(Test test) {
		tests.remove(test);
	}
	
	public Test getTest(String testClassName, String testMethodName) {
		for (Test test : tests) {
			if (test.equalName(testClassName, testMethodName)) {
				return test;
			}
		}
		return null;
	}

	public Collection<Test> getAllTests() {
		return tests;
	}

	public Collection<Test> getTestsIn(String testClassName) {
		Set<Test> foundTests = new HashSet<Test>();
		for(Test test : tests) {
			if( test.getTestClassName().equals(testClassName) ) foundTests.add(test);
		}
		return foundTests;
	}

	// TESTRUNS
	
	public void addTestRun(TestRun testRun, Test test) {
		addTestRun(testRun, test.getTestClassName(), test.getTestMethodName());
	}

	protected void addTestRun(TestRun testRun, String testClassName, String testMethodName) {
		Test test = getTest(testClassName, testMethodName);
		if (test == null) {
			test = new Test(testClassName, testMethodName);
			addTest(test);
		}
		test.addTestRun(testRun);
	}

	public List<TestRun> getAllTestRuns() {
		List<TestRun> testRuns = new ArrayList<TestRun>();
		for (Test test : tests) {
			testRuns.addAll(test.getTestRuns());
		}
		return testRuns;
	}

	// METHOD-TEST LINKS

	public void addMethodTestLink(TestedMethod testedMethod, Test test) {
		testMethodLinks.add(new TestMethodLink(testedMethod, test));
	}

	public void clearMethodLinks() {
		testMethodLinks.clear();
	}
	
	public boolean containsMethodTestLink(TestedMethod testedMethod, Test test) {
		return testMethodLinks.contains(new TestMethodLink(testedMethod, test));
	}

	public Collection<TestedMethod> getLinkedMethods(Test test) {
		Collection<TestedMethod> linkedMethods = new HashSet<TestedMethod>();
		for (TestMethodLink testMethodLink : testMethodLinks) {
			if (testMethodLink.getTest().equals(test)) {
				linkedMethods.add(testMethodLink.getTestedMethod());
			}
		}
		return linkedMethods;
	}

	public Collection<Test> getLinkedTests(TestedMethod testedMethod) {
		Collection<Test> linkedTests = new HashSet<Test>();
		for (TestMethodLink testMethodLink : testMethodLinks) {
			if (testMethodLink.getTestedMethod().equals(testedMethod)) {
				linkedTests.add(testMethodLink.getTest());
			}
		}
		return linkedTests;
	}

	public int getNbLinks() {
		return testMethodLinks.size();
	}

}
