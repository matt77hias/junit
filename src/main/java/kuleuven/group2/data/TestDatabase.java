package kuleuven.group2.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kuleuven.group2.data.signature.JavaSignature;

/**
 * A class that has access to all tests (Test) and tested methods (TestedMethod)
 * for access by updaters and users.
 * 
 * @author Vital D'haveloose
 */
public class TestDatabase {

	
	// TODO Use a Map with the signature as key?
	protected Collection<TestMethodLink> testMethodLinks =
			Collections.synchronizedCollection(new HashSet<TestMethodLink>());
	protected Set<TestedMethod> methods =
			Collections.synchronizedSet(new HashSet<TestedMethod>());
	protected Set<Test> tests =
			Collections.synchronizedSet(new HashSet<Test>());

	// TODO: synchronized gebruiken?
	
	// ACCESS

	
	// TODO: I'm not sure I agree with this approach,
	// why exactly do we need to get these references so people can do with it whatever they want?
	// shouldn't we provide methods for the update operations we want to support?
	// - ruben
	public Test getTest(String testClassName, String testMethodName) {
		for (Test test : tests) {
			if (test.equalName(testClassName, testMethodName)) {
				return test;
			}
		}
		return null;
	}

	public TestedMethod getMethod(JavaSignature signature) {
		for (TestedMethod testedMethod : methods) {
			if (testedMethod.getSignature().equals(signature)) {
				return testedMethod;
			}
		}
		throw new IllegalArgumentException("Method with signature " + signature + " not in database.");
	}
	
	

	public void addMethod(TestedMethod testedMethod) {
		methods.add(testedMethod);
	}

	public void removeMethod(TestedMethod testedMethod) {
		methods.remove(testedMethod);
	}
	
	public boolean containsMethodTestLink(TestedMethod testedMethod, Test test) {
		return testMethodLinks.contains(new TestMethodLink(testedMethod, test));
	}
	
	public void addMethodTestLink(TestedMethod testedMethod, Test test) {
		testMethodLinks.add(new TestMethodLink(testedMethod, test));
	}
	
	public Collection<TestedMethod> getLinkedMethods(Test test) {
		Collection<TestedMethod> linkedMethods = new HashSet<TestedMethod>();
		for(TestMethodLink testMethodLink : testMethodLinks) {
			if (testMethodLink.getTest().equalName(test.getTestClassName(), test.getTestMethodName())) {
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
	
	public void clearMethodLinks() {
		testMethodLinks.clear();
	}

	public void printMethodLinks() {
		for (TestMethodLink testMethodLink : testMethodLinks) {
			System.out.println(testMethodLink);
		}
	}
	
	protected Test getTestByName(String testClassName, String testMethodName) {
		for (Test test : tests) {
			if (test.equalName(testClassName, testMethodName)) {
				return test;
			}
		}
		throw new IllegalArgumentException("Test " + testClassName + " " + testMethodName + " does not exist.");
	}
	
	protected void addTest(Test test) {
		tests.add(test);
	}
	
	protected void removeTest(Test test) {
		tests.remove(test);
	}
	
	public void addTestRun(TestRun testRun, String testClassName, String testMethodName) {
		Test test = getTestByName(testClassName, testMethodName);
		if (test == null) {
			test = new Test(testClassName, testMethodName);
			addTest(test);
		}
		test.addTestRun(testRun);
	}

}
