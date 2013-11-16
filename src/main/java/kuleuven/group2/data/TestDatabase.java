package kuleuven.group2.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import kuleuven.group2.data.signature.JavaSignature;

/**
 * A class that has access to all tests (Test) and tested methods (TestedMethod)
 * for access by updaters and users.
 * 
 * @author Vital D'haveloose
 */
public class TestDatabase {

	// protected Set<Test> tests = Collections.synchronizedSet(new HashSet<Test>());
	
	// TODO Use a Map with the signature as key?
	protected Set<TestedMethod> methods = Collections.synchronizedSet(new HashSet<TestedMethod>());

	// TODO: synchronized gebruiken?
	
	// ACCESS

	public void addMethod(TestedMethod method) {
		methods.add(method);
	}

	public void removeMethod(TestedMethod method) {
		methods.remove(method);
	}

	public TestedMethod getMethod(JavaSignature signature) {
		for (TestedMethod method : methods) {
			if (method.getSignature().equals(signature)) return method;
		}
		return null;
	}
	
	public Test getTest(String className, String methodName) throws Exception {
		for(TestedMethod method : methods) {
			for(Test test : method.getTests()) {
				if(test.getTestClassName().equals(className) && test.getTestMethodName().equals(methodName)) return test;
			}
		}
		throw new Exception("There is no test in the database with class name " + className + " and method name " + methodName + " .");
	}
	
	public Set<TestedMethod> getLinkedMethods(Test test) {
		Set<TestedMethod> linkedMethods = new HashSet<TestedMethod>();
		for(TestedMethod method : methods) {
			if(method.isTestedBy(test)) linkedMethods.add(method);
		}
		return linkedMethods;
	}
	
	public void removeMethodLinks(Test test) {
		for(TestedMethod method : getLinkedMethods(test)) {
			method.removeTest(test);
		}
	}

	public void printMethodLinks() {
		Set<Test> tests;
		for (TestedMethod method : methods) {
			System.out.println(method);
			tests = new HashSet<Test>(method.getTests());
			for (Test test : tests) {
				System.out.println(test);
			}
		}
	}

}
