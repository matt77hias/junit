package kuleuven.group2.store;

public interface StoreListener {

	public void resourceAdded(String resourceName);

	public void resourceChanged(String resourceName);

	public void resourceRemoved(String resourceName);

}
