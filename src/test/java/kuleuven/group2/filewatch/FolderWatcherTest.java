package kuleuven.group2.filewatch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kuleuven.group2.util.FileUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FolderWatcherTest {

	private static final Map<String, Set<Path>> registeredChanges = new HashMap<String, Set<Path>>();

	private static final int FILE_SYSTEM_TIMEOUT = 30;

	private static Path testFolder;
	private static Path testFile;
	private static Path testSubFolder;
	private static Path testSubFile;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testFolder = Files.createTempDirectory(FolderWatcherTest.class.getSimpleName());
		testFile = testFolder.resolve("helloworld.txt");
		testSubFolder = testFolder.resolve("testfolder");
		testSubFile = testSubFolder.resolve("helloworld.txt");
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		registeredChanges.put("modify", new HashSet<Path>());
		registeredChanges.put("create", new HashSet<Path>());
		registeredChanges.put("delete", new HashSet<Path>());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.deleteRecursively(testFolder, true);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);
	}

	@Before
	public void setUp() throws Exception {
		registeredChanges.get("modify").clear();
		registeredChanges.get("create").clear();
		registeredChanges.get("delete").clear();
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteRecursively(testFolder, false);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);
	}

	public class TestFolderWatchListener implements DirectoryWatchListener {

		public void fileModified(Path filePath) {
			registeredChanges.get("modify").add(filePath);
		}

		public void fileDeleted(Path filePath) {
			registeredChanges.get("delete").add(filePath);
		}

		public void fileCreated(Path filePath) {
			registeredChanges.get("create").add(filePath);
		}
	}

	@Test
	public void createFileTest() throws IOException, InterruptedException {
		// Setup
		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.createFile(testFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertCreated(testFile);
	}

	@Test
	public void modifyFileTest() throws IOException, InterruptedException {
		// Setup
		Files.createFile(testFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.write(testFile, "hello world".getBytes());
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertModified(testFile);
		// TODO [TEST] perhaps we want to test things that shouldn't have happened as
		// well?
	}

	@Test
	public void deleteFileTest() throws IOException, InterruptedException {
		// Setup
		Files.createFile(testFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.delete(testFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertDeleted(testFile);
	}

	@Test
	public void recursiveCreateTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.createFile(testSubFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertCreated(testSubFile);
	}

	@Test
	public void recursiveModifyTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);
		Files.createFile(testSubFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.write(testSubFile, "hello world".getBytes());
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertModified(testSubFile);
	}

	@Test
	public void recursiveDeleteTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);
		Files.createFile(testSubFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.delete(testSubFile);
		Thread.sleep(FILE_SYSTEM_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertDeleted(testSubFile);
	}

	private void assertModified(Path path) {
		assertTrue(registeredChanges.get("modify").contains(path));
	}

	private void assertDeleted(Path path) {
		assertTrue(registeredChanges.get("delete").contains(path));
	}

	private void assertCreated(Path path) {
		assertTrue(registeredChanges.get("create").contains(path));
	}

}
