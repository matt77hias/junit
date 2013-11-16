package kuleuven.group2.filewatch;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.store.StoreFilter;
import kuleuven.group2.store.StoreListener;
import kuleuven.group2.util.Consumer;

/**
 * The SourceWatcher class notifies its consumers of changed source files.
 * 
 * @author Ruben, Mattias
 */
public class SourceWatcher implements StoreListener {

	protected StoreFilter filter = StoreFilter.SOURCE;

	protected final List<Consumer<SourceEvent>> consumers = new ArrayList<Consumer<SourceEvent>>();

	protected StoreFilter getFilter() {
		return filter;
	}

	protected void setFilter(StoreFilter filter) {
		this.filter = filter;
	}

	public void registerConsumer(Consumer<SourceEvent> consumer) {
		consumers.add(consumer);
	}

	public void unregisterConsumer(Consumer<SourceEvent> consumer) {
		consumers.remove(consumer);
	}

	@Override
	public void resourceAdded(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new SourceEvent(SourceEvent.Type.ADDED, resourceName));
		}
	}

	@Override
	public void resourceChanged(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new SourceEvent(SourceEvent.Type.CHANGED, resourceName));
		}
	}

	@Override
	public void resourceRemoved(String resourceName) {
		if (getFilter().accept(resourceName)) {
			fireSourceEvent(new SourceEvent(SourceEvent.Type.REMOVED, resourceName));
		}
	}

	protected void fireSourceEvent(SourceEvent event) {
		for (Consumer<SourceEvent> consumer : consumers) {
			consumer.consume(event);
		}
	}

}
