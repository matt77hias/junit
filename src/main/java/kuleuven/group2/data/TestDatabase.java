package kuleuven.group2.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A class that has access to all tests (Test) and tested methods (TestedMethod)
 * for access by updaters and users.
 * 
 * @author vital.dhaveloose
 */
public class TestDatabase {
	
	//TODO: lijst van tests
	//TODO: lijst van geteste methodes
	
	//TODO: implementatie van verschillende access-interfaces voor users
	
	//TODO implementeren, daarbij rekening houden met concurrency en uitbreidbaarheid

	//TODO: het volgende verwerken in nieuw model:
//	protected Map<TestedMethod, Collection<Test>> methodToTestLinks = new HashMap<TestedMethod, Collection<Test>>();
//	
//	public void registerMethod(TestedMethod method) {
//		if (isMethodRegistered(method)) 
//			throw new IllegalArgumentException("Method " + method + " already registered.");
//		
//		methodToTestLinks.put(method, new HashSet<Test>());
//	}
//	
//	public void unregisterTest(TestedMethod method) {
//		if (! isMethodRegistered(method)) 
//			throw new IllegalArgumentException("Method " + method + " is not registered.");
//		
//		methodToTestLinks.remove(method);
//	}
//	
//	public boolean isMethodRegistered(TestedMethod method) {
//		return methodToTestLinks.containsKey(method);
//	}
//	
//	public void addLink(TestedMethod method, Test test) {
//		if (! isMethodRegistered(method)) {
//			registerMethod(method);
//		}
//		
//		Collection<Test> testLinkList = methodToTestLinks.get(method);
//		testLinkList.add(test);
//	}
//	
//	public void removeLink(TestedMethod method, Test test) {
//		if (! isMethodRegistered(method)) {
//			return;
//		}
//
//		Collection<Test> testLinkList = methodToTestLinks.get(method);
//		testLinkList.remove(test);
//	}
//	
//	public boolean containsLink(TestedMethod method, Test test) {
//		if (! isMethodRegistered(method)) {
//			return false;
//		}
//
//		Collection<Test> testLinkList = methodToTestLinks.get(method);
//		return testLinkList.contains(test);
//	}
	
}
