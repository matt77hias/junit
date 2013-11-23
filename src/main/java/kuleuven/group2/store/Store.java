package kuleuven.group2.store;

import java.util.Collection;

/**
 * An interface that represents a resource container. Both source code files and binaries
 * are considered resources.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public interface Store {

	public boolean contains(String resourceName);

	public Collection<String> getAll();

	public Collection<String> getFiltered(StoreFilter filter);

	public byte[] read(String resourceName);

	public void write(String resourceName, byte[] contents);

	public void remove(String resourceName);

	public void clear();

	public boolean isListening();

	public void startListening();

	public void stopListening();

	public void addStoreListener(StoreListener listener);

	public void removeStoreListener(StoreListener listener);

}
