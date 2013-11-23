package kuleuven.group2.filewatch;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
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

/**
 * The DirectoryWatcher class notifies its listeners of the file system events
 * create, modify and delete.
 * 
 * @author Group2
 * @version 13 November 2013
 */
public class DirectoryWatcher {

	private final Map<WatchKey, Path> keys;
	private static final boolean recursiveWatching = true;

	protected final Collection<DirectoryWatchListener> listeners = new HashSet<DirectoryWatchListener>();
	protected Path watchedFolderPath;

	private WatchService watchService;
	protected Thread watchThread;

	public DirectoryWatcher(Path folderPath) {
		this.keys = new HashMap<WatchKey, Path>();
		this.watchedFolderPath = folderPath;
	}

	protected void createWatchService() throws IOException {
		this.watchService = FileSystems.getDefault().newWatchService();
		registerAllDirectoriesInPath(watchedFolderPath);
	}

	protected void registerAllDirectoriesInPath(final Path startingPath) throws IOException {
		Files.walkFileTree(startingPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path directoryPath, BasicFileAttributes attributes)
					throws IOException {
				registerDirectory(directoryPath);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	protected void registerDirectory(Path directoryPath) throws IOException {
		WatchKey key = directoryPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, directoryPath);
	}

	public boolean isWatching() {
		return watchService != null && watchThread != null && watchThread.isAlive();
	}

	public void startWatching() throws IOException {
		if (!isWatching()) {
			createWatchService();
			watchThread = new Thread(new WatchTask());
			watchThread.start();
		}
	}

	public void stopWatching() throws IOException {
		if (isWatching()) {
			watchService.close();
			watchService = null;
			try {
				watchThread.join();
			} catch (InterruptedException e) {
				// We got interrupted ourselves...
			} finally {
				watchThread = null;
			}
		}
	}

	public void addWatchListener(DirectoryWatchListener listener) {
		listeners.add(listener);
	}

	public void removeWatchListener(DirectoryWatchListener listener) {
		listeners.remove(listener);
	}

	protected boolean processEvent() {
		WatchKey key;
		try {
			key = waitTillNextKey();
		} catch (ClosedWatchServiceException e) {
			// Closed
			return false;
		} catch (InterruptedException e) {
			// Killed
			return false;
		}

		assert isRegisteredKey(key);
		handleEventsInKey(key);

		boolean accessible = checkIfDirectoryIsAccessible(key);
		if (!accessible) {
			removeKeyFromRegisteredKeys(key);

			// all directories are inaccessible
			// if (keys.isEmpty()) return false;
		}

		return true;
	}

	protected void processEvents() {
		while (true) {
			if (!processEvent()) break;
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
		if (!keys.containsKey(key)) return false;
		if (keys.get(key) == null) return false;
		return true;
	}

	private void handleEventsInKey(WatchKey key) {
		Path directoryPath = keys.get(key);

		for (WatchEvent<?> event : key.pollEvents()) {
			if (isOverflowEvent(event)) {
				continue;
			}

			// Since the event is not an overflow event we assume the event is a
			// WatchEvent<Path> event
			@SuppressWarnings("unchecked")
			WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

			Path entryFileName = pathEvent.context();
			Path entryFullName = directoryPath.resolve(entryFileName);

			notifyListeners(pathEvent, entryFullName);

			if (isCreateEvent(event) && recursiveWatching) {
				registerSubDirectories(entryFullName);
			}
		}
	}

	private void notifyListeners(WatchEvent<?> event, Path filePath) {
		for (DirectoryWatchListener listener : listeners) {
			if (isCreateEvent(event)) {
				listener.fileCreated(filePath);
			} else if (isModifyEvent(event)) {
				listener.fileModified(filePath);
			} else if (isDeleteEvent(event)) {
				listener.fileDeleted(filePath);
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

	protected class WatchTask implements Runnable {
		@Override
		public void run() {
			processEvents();
		}
	}

}
