package kuleuven.group2.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MemoryStore extends AbstractStore {

	protected final Map<String, byte[]> store = new HashMap<String, byte[]>();
	protected volatile boolean isListening = false;

	@Override
	public boolean contains(String resourceName) {
		return store.containsKey(resourceName);
	}

	@Override
	public Collection<String> getAll() {
		return Collections.unmodifiableCollection(store.keySet());
	}

	@Override
	public Collection<String> getFiltered(StoreFilter filter) {
		if (filter == null) return getAll();
		List<String> filtered = new ArrayList<String>();
		for (String resourceName : getAll()) {
			if (filter.accept(resourceName)) filtered.add(resourceName);
		}
		return filtered;
	}

	@Override
	public byte[] read(String resourceName) {
		return store.get(resourceName);
	}

	@Override
	public void write(String resourceName, byte[] contents) {
		boolean isChanged = contains(resourceName);
		store.put(resourceName, contents);
		if (isChanged) {
			fireChanged(resourceName);
		} else {
			fireAdded(resourceName);
		}
	}

	@Override
	public void remove(String resourceName) {
		boolean isRemoved = contains(resourceName);
		store.remove(resourceName);
		if (isRemoved) {
			fireRemoved(resourceName);
		}
	}

	@Override
	public void clear() {
		for (Iterator<String> it = store.keySet().iterator(); it.hasNext();) {
			String resourceName = it.next();
			it.remove();
			fireRemoved(resourceName);
		}
	}

	@Override
	public boolean isListening() {
		return isListening;
	}

	@Override
	public void startListening() {
		isListening = true;
	}

	@Override
	public void stopListening() {
		isListening = false;
	}

}
