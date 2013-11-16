package kuleuven.group2.filewatch;

public class SourceEvent {

	public final Type eventType;
	public final String resourceName;

	public SourceEvent(Type eventType, String resourceName) {
		this.eventType = eventType;
		this.resourceName = resourceName;
	}

	public Type getType() {
		return eventType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public enum Type {
		ADDED, CHANGED, REMOVED
	}

}
