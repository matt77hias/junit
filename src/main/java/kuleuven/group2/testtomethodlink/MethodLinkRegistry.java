package kuleuven.group2.testtomethodlink;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MethodLinkRegistry {
	
	protected Map<Test, Collection<Method>> testToMethodLinks = new HashMap<Test, Collection<Method>>();

	public MethodLinkRegistry() {
		
	}
	
	public void registerTest(Test test) {
		if (isTestRegistered(test)) 
			throw new IllegalArgumentException("Test " + test + " already registered.");
		
		testToMethodLinks.put(test, new HashSet<Method>());
	}
	
	public void unregisterTest(Test test) {
		if (! isTestRegistered(test)) 
			throw new IllegalArgumentException("Test " + test + " is not registered.");
		
		testToMethodLinks.remove(test);
	}
	
	public boolean isTestRegistered(Test test) {
		return testToMethodLinks.containsKey(test);
	}
	
	public void addLink(Test test, Method method) {
		if (! isTestRegistered(test)) {
			registerTest(test);
		}
		
		Collection<Method> methodLinkList = testToMethodLinks.get(test);
		methodLinkList.add(method);
	}
	
	public void removeLink(Test test, Method method) {
		if (! isTestRegistered(test)) {
			return;
		}
		
		Collection<Method> methodLinkList = testToMethodLinks.get(test);
		methodLinkList.remove(method);
	}
	
	public boolean containsLink(Test test, Method method) {
		if (! isTestRegistered(test)) {
			return false;
		}

		Collection<Method> methodLinkList = testToMethodLinks.get(test);
		return methodLinkList.contains(method);
	}
	
	
	
}
