package kuleuven.group2.store;

import java.util.ArrayList;
import java.util.List;

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