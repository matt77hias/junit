package kuleuven.group2.filewatch;

import java.util.Collection;
import java.util.HashSet;

import kuleuven.group2.store.StoreListener;

/**
 * The SourceWatcher class notifies its subscribers of changed method lists in edited .java files.
 * 
 * @author Ruben
 *
 */
public class SourceWatcher implements StoreListener {
	
	protected Collection<SourceWatcherSubscriber> subscriberList = new HashSet<SourceWatcherSubscriber>();

    
    public void registerSubscriber(SourceWatcherSubscriber subscriber) {
    	subscriberList.add(subscriber);
    }
    
    public boolean isRegisteredAsSubscriber(SourceWatcherSubscriber subscriber) {
    	return subscriberList.contains(subscriber);
    }
    
    public void unregisterSubscriber(SourceWatcherSubscriber subscriber) {
    	if (isRegisteredAsSubscriber(subscriber)) {
    		subscriberList.remove(subscriber);
    	}
    }
    
    public int numberOfSubscribers() {
    	return subscriberList.size();
    }

	public void resourceAdded(String resourceName) {
		if (! interestedInResource(resourceName)) {
			return;
		}
	}

	public void resourceChanged(String resourceName) {
		if (! interestedInResource(resourceName)) {
			return;
		}
		
	}
	
	private void onModifiedResource(String resourceName) {
		getChangedMethodListForResource(resourceName);
		
	}
	
	private void getChangedMethodListForResource(String resourceName) {
		
	}
	
	private void notifyChangedMethod() {
    	for (SourceWatcherSubscriber subscriber : subscriberList) {
    		subscriber.reportChangedMethod();
    	}
	}

	public void resourceRemoved(String resourceName) {
		if (! interestedInResource(resourceName)) {
			return;
		}
		
	}
	
	private boolean interestedInResource(String resourceName) {
		return resourceName.endsWith(".java");
	}


}
