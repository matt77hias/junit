package kuleuven.group2;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FolderWatcher {

    private final WatchService watchService;
    private final Map<WatchKey,Path> keys;
    private static final boolean recursiveWatching = true;
    
    protected Collection<FolderWatcherSubscriber> subscriberList = new HashSet<FolderWatcherSubscriber>();
	protected Path watchedFolderPath;
	
	public FolderWatcher(String folderPathName) throws IOException {
		this.watchedFolderPath = Paths.get(folderPathName);
        this.watchService = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();

        registerAllDirectoriesInPath(watchedFolderPath);
	}

    private void registerAllDirectoriesInPath(final Path startingPath) throws IOException {
        Files.walkFileTree(startingPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directoryPath, BasicFileAttributes attributes)
                throws IOException
            {
                registerDirectory(directoryPath);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void registerDirectory(Path directoryPath) throws IOException {
        WatchKey key = directoryPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, directoryPath);
    }
    
    public void registerSubscriber(FolderWatcherSubscriber subscriber) {
    	subscriberList.add(subscriber);
    }
    
    public boolean isRegisteredAsSubscriber(FolderWatcherSubscriber subscriber) {
    	return subscriberList.contains(subscriber);
    }
    
    public void unregisterSubscriber(FolderWatcherSubscriber subscriber) {
    	if (isRegisteredAsSubscriber(subscriber)) {
    		subscriberList.remove(subscriber);
    	}
    }
    
    public int numberOfSubscribers() {
    	return subscriberList.size();
    }

    public void processEvents() {
        for (;;) {
            WatchKey key;
            try {
                key = waitTillNextKey();
            } catch (InterruptedException e) {
                return;
            }

            if (! isRegisteredKey(key)) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            handleEventsInKey(key);

            boolean accessible = checkIfDirectoryIsAccessible(key);
            if (!accessible) {
                removeKeyFromRegisteredKeys(key);

                // all directories are inaccessible
                /*if (keys.isEmpty()) {
                    break;
                }*/
            }
        }
    }
    
    private boolean checkIfDirectoryIsAccessible(WatchKey key) {
    	return key.reset();
    }
    
    private void removeKeyFromRegisteredKeys(WatchKey key) {
    	keys.remove(key);
    }
    
    private WatchKey waitTillNextKey() throws InterruptedException {
        return watchService.take();
    }
    
    private boolean isRegisteredKey(WatchKey key) {
    	if (! keys.containsKey(key))
    		return false;
    	if (keys.get(key) == null)
    		return false;
    	return true;
    }

	private void handleEventsInKey(WatchKey key) {
		Path directoryPath = keys.get(key);
		
		for (WatchEvent<?> event: key.pollEvents()) {
		    if (isOverflowEvent(event)) {
		        continue;
		    }
		    
		    // Since the event is not an overflow event we assume the event is a
		    // WatchEvent<Path> event
		    @SuppressWarnings("unchecked")
			WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

		    // Context for directory entry event is the file name of entry;
		    Path name = pathEvent.context();
		    Path fullName = directoryPath.resolve(name);
		    
		    notifySubscribers(pathEvent, fullName);

		    if (isCreateEvent(event) && recursiveWatching) {
		    	registerSubDirectories(fullName);
		    }
		}
	}
    
    private void notifySubscribers(WatchEvent<?> event, Path filePath) {
    	for (FolderWatcherSubscriber subscriber : subscriberList) {
	        if (isCreateEvent(event)) {
	        	subscriber.createEvent(filePath);
	        } else if (isModifyEvent(event)) {
	        	subscriber.modifyEvent(filePath);
	        } else if (isDeleteEvent(event)) {
	        	subscriber.deleteEvent(filePath);
	        }
	    }
    }
    
    private void registerSubDirectories(Path child) {
        try {
            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                registerAllDirectoriesInPath(child);
            }
        } catch (IOException e) {
        }
    }
    

    
    private boolean isOverflowEvent(WatchEvent<?> event) {
        return eventIsStandardEventKind(event, OVERFLOW);
    }
    
    private boolean isCreateEvent(WatchEvent<?> event) {
        return eventIsStandardEventKind(event, ENTRY_CREATE);
    }
    
    private boolean isModifyEvent(WatchEvent<?> event) {
        return eventIsStandardEventKind(event, ENTRY_MODIFY);
    }
    
    private boolean isDeleteEvent(WatchEvent<?> event) {
        return eventIsStandardEventKind(event, ENTRY_DELETE);
    }
    
    private boolean eventIsStandardEventKind(WatchEvent<?> event, Kind<?> standardEventKind) {
        WatchEvent.Kind<?> eventKind = event.kind();
        return eventKind == standardEventKind;
    }
}
