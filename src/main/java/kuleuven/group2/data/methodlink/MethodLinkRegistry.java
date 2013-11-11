package kuleuven.group2.data.methodlink;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Keeps track of the links between (tested) methods and tests as a unidirectional, oneToMany relation.
 * @author vital.dhaveloose
 *
 */
public class MethodLinkRegistry {
	
	protected Map<Method, Collection<Test>> methodToTestLinks = new HashMap<Method, Collection<Test>>();

	public MethodLinkRegistry() {
		
	}
	
	public void registerMethod(Method method) {
		if (isMethodRegistered(method)) 
			throw new IllegalArgumentException("Method " + method + " already registered.");
		
		methodToTestLinks.put(method, new HashSet<Test>());
	}
	
	public void unregisterTest(Method method) {
		if (! isMethodRegistered(method)) 
			throw new IllegalArgumentException("Method " + method + " is not registered.");
		
		methodToTestLinks.remove(method);
	}
	
	public boolean isMethodRegistered(Method method) {
		return methodToTestLinks.containsKey(method);
	}
	
	public void addLink(Method method, Test test) {
		if (! isMethodRegistered(method)) {
			registerMethod(method);
		}
		
		Collection<Test> testLinkList = methodToTestLinks.get(method);
		testLinkList.add(test);
	}
	
	public void removeLink(Method method, Test test) {
		if (! isMethodRegistered(method)) {
			return;
		}

		Collection<Test> testLinkList = methodToTestLinks.get(method);
		testLinkList.remove(test);
	}
	
	public boolean containsLink(Method method, Test test) {
		if (! isMethodRegistered(method)) {
			return false;
		}

		Collection<Test> testLinkList = methodToTestLinks.get(method);
		return testLinkList.contains(test);
	}
	
	
	
}
