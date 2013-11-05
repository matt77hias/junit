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

	private static final int FILEWATCHER_TIMEOUT = 20;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Files.createDirectory(Paths.get("D:\\filewatchertest"));
		
		registeredChangeList.put("modify", new ArrayList<Path>());
		registeredChangeList.put("create", new ArrayList<Path>());
		registeredChangeList.put("delete", new ArrayList<Path>());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get("D:\\filewatchertest"));
	}

	@Before
	public void setUp() throws Exception {
		registeredChangeList.get("modify").clear();
		registeredChangeList.get("create").clear();
		registeredChangeList.get("delete").clear();
	}

	@After
	public void tearDown() throws Exception {
		Files.deleteIfExists(Paths.get("D:\\filewatchertest\\helloworld.txt"));
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
		}
		
		public void deleteEvent(Path filePath) {
			registeredChangeList.get("delete").add(filePath);
		}
		
		public void createEvent(Path filePath) {
			registeredChangeList.get("create").add(filePath);
			System.out.println("create " + filePath + this.toString());
		}
	}
	
	@Test
	public void createFileTest() throws IOException, InterruptedException {
		// Setup
		FolderWatcher folderWatcher = new FolderWatcher("D:\\filewatchertest");
		FolderWatcherSubscriber folderWatcherSubscriber = new TestFolderWatcherSubscriber();
		folderWatcher.registerSubscriber(folderWatcherSubscriber);
		TestFolderWatcherThread folderWatcherThread = new TestFolderWatcherThread(folderWatcher);
		Path filePath = Paths.get("D:\\filewatchertest\\helloworld.txt");
		
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
		Path filePath = Paths.get("D:\\filewatchertest\\helloworld.txt");
		Files.createFile(filePath);
		
		FolderWatcher folderWatcher = new FolderWatcher("D:\\filewatchertest");
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
		Path filePath = Paths.get("D:\\filewatchertest\\helloworld.txt");
		Files.createFile(filePath);
		
		FolderWatcher folderWatcher = new FolderWatcher("D:\\filewatchertest");
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
