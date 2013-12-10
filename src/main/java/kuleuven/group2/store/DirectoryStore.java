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

import kuleuven.group2.filewatch.DirectoryWatchListener;
import kuleuven.group2.filewatch.DirectoryWatcher;
import kuleuven.group2.util.FileUtils;

/**
 * A store that has its contents on disk in a file system.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public class DirectoryStore extends AbstractStore implements DirectoryWatchListener {

	protected final Path root;
	protected final DirectoryWatcher watcher;

	public DirectoryStore(Path root) throws IllegalArgumentException, IOException {
		if (root == null) {
			throw new IllegalArgumentException("Root must be effective.");
		}
		// Make the root directory if it doesn't exit
		if (!Files.exists(root)) {
			Files.createDirectories(root);
		} else if (!Files.isDirectory(root)) {
			throw new IllegalArgumentException("Root must be a directory.");
		}
		this.root = root;
		this.watcher = new DirectoryWatcher(root);
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
			Path path = getPath(resourceName);
			Files.createDirectories(path.getParent());
			Files.write(getPath(resourceName), contents);
		} catch (IOException e) {
			// File not writable
			e.printStackTrace();
		}
	}

	@Override
	public void remove(String resourceName) {
		try {
			// Delete resource file
			Files.delete(getPath(resourceName));
		} catch (IOException e) {
			// File not writable
			e.printStackTrace();
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
		return watcher.isWatching();
	}

	@Override
	public void startListening() {
		if (!isListening()) {
			watcher.addWatchListener(this);
			try {
				watcher.startWatching();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stopListening() {
		if (isListening()) {
			watcher.removeWatchListener(this);
			try {
				watcher.stopWatching();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

}
