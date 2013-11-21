package kuleuven.group2.filewatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kuleuven.group2.util.FileUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FolderWatcherTest {

	private static final Map<String, List<Path>> registeredChangeList = new HashMap<String, List<Path>>();

	private static final int FILEWATCHER_TIMEOUT = 30;

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

		registeredChangeList.put("modify", new ArrayList<Path>());
		registeredChangeList.put("create", new ArrayList<Path>());
		registeredChangeList.put("delete", new ArrayList<Path>());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FileUtils.deleteRecursively(testFolder, true);
	}

	@Before
	public void setUp() throws Exception {
		registeredChangeList.get("modify").clear();
		registeredChangeList.get("create").clear();
		registeredChangeList.get("delete").clear();
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteRecursively(testFolder, false);
		Thread.sleep(FILEWATCHER_TIMEOUT);
	}

	public class TestFolderWatchListener implements DirectoryWatchListener {

		public void fileModified(Path filePath) {
			registeredChangeList.get("modify").add(filePath);
		}

		public void fileDeleted(Path filePath) {
			registeredChangeList.get("delete").add(filePath);
		}

		public void fileCreated(Path filePath) {
			registeredChangeList.get("create").add(filePath);
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

		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		correctChangesRegisteredCreateModifyDelete(1, 0, 0);
	}

	@Test
	public void modifyFileTest() throws IOException, InterruptedException {
		// Setup
		Files.createFile(testFile);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.write(testFile, "hello world".getBytes());
		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		assertTrue(atleastOneModifyChangeRegistered());
	}

	@Test
	public void deleteFileTest() throws IOException, InterruptedException {
		// Setup
		Files.createFile(testFile);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.delete(testFile);

		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		correctChangesRegisteredCreateModifyDelete(0, 0, 1);
	}

	@Test
	public void recursiveCreateTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.createFile(testSubFile);

		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test

		// a modify entry is received on the recursive folder as well
		// -> when a file is created the folder is considered modified
		// this behavior is not present on the not recursive case because
		// in the not recursive case we are not watching the folder above the
		// test folder
		correctChangesRegisteredCreateModifyDelete(1, 1, 0);
	}

	@Test
	public void recursiveModifyTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);
		Files.createFile(testSubFile);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();

		Files.write(testSubFile, "hello world".getBytes());
		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		correctChangesRegisteredCreateModifyDelete(0, 2, 0);
	}

	@Test
	public void recursiveDeleteTest() throws IOException, InterruptedException {
		// Setup
		Files.createDirectory(testSubFolder);
		Files.createFile(testSubFile);

		DirectoryWatcher folderWatcher = new DirectoryWatcher(testFolder);
		DirectoryWatchListener watchListener = new TestFolderWatchListener();
		folderWatcher.addWatchListener(watchListener);

		// Action
		folderWatcher.startWatching();
		Files.delete(testSubFile);

		Thread.sleep(FILEWATCHER_TIMEOUT);

		folderWatcher.removeWatchListener(watchListener);
		folderWatcher.stopWatching();

		// Test
		correctChangesRegisteredCreateModifyDelete(0, 0, 1);
	}

	private void correctChangesRegisteredCreateModifyDelete(int amountOfCreates, int amountOfModifies,
			int amountOfDeletes) {
		assertEquals(amountOfCreates, registeredChangeList.get("create").size());
		// System.out.println("cr " + amountOfCreates + " " +
		// registeredChangeList.get("create").size());
		assertEquals(amountOfModifies, registeredChangeList.get("modify").size());
		// System.out.println("mod " + amountOfModifies + " " +
		// registeredChangeList.get("modify").size());
		assertEquals(amountOfDeletes, registeredChangeList.get("delete").size());
	}

	private boolean atleastOneModifyChangeRegistered() {
		return registeredChangeList.get("modify").size() >= 1;
	}

}
