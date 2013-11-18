package kuleuven.group2.store;

import java.util.Collection;

public interface Store {

	public boolean contains(String resourceName);

	public Collection<String> getAll();

	public Collection<String> getFiltered(StoreFilter filter);

	public byte[] read(String resourceName);

	public void write(String resourceName, byte[] contents);

	public void remove(String resourceName);

	public void clear();

	public void addStoreListener(StoreListener listener);

	public void removeStoreListener(StoreListener listener);

}
