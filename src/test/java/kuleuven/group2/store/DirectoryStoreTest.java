package kuleuven.group2.store;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kuleuven.group2.store.MemoryStoreTest.TestMemoryStoreListener;
import kuleuven.group2.util.FileUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectoryStoreTest {

	protected static Path root;
	protected DirectoryStore store;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		root = Files.createTempDirectory(DirectoryStoreTest.class.getSimpleName());
	}

	@Before
	public void setUp() throws IOException {
		store = new DirectoryStore(root);
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteRecursively(root, false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		FileUtils.deleteRecursively(root, true);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidRoot() throws IllegalArgumentException, IOException {
		new DirectoryStore("invalid*");
	}
	
	@Test
	public void getAllRecursive() throws IOException {
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
	
	@Test
	public void testGetFiltered() throws IOException {
		String fileA = "a.java";
		String fileB = "b.c";
		Path pathA = Paths.get(fileA);
		Path pathB = Paths.get(fileB);
		Path fullPathA = root.resolve(pathA);
		Path fullPathB = root.resolve(pathB);
		
		Files.write(fullPathA, Collections.singleton(fileA), Charset.defaultCharset());
		Files.write(fullPathB, Collections.singleton(fileB), Charset.defaultCharset());

		Collection<String> resources = store.getFiltered(StoreFilter.SOURCE);
		assertTrue(resources.contains(pathA.toString()));
		assertFalse(resources.contains(pathB.toString()));
	}
	
	@Test
	public void testWriteAndRead() throws IOException {
		String fileA = "a.txt";
		Path pathA = Paths.get(fileA);
		Path fullPathA = root.resolve(pathA);
		Files.write(fullPathA, Collections.singleton(fileA), Charset.defaultCharset());
		
		String contents = "hello world";
		store.write(pathA.toString(), contents.getBytes());
		
		byte[] contentBytes = store.read(pathA.toString());
		String contentString = new String(contentBytes, "UTF-8");
		assertEquals(contents, contentString);
	}
	
	@Test
	public void testNonExistingRoot () throws IllegalArgumentException, IOException {
		String newPath = root + "/doesnotexist";
		DirectoryStore directoryStore2 = new DirectoryStore(newPath);
	}

	
	@Test
	public void testFireAdded() {
		String fileA = "a.txt";
		
		TestDirectoryStoreListener testDirectoryStoreListener = new TestDirectoryStoreListener();
		store.addStoreListener(testDirectoryStoreListener);
		
		store.startListening();
		
		String contents = "hello world";
		store.write(fileA, contents.getBytes());
		
		store.stopListening();
		
		assertTrue(testDirectoryStoreListener.addedResources.contains(fileA));
	}
	
	@Test
	public void testFireRemoved() throws InterruptedException {
		String fileA = "a.txt";
		
		String contents = "hello world";
		store.write(fileA, contents.getBytes());
		
		TestDirectoryStoreListener testDirectoryStoreListener = new TestDirectoryStoreListener();
		store.addStoreListener(testDirectoryStoreListener);
		
		store.startListening();

		store.remove(fileA);
		
		store.stopListening();
		
		assertTrue(testDirectoryStoreListener.removedResources.contains(fileA));
	}
	
	@Test
	public void testNoFireRemovedOnUnexistingResource() {
		String fileA = "a.txt";
		
		TestDirectoryStoreListener testDirectoryStoreListener = new TestDirectoryStoreListener();
		store.addStoreListener(testDirectoryStoreListener);
		
		store.startListening();
		
		store.remove(fileA);
		
		store.stopListening();
		
		assertFalse(testDirectoryStoreListener.removedResources.contains(fileA));
	}
	
	@Test
	public void testFireChanged() {
		String fileA = "a.txt";
		
		TestDirectoryStoreListener testDirectoryStoreListener = new TestDirectoryStoreListener();
		store.addStoreListener(testDirectoryStoreListener);
		
		String contents = "hello world";
		store.write(fileA, contents.getBytes());
		
		store.startListening();
		
		store.write(fileA, contents.getBytes());
		
		store.stopListening();
		
		assertTrue(testDirectoryStoreListener.changedResources.contains(fileA));
	}
	
	protected class TestDirectoryStoreListener implements StoreListener {

		protected List<String> changedResources = new ArrayList<>();
		protected List<String> addedResources = new ArrayList<>();
		protected List<String> removedResources = new ArrayList<>();
		
		@Override
		public void resourceAdded(String resourceName) {
			addedResources.add(resourceName);
		}

		@Override
		public void resourceChanged(String resourceName) {
			changedResources.add(resourceName);
		}

		@Override
		public void resourceRemoved(String resourceName) {
			removedResources.add(resourceName);
		}
		
	}
}
