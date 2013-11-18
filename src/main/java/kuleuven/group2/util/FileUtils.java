package kuleuven.group2.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public final class FileUtils {

	private FileUtils() {
	}

	public static void deleteRecursively(final Path path) throws IOException {
		deleteRecursively(path, true);
	}

	public static void deleteRecursively(final Path path, final boolean deleteSelf) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// Delete file
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				// Try to delete anyway
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (exc == null) {
					// Delete directory (unless self)
					if (deleteSelf || !dir.equals(path)) {
						Files.delete(dir);
					}
					return FileVisitResult.CONTINUE;
				} else {
					throw exc;
				}
			}
		});
	}
}
