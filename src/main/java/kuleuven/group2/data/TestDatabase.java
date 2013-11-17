package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.testrun.TestRun;

/**
 * A class that has keepstrack of all tests (Test), tested methods (TestedMethod) and
 * links between them (TestMethodLink) for access by updaters and users.
 * 
 * @author Vital D'haveloose, Ruben Pieters
 */
public class TestDatabase {

	// TODO: synchronized gebruiken?
	// TODO Use a Map with the signature as key?
	protected Collection<TestMethodLink> testMethodLinks =
			Collections.synchronizedCollection(new HashSet<TestMethodLink>());
	protected Set<TestedMethod> methods =
			Collections.synchronizedSet(new HashSet<TestedMethod>());
	protected Set<Test> tests =
			Collections.synchronizedSet(new HashSet<Test>());
	
	// METHODS
	
	protected TestedMethod getMethod(JavaSignature signature) {
		for (TestedMethod testedMethod : methods) {
			if (testedMethod.getSignature().equals(signature)) {
				return testedMethod;
			}
		}
		throw new IllegalArgumentException("Method with signature " + signature + " not in database.");
	}

	protected void addMethod(TestedMethod testedMethod) {
		methods.add(testedMethod);
	}

	protected void removeMethod(TestedMethod testedMethod) {
		methods.remove(testedMethod);
	}
	
	// TESTS
	
	protected Test getTest(String testClassName, String testMethodName) {
		for (Test test : tests) {
			if (test.equalName(testClassName, testMethodName)) {
				return test;
			}
		}
		return null;
	}

	protected void addTest(Test test) {
		tests.add(test);
	}

	protected void removeTest(Test test) {
		tests.remove(test);
	}

	public List<TestRun> getAllTestRuns() {
		List<TestRun> testRuns = new ArrayList<TestRun>();
		for (Test test : tests) {
			testRuns.addAll(test.getTestRuns());
		}
		return testRuns;
	}
	
	// TESTRUNS

	public List<Test> getLastFailedTests() {
		List<Test> lastFailedTests = new ArrayList<Test>(tests);
		
		Collections.sort(lastFailedTests, new Comparator<Test>() {
			@Override
			public int compare(Test o1, Test o2) {
				return - o1.getLastFailureTime().compareTo(o2.getLastFailureTime());
			}
		});
		
		return lastFailedTests;
	}

	public List<Test> getMostFailedTests(final int depth) {
		List<Test> lastFailedTests = new ArrayList<Test>(tests);
		
		Collections.sort(lastFailedTests, new Comparator<Test>() {
			@Override
			public int compare(Test o1, Test o2) {
				return - Float.compare(o1.getFailurePercentage(depth), o2.getFailurePercentage(depth));
			}
		});
		
		return lastFailedTests;
	}

	protected void addTestRun(TestRun testRun, String testClassName, String testMethodName) {
		Test test = getTest(testClassName, testMethodName);
		if (test == null) {
			test = new Test(testClassName, testMethodName);
			addTest(test);
		}
		test.addTestRun(testRun);
	}
	
	// METHOD-TEST LINKS

	public boolean containsMethodTestLink(TestedMethod testedMethod, Test test) {
		return testMethodLinks.contains(new TestMethodLink(testedMethod, test));
	}
	
	protected void addMethodTestLink(TestedMethod testedMethod, Test test) {
		testMethodLinks.add(new TestMethodLink(testedMethod, test));
	}
	
	protected void clearMethodLinks() {
		testMethodLinks.clear();
	}

	public Collection<TestedMethod> getLinkedMethods(Test test) {
		Collection<TestedMethod> linkedMethods = new HashSet<TestedMethod>();
		for(TestMethodLink testMethodLink : testMethodLinks) {
			if (testMethodLink.getTest().equals(test)) {
				linkedMethods.add(testMethodLink.getTestedMethod());
			}
		}
		return linkedMethods;
	}
	
	public Collection<Test> getLinkedTests(TestedMethod testedMethod) {
		Collection<Test> linkedTests = new HashSet<Test>();
		for(TestMethodLink testMethodLink : testMethodLinks) {
			if (testMethodLink.getTestedMethod().equals(testedMethod)) {
				linkedTests.add(testMethodLink.getTest());
			}
		}
		return linkedTests;
	}
	
	public void printMethodLinks() {
		for (TestMethodLink testMethodLink : testMethodLinks) {
			System.out.println(testMethodLink);
		}
	}

}
