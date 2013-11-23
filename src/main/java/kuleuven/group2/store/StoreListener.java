package kuleuven.group2.store;

/**
 * An interface that should be implemented by classes interested in
 * events happening in a store, such as adds, changes and removals.
 * 
 * @author Group2
 * @version 11 November 2013
 */
public interface StoreListener {

	public void resourceAdded(String resourceName);

	public void resourceChanged(String resourceName);

	public void resourceRemoved(String resourceName);

}
