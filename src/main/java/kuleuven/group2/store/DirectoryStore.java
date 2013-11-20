package kuleuven.group2.store;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kuleuven.group2.filewatch.DirectoryWatchListener;
import kuleuven.group2.filewatch.DirectoryWatcher;
import kuleuven.group2.util.FileUtils;

public class DirectoryStore extends AbstractStore implements DirectoryWatchListener {

	protected final Path root;
	protected final DirectoryWatcher watcher;
	protected final ExecutorService executor;
	protected Future<?> watchTask;

	public DirectoryStore(Path root) throws IllegalArgumentException, IOException {
		if (root == null) {
			throw new IllegalArgumentException("Root must be effective.");
		}
		if (!Files.isDirectory(root)) {
			throw new IllegalArgumentException("Root must be a directory.");
		}
		// Make the root directory if it doesn't exit
		if (!Files.exists(root)) {
			Files.createDirectories(root);
		}
		this.root = root;
		this.watcher = new DirectoryWatcher(root);
		this.executor = Executors.newSingleThreadExecutor();
	}

	public DirectoryStore(String root) throws IllegalArgumentException, IOException {
		this(Paths.get(root));
	}

	@Override
	public boolean contains(String resourceName) {
		return Files.exists(getPath(resourceName));
	}

	@Override
	public Collection<String> getAll() {
		return getFiltered(StoreFilter.ALL);
	}

	@Override
	public Collection<String> getFiltered(final StoreFilter filter) {
		try {
			final List<String> resourceNames = new ArrayList<String>();
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
					if (isResource(path)) {
						String resourceName = getResourceName(path);
						if (filter == null || filter.accept(resourceName)) {
							resourceNames.add(resourceName);
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
			return resourceNames;
		} catch (IOException e) {
			// Ignore
		}
		return Collections.emptyList();
	}

	@Override
	public byte[] read(String resourceName) {
		try {
			// Read file as byte array
			return Files.readAllBytes(getPath(resourceName));
		} catch (IOException e) {
			// File not found or not readable
			return null;
		}
	}

	@Override
	public void write(String resourceName, byte[] contents) {
		try {
			// Write byte array to file
			Files.write(getPath(resourceName), contents);
		} catch (IOException e) {
			// File not writable
		}
	}

	@Override
	public void remove(String resourceName) {
		try {
			// Delete resource file
			Files.delete(getPath(resourceName));
		} catch (IOException e) {
			// File not writable
		}
	}

	@Override
	public void clear() {
		try {
			// Delete directory contents
			FileUtils.deleteRecursively(root, false);
		} catch (IOException e) {
			// Ignore
		}
	}

	@Override
	public boolean isListening() {
		return watchTask != null && !watchTask.isDone();
	}

	@Override
	public void startListening() {
		if (!isListening()) {
			watcher.addWatchListener(this);
			watchTask = executor.submit(new WatchTask());
		}
	}

	@Override
	public void stopListening() {
		if (isListening()) {
			watcher.removeWatchListener(this);
			watchTask.cancel(true);
			watchTask = null;
		}
	}

	protected boolean isResource(Path filePath) {
		return Files.isRegularFile(filePath);
	}

	protected Path getPath(String resourceName) {
		return root.resolve(resourceName);
	}

	protected String getResourceName(Path path) {
		return root.relativize(path).toString();
	}

	@Override
	public void fileCreated(Path path) {
		if (isResource(path)) fireAdded(getResourceName(path));
	}

	@Override
	public void fileModified(Path path) {
		if (isResource(path)) fireChanged(getResourceName(path));
	}

	@Override
	public void fileDeleted(Path path) {
		if (isResource(path)) fireRemoved(getResourceName(path));
	}

	protected class WatchTask implements Runnable {
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				watcher.processEvents();
			}
		}
	}

}
