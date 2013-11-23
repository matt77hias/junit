package kuleuven.group2.store;

import java.util.ArrayList;
import java.util.List;

/**
 * A abstraction for resource containers, or {@link Store}s that implements common functionality
 * such as adding and removing listeners, and firing events.
 * 
 * @author Group2
 * @version 16 November 2013
 */
public abstract class AbstractStore implements Store {

	protected final List<StoreListener> listeners = new ArrayList<StoreListener>();

	public void addStoreListener(StoreListener listener) {
		listeners.add(listener);
	}

	public void removeStoreListener(StoreListener listener) {
		listeners.remove(listener);
	}

	protected void fireAdded(String resourceName) {
		if (isListening()) {
			for (StoreListener listener : listeners) {
				listener.resourceAdded(resourceName);
			}
		}
	}

	protected void fireChanged(String resourceName) {
		if (isListening()) {
			for (StoreListener listener : listeners) {
				listener.resourceChanged(resourceName);
			}
		}
	}

	protected void fireRemoved(String resourceName) {
		if (isListening()) {
			for (StoreListener listener : listeners) {
				listener.resourceRemoved(resourceName);
			}
		}
	}

}