package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kuleuven.group2.data.signature.JavaSignature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * A class that has keeps track of all {@link Test}s, {@link TestedMethod}s and
 * links between them for access by updaters and users.
 * 
 * @author Vital D'haveloose, Ruben Pieters, Mattias Buelens
 */
public class TestDatabase {

	protected final ConcurrentMap<String, ConcurrentMap<JavaSignature, TestedMethod>> methods = new ConcurrentHashMap<>();

	protected final ConcurrentMap<String, ConcurrentMap<String, Test>> tests = new ConcurrentHashMap<>();

	protected final Object linksLock = new Object();
	protected final Multimap<TestedMethod, Test> methodToTestLinks = HashMultimap.create();
	protected final Multimap<Test, TestedMethod> testToMethodLinks = HashMultimap.create();

	/*
	 * Methods
	 */

	public void addMethod(TestedMethod testedMethod) {
		JavaSignature signature = testedMethod.getSignature();
		getOrCreateMethodMap(signature.getFullClassName()).put(signature, testedMethod);
	}

	public void removeMethod(TestedMethod testedMethod) {
		JavaSignature signature = testedMethod.getSignature();
		synchronized (methods) {
			// Remove from method map
			ConcurrentMap<JavaSignature, TestedMethod> methodMap = getMethodMap(signature.getFullClassName());
			if (methodMap != null && methodMap.remove(signature, testedMethod) && methodMap.isEmpty()) {
				// Remove empty method map
				methods.remove(signature.getFullClassName(), methodMap);
			}
		}
	}

	public boolean containsMethod(JavaSignature signature) {
		Map<JavaSignature, TestedMethod> methodMap = getMethodMap(signature.getFullClassName());
		if (methodMap != null) {
			return methodMap.containsKey(signature);
		}
		return false;
	}

	public TestedMethod getMethod(JavaSignature signature) {
		Map<JavaSignature, TestedMethod> methodMap = getMethodMap(signature.getFullClassName());
		if (methodMap != null) {
			return methodMap.get(signature);
		}
		return null;
	}

	public TestedMethod getOrCreateMethod(JavaSignature signature) {
		TestedMethod newMethod = new TestedMethod(signature);
		TestedMethod oldMethod = getOrCreateMethodMap(signature.getFullClassName()).putIfAbsent(signature, newMethod);
		return oldMethod != null ? oldMethod : newMethod;
	}

	public Collection<TestedMethod> getMethodsIn(String className) {
		Map<JavaSignature, TestedMethod> methodMap = getMethodMap(className);
		if (methodMap == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(methodMap.values());
		}
	}

	protected ConcurrentMap<JavaSignature, TestedMethod> getMethodMap(String className) {
		return methods.get(className);
	}

	protected ConcurrentMap<JavaSignature, TestedMethod> getOrCreateMethodMap(String className) {
		ConcurrentMap<JavaSignature, TestedMethod> newMethodMap = new ConcurrentHashMap<>();
		ConcurrentMap<JavaSignature, TestedMethod> oldMethodMap = methods.putIfAbsent(className, newMethodMap);
		return oldMethodMap != null ? oldMethodMap : newMethodMap;
	}

	/*
	 * Tests
	 */

	public void addTest(Test test) {
		getOrCreateTestMap(test.getTestClassName()).put(test.getTestMethodName(), test);
	}

	public void removeTest(Test test) {
		synchronized (tests) {
			// Remove from test map
			ConcurrentMap<String, Test> testMap = getTestMap(test.getTestClassName());
			if (testMap != null && testMap.remove(test.getTestMethodName(), test) && testMap.isEmpty()) {
				// Remove empty test map
				tests.remove(test.getTestClassName(), testMap);
			}
		}
	}

	public boolean containsTest(String testClassName, String testMethodName) {
		Map<String, Test> testMap = getTestMap(testClassName);
		if (testMap != null) {
			return testMap.containsKey(testMethodName);
		}
		return false;
	}

	public Test getTest(String testClassName, String testMethodName) {
		Map<String, Test> testMap = getTestMap(testClassName);
		if (testMap != null) {
			return testMap.get(testMethodName);
		}
		return null;
	}

	public Test getOrCreateTest(String testClassName, String testMethodName) {
		Test newTest = new Test(testClassName, testMethodName);
		Test oldTest = getOrCreateTestMap(testClassName).putIfAbsent(testMethodName, newTest);
		return oldTest != null ? oldTest : newTest;
	}

	public Collection<Test> getAllTests() {
		Set<Test> allTests = new HashSet<>();
		for (Map.Entry<String, ConcurrentMap<String, Test>> entry : tests.entrySet()) {
			allTests.addAll(entry.getValue().values());
		}
		return allTests;
	}

	public Collection<Test> getTestsIn(String testClassName) {
		Map<String, Test> testMap = getTestMap(testClassName);
		if (testMap == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(testMap.values());
		}
	}

	protected ConcurrentMap<String, Test> getTestMap(String testClassName) {
		return tests.get(testClassName);
	}

	protected ConcurrentMap<String, Test> getOrCreateTestMap(String testClassName) {
		ConcurrentMap<String, Test> newTestMap = new ConcurrentHashMap<>();
		ConcurrentMap<String, Test> oldTestMap = tests.putIfAbsent(testClassName, newTestMap);
		return oldTestMap != null ? oldTestMap : newTestMap;
	}

	/*
	 * Test runs
	 */

	public void addTestRun(TestRun testRun, Test test) {
		addTestRun(testRun, test.getTestClassName(), test.getTestMethodName());
	}

	protected void addTestRun(TestRun testRun, String testClassName, String testMethodName) {
		getOrCreateTest(testClassName, testMethodName).addTestRun(testRun);
	}

	public List<TestRun> getAllTestRuns() {
		List<TestRun> testRuns = new ArrayList<TestRun>();
		for (Test test : getAllTests()) {
			testRuns.addAll(test.getTestRuns());
		}
		return testRuns;
	}

	/*
	 * Method-test links
	 */

	public void addMethodTestLink(TestedMethod testedMethod, Test test) {
		synchronized (linksLock) {
			methodToTestLinks.put(testedMethod, test);
			testToMethodLinks.put(test, testedMethod);
		}
	}

	public void clearMethodTestLinks() {
		synchronized (linksLock) {
			methodToTestLinks.clear();
			testToMethodLinks.clear();
		}
	}

	public boolean containsMethodTestLink(TestedMethod testedMethod, Test test) {
		return methodToTestLinks.containsEntry(testedMethod, test);
	}

	public Collection<TestedMethod> getLinkedMethods(Test test) {
		System.out.println(testToMethodLinks);
		Collection<TestedMethod> linkedMethods = testToMethodLinks.get(test);
		if (linkedMethods == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(linkedMethods);
		}
	}

	public Collection<Test> getLinkedTests(TestedMethod testedMethod) {
		Collection<Test> linkedTests = methodToTestLinks.get(testedMethod);
		if (linkedTests == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(linkedTests);
		}
	}

	public int getNbLinks() {
		return methodToTestLinks.size();
	}

}
