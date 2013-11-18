package kuleuven.group2.sourcehandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;

import com.google.common.collect.Sets;

public abstract class SourceEventHandler {

	public abstract void handleEvents(List<StoreEvent> events) throws Exception;

	protected Changes collectChanges(List<StoreEvent> events) {
		return collectChanges(events, null);
	}

	protected Changes collectChanges(List<StoreEvent> events, Store owningStore) {
		Set<String> added = new HashSet<String>();
		Set<String> changed = new HashSet<String>();
		Set<String> removed = new HashSet<String>();
		for (StoreEvent event : events) {
			if (owningStore != null && event.getStore().equals(owningStore)) {
				String name = event.getResourceName();
				switch (event.getType()) {
				case ADDED:
					removed.remove(name);
					changed.remove(name);
					added.add(name);
					break;
				case CHANGED:
					removed.remove(name);
					if (!added.contains(name)) {
						changed.add(name);
					}
					break;
				case REMOVED:
					removed.add(name);
					added.remove(name);
					changed.remove(name);
					break;
				}
			}
		}
		return new Changes(added, changed, removed);
	}

	protected static class Changes {

		protected final Set<String> added;
		protected final Set<String> changed;
		protected final Set<String> removed;

		public Changes(Set<String> added, Set<String> changed, Set<String> removed) {
			this.added = added;
			this.changed = changed;
			this.removed = removed;
		}

		public Set<String> getAddedResources() {
			return added;
		}

		public Set<String> getChangedResources() {
			return changed;
		}

		public Set<String> getAddedOrChangedResources() {
			return Sets.union(getAddedResources(), getChangedResources());
		}

		public Set<String> getRemovedResources() {
			return removed;
		}

	}

}
