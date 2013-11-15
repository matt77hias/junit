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

	protected Set<Test> tests = Collections.synchronizedSet(new HashSet<Test>());
	
	// TODO Use a Map with the signature as key?
	protected Set<TestedMethod> methods = Collections.synchronizedSet(new HashSet<TestedMethod>());

	// TODO: synchronized gebruiken?

	// LAST FAILURE FIRST, FREQUENT FAILURE FIRST

	// TODO: methodes implementeren voor TestResultUpdater

	// DISTINCT FAILURE FIRST

	public void addMethodTestLink(Test currentRunningTest, TestedMethod enteredMethod) {
		enteredMethod.addTest(currentRunningTest);
		methods.add(enteredMethod);
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

	// CHANGED CODE FIRST

	// TODO: methodes implementeren voor LastChangeUpdater

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

}
