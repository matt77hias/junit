package kuleuven.group2.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStore implements Store {

	protected final Map<String, byte[]> store = new HashMap<String, byte[]>();

	protected final List<StoreListener> listeners = new ArrayList<StoreListener>();

	public boolean contains(String resourceName) {
		return store.containsKey(resourceName);
	}

	public byte[] read(String resourceName) {
		return store.get(resourceName);
	}

	public void write(String resourceName, byte[] contents) {
		boolean isChanged = contains(resourceName);
		store.put(resourceName, contents);
		if (isChanged) {
			fireChanged(resourceName);
		} else {
			fireAdded(resourceName);
		}
	}

	public void remove(String resourceName) {
		boolean isRemoved = contains(resourceName);
		store.remove(resourceName);
		if (isRemoved) {
			fireRemoved(resourceName);
		}
	}

	public void addStoreListener(StoreListener listener) {
		listeners.add(listener);
	}

	public void removeStoreListener(StoreListener listener) {
		listeners.remove(listener);
	}

	protected void fireAdded(String resourceName) {
		for (StoreListener listener : listeners) {
			listener.resourceAdded(resourceName);
		}
	}

	protected void fireChanged(String resourceName) {
		for (StoreListener listener : listeners) {
			listener.resourceChanged(resourceName);
		}
	}

	protected void fireRemoved(String resourceName) {
		for (StoreListener listener : listeners) {
			listener.resourceRemoved(resourceName);
		}
	}

}
