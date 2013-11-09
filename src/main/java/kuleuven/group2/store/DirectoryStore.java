package kuleuven.group2.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public class DirectoryStore implements Store {

	protected final Path root;

	public DirectoryStore(Path root) throws IllegalArgumentException,
			IOException {
		if (root == null) {
			throw new IllegalArgumentException("Root must be effective.");
		}
		if (!Files.isDirectory(root)) {
			throw new IllegalArgumentException("Root must be a directory.");
		}
		// Make the root directory if it doesn't exit
		if (!Files.exists(root)) {
			Files.createDirectory(root);
		}
		this.root= root;
	}

	public DirectoryStore(String root) throws IllegalArgumentException,
			IOException {
		this(Paths.get(root));
	}

	protected Path getPath(String resourceName) {
		return root.resolve(resourceName);
	}

	public boolean contains(String resourceName) {
		return Files.exists(getPath(resourceName));
	}

	public byte[] read(String resourceName) {
		InputStream is= null;
		try {
			// Read file as byte array
			is= Files.newInputStream(getPath(resourceName));
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			// File not found or not readable
			return null;
		} finally {
			// Close input stream
			IOUtils.closeQuietly(is);
		}
	}

	public void write(String resourceName, byte[] contents) {
		OutputStream os= null;
		try {
			// Read file as byte array
			os= Files.newOutputStream(getPath(resourceName));
			IOUtils.write(contents, os);
		} catch (IOException e) {
			// File not found or not writable
		} finally {
			// Close output stream
			IOUtils.closeQuietly(os);
		}
	}

}
