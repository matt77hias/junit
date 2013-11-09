package kuleuven.group2.store;

import java.util.HashMap;
import java.util.Map;

public class MemoryStore implements Store {

	protected final Map<String, byte[]> store= new HashMap<String, byte[]>();

	public boolean contains(String resourceName) {
		return store.containsKey(resourceName);
	}

	public byte[] read(String resourceName) {
		return store.get(resourceName);
	}

	public void write(String resourceName, byte[] contents) {
		store.put(resourceName, contents);
	}

}
