package kuleuven.group2.store;

/**
 * An event in a store, such as a deletion, add or change.
 * 
 * @author Group2
 * @version 11 November 2013
 */
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
