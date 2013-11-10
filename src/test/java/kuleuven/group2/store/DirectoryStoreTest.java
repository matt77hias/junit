package kuleuven.group2.store;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import kuleuven.group2.util.FileUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectoryStoreTest {

	protected DirectoryStore store;
	protected Path root;

	@Before
	public void setup() throws IOException {
		root = Files.createTempDirectory(DirectoryStoreTest.class.getSimpleName());
		store = new DirectoryStore(root);
	}

	@After
	public void teardown() throws IOException {
		FileUtils.deleteRecursively(root);
	}

	@Test
	public void getAll_recursive() throws IOException {
		Path pathA = Paths.get("foo/bar/A");
		Path pathB = Paths.get("bar/baz/B");
		Path fullPathA = root.resolve(pathA);
		Path fullPathB = root.resolve(pathB);
		Files.createDirectories(fullPathA.getParent());
		Files.write(fullPathA, Collections.singleton("A"), Charset.defaultCharset());
		Files.createDirectories(fullPathB.getParent());
		Files.write(fullPathB, Collections.singleton("B"), Charset.defaultCharset());

		Collection<String> resources = store.getAll();
		assertTrue(resources.contains(pathA.toString()));
		assertTrue(resources.contains(pathB.toString()));
	}

}
