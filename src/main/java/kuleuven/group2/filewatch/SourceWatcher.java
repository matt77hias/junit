package kuleuven.group2.filewatch;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.store.StoreListener;

/**
 * The SourceWatcher class notifies its subscribers of changed method lists in
 * edited .java files.
 * 
 * @author Ruben
 * 
 */
public class SourceWatcher implements StoreListener {

	protected final List<SourceWatchListener> listeners = new ArrayList<SourceWatchListener>();

	public void registerSubscriber(SourceWatchListener listener) {
		listeners.add(listener);
	}

	public void unregisterSubscriber(SourceWatchListener listener) {
		listeners.remove(listener);
	}

	public void resourceAdded(String resourceName) {
		if (!interestedInResource(resourceName)) {
			return;
		}
	}

	public void resourceChanged(String resourceName) {
		if (!interestedInResource(resourceName)) {
			return;
		}

	}

	private void onModifiedResource(String resourceName) {
		getChangedMethodListForResource(resourceName);

	}

	private void getChangedMethodListForResource(String resourceName) {

	}

	private void notifyChangedMethod() {
		for (SourceWatchListener listener : listeners) {
			listener.reportChangedMethod();
		}
	}

	public void resourceRemoved(String resourceName) {
		if (!interestedInResource(resourceName)) {
			return;
		}

	}

	private boolean interestedInResource(String resourceName) {
		return resourceName.endsWith(".java");
	}

}
