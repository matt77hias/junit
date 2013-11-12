package kuleuven.group2.data.updating;

import java.util.Set;

import kuleuven.group2.data.TestedMethod;

/**
 * Updates the data when a method is changed. For this, the class implements a StoreListener.
 * @author vital.dhaveloose
 *
 */
public class LastChangeUpdater{
	
	//TODO: implements some kind of listener to know when methods are changed
	
	// ATTRIBUTES
	
	protected Set<TestedMethod> methods;
	
	// CONSTRUCTION
	
	public LastChangeUpdater(Set<TestedMethod> methods) {
		this.methods = methods;
	}

}
