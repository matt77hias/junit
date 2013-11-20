package kuleuven.group2.store;


public class StoreEvent {

	public final Store store;
	public final String resourceName;
	public final Type eventType;

	public StoreEvent(Store store, String resourceName, Type eventType) {
		this.store = store;
		this.resourceName = resourceName;
		this.eventType = eventType;
	}

	public Store getStore() {
		return store;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Type getType() {
		return eventType;
	}

	public enum Type {
		ADDED, CHANGED, REMOVED
	}

}
