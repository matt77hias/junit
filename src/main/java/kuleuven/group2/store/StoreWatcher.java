package kuleuven.group2.store;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.util.Consumer;

/**
 * A {@code StoreWatcher} listens and notifies its consumers of changed
 * resources. It also allows to filter the events using a {@link StoreFilter}.
 * 
 * <p>
 * This effectively transforms the Observer ({@link StoreListener}) into a
 * Producer, which is useful when the listener events should be retained for a
 * while before being processed.
 * </p>
 * 
 * @author Group2
 * @version 11 November 2013
 */
public class StoreWatcher implements StoreListener {

	protected final Store store;
	protected StoreFilter filter;
	protected final List<Consumer<StoreEvent>> consumers = new ArrayList<Consumer<StoreEvent>>();

	public StoreWatcher(Store store) {
		this(store, StoreFilter.ALL);
	}

	public StoreWatcher(Store store, StoreFilter filter) {
		this.store = store;
		setFilter(filter);
	}

	protected Store getStore() {
		return store;
	}

	public StoreFilter getFilter() {
		return filter;
	}

	public void setFilter(StoreFilter filter) {
		this.filter = filter;
	}

	public void registerConsumer(Consumer<StoreEvent> consumer) {
		consumers.add(consumer);
	}

	public void unregisterConsumer(Consumer<StoreEvent> consumer) {
		consumers.remove(consumer);
	}

	@Override
	public void resourceAdded(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new StoreEvent(getStore(), resourceName, StoreEvent.Type.ADDED));
		}
	}

	@Override
	public void resourceChanged(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new StoreEvent(getStore(), resourceName, StoreEvent.Type.CHANGED));
		}
	}

	@Override
	public void resourceRemoved(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new StoreEvent(getStore(), resourceName, StoreEvent.Type.REMOVED));
		}
	}

	protected void fireSourceEvent(StoreEvent event) {
		for (Consumer<StoreEvent> consumer : consumers) {
			try {
				consumer.consume(event);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
