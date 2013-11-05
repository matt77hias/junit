package kuleuven.group2.filewatch;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;


public class SourceWatcher implements FolderWatcherSubscriber {
	
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
    
	public void createEvent(Path filePath) {
		
	}

	public void modifyEvent(Path filePath) {
		
	}
	
	private void onModifiedFile(Path filePath) {
		getChangedMethodListForFile(filePath);
		
	}
	
	private void getChangedMethodListForFile(Path filePath) {
		
	}
	
	private void notifyChangedMethod() {
    	for (SourceWatcherSubscriber subscriber : subscriberList) {
    		subscriber.reportChangedMethod();
    	}
	}

	public void deleteEvent(Path filePath) {
		
	}


}
