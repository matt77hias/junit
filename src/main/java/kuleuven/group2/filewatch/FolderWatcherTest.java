package kuleuven.group2.filewatch;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FolderWatcherTest {
	
	private static final Map<String, List<Path>> registeredChangeList = new HashMap<String, List<Path>>();

	private static final int FILEWATCHER_TIMEOUT = 30;
	private static final File MAIN_TEST_FOLDER = new File("D:\\filewatchertest");
	private static final File MAIN_TEST_FILE = new File("D:\\filewatchertest\\helloworld.txt");
	private static final File RECURSIVE_TEST_FOLDER = new File("D:\\filewatchertest\\testfolder");
	private static final File RECURSIVE_TEST_FILE = new File("D:\\filewatchertest\\testfolder\\helloworld.txt");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Files.createDirectory(Paths.get("D:\\filewatchertest"));
		
		registeredChangeList.put("modify", new ArrayList<Path>());
		registeredChangeList.put("create", new ArrayList<Path>());
		registeredChangeList.put("delete", new ArrayList<Path>());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		deleteFolder(MAIN_TEST_FOLDER);
		MAIN_TEST_FOLDER.delete();
	}

	@Before
	public void setUp() throws Exception {
		registeredChangeList.get("modify").clear();
		registeredChangeList.get("create").clear();
		registeredChangeList.get("delete").clear();
	}

	@After
	public void tearDown() throws Exception {
		deleteFolder(MAIN_TEST_FOLDER);
		Thread.sleep(FILEWATCHER_TIMEOUT);
	}
	
	private static void deleteFolder(File directory) throws IOException {
		for (File testFile : directory.listFiles()) {
			if (testFile.isDirectory()) {
				deleteFolder(testFile);
				Files.deleteIfExists(testFile.toPath());
			} else {
				//System.out.println("deleting " + testFile.toPath());
				Files.deleteIfExists(testFile.toPath());
			}
		}
	}

	public class TestFolderWatcherThread extends Thread {
		private boolean stopped = false;
		private FolderWatcher folderWatcher;
		
		public void stopFolderWatcher() {
			stopped = true;
		}
		
		public TestFolderWatcherThread(FolderWatcher folderWatcher) {
			this.folderWatcher = folderWatcher;
		}
		
		@Override
		public void run() {
			while (!stopped) {
				folderWatcher.processEvents();
			}
		}
	}
	
	public class TestFolderWatcherSubscriber implements FolderWatcherSubscriber {

		public void modifyEvent(Path filePath) {
			registeredChangeList.get("modify").add(filePath);
			System.out.println("modify " + filePath);
		}
		
		public void deleteEvent(Path filePath) {
			registeredChangeList.get("delete").add(filePath);
			System.out.println("delete " + filePath);
		}
		
		public void createEvent(Path filePath) {
			registeredChangeList.get("create").add(filePath);
			System.out.println("create " + filePath);
		}
	}
	
	@Test
	public void createFileTest() throws IOException, InterruptedException {
		// Setup
		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		Path filePath = MAIN_TEST_FILE.toPath();
		
		// Action
		folderWatcherThread.start();
		
		Files.createFile(filePath);
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		correctChangesRegisteredCreateModifyDelete(1, 0, 0);
	}
	
	@Test
	public void modifyFileTest() throws IOException, InterruptedException {
		// Setup
		Path filePath = MAIN_TEST_FILE.toPath();
		Files.createFile(filePath);
		
		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		
		// Action
		folderWatcherThread.start();
		
		FileWriter writer = new FileWriter(new File(filePath.toUri()));;
		try {
			writer.append("hello world");
		} finally {
			writer.close();
		}
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		assertTrue(atleastOneModifyChangeRegistered());
	}
	
	@Test
	public void deleteFileTest() throws IOException, InterruptedException {
		// Setup
		Path filePath = MAIN_TEST_FILE.toPath();
		Files.createFile(filePath);

		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		
		// Action
		folderWatcherThread.start();
		
		Files.delete(filePath);
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		correctChangesRegisteredCreateModifyDelete(0, 0, 1);
	}
	
	@Test
	public void recursiveCreateTest() throws IOException, InterruptedException {
		// Setup
		Path folderPath = RECURSIVE_TEST_FOLDER.toPath();
		Files.createDirectory(folderPath);
		
		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		
		// Action
		folderWatcherThread.start();
		
		Path filePath = RECURSIVE_TEST_FILE.toPath();
		Files.createFile(filePath);
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		
		// a modify entry is received on the recursive folder as well
		// -> when a file is created the folder is considered modified
		// this behavior is not present on the not recursive case because
		// in the not recursive case we are not watching the folder above the test folder
		correctChangesRegisteredCreateModifyDelete(1, 1, 0);
	}
	
	@Test
	public void recursiveModifyTest() throws IOException, InterruptedException {
		// Setup
		Path folderPath = RECURSIVE_TEST_FOLDER.toPath();
		Files.createDirectory(folderPath);
		
		Path filePath = RECURSIVE_TEST_FILE.toPath();
		Files.createFile(filePath);
		
		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		
		// Action
		folderWatcherThread.start();
		
		FileWriter writer = new FileWriter(new File(filePath.toUri()));;
		try {
			writer.append("hello world");
		} finally {
			writer.close();
		}
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		correctChangesRegisteredCreateModifyDelete(0, 2, 0);
	}
	
	@Test
	public void recursiveDeleteTest() throws IOException, InterruptedException {
		// Setup
		Path folderPath = RECURSIVE_TEST_FOLDER.toPath();
		Files.createDirectory(folderPath);
		
		Path filePath = RECURSIVE_TEST_FILE.toPath();
		Files.createFile(filePath);
		
		FolderWatcher folderWatcher = new FolderWatcher(MAIN_TEST_FOLDER.toPath());
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		
		// Action
		folderWatcherThread.start();
		
		Files.delete(filePath);
		
		Thread.sleep(FILEWATCHER_TIMEOUT);
		
		folderWatcherThread.stopFolderWatcher();
		folderWatcher.unregisterSubscriber(folderWatcherSubscriber);
		
		// Test
		correctChangesRegisteredCreateModifyDelete(0, 0, 1);
	}
	
	private void correctChangesRegisteredCreateModifyDelete(int amountOfCreates, int amountOfModifies, int amountOfDeletes) {
		assertEquals(amountOfCreates, registeredChangeList.get("create").size());
		//System.out.println("cr " + amountOfCreates + " " + registeredChangeList.get("create").size());
		assertEquals(amountOfModifies, registeredChangeList.get("modify").size());
		//System.out.println("mod " + amountOfModifies + " " + registeredChangeList.get("modify").size());
		assertEquals(amountOfDeletes, registeredChangeList.get("delete").size());
	}
	
	private boolean atleastOneModifyChangeRegistered() {
		return registeredChangeList.get("modify").size() >= 1;
	}

}
