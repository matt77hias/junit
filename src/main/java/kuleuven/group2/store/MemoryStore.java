package kuleuven.group2.store;

import java.util.HashMap;
import java.util.Map;

public class MemoryStore extends AbstractStore {

	protected final Map<String, byte[]> store = new HashMap<String, byte[]>();

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

}
