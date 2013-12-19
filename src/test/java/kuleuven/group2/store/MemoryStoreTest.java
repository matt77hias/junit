package kuleuven.group2.store;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryStoreTest {
	
	protected MemoryStore memoryStore;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		memoryStore = new MemoryStore();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testWriteAndRead() throws UnsupportedEncodingException {
		String fileA = "a.txt";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		
		byte[] contentBytes = memoryStore.read(fileA);
		String contentString = new String(contentBytes, "UTF-8");
		assertEquals(contents, contentString);
	}
	
	@Test
	public void testRemove() {
		String fileA = "a.txt";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		memoryStore.remove(fileA);
		
		assertFalse(memoryStore.contains(fileA));
	}
	
	@Test
	public void testGetFiltered() {
		String fileA = "a.txt";
		String fileB = "b.java";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		memoryStore.write(fileB, contents.getBytes());

		Collection<String> resources = memoryStore.getFiltered(StoreFilter.SOURCE);
		assertFalse(resources.contains(fileA));
		assertTrue(resources.contains(fileB));
	}
	
	@Test
	public void testGetAll() {
		String fileA = "a.txt";
		String fileB = "b.java";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		memoryStore.write(fileB, contents.getBytes());

		Collection<String> resources = memoryStore.getAll();
		assertTrue(resources.contains(fileA));
		assertTrue(resources.contains(fileB));
	}
	
	@Test
	public void testGetFilteredWithNullArgument() {
		String fileA = "a.txt";
		String fileB = "b.java";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		memoryStore.write(fileB, contents.getBytes());

		Collection<String> resources = memoryStore.getFiltered(null);
		assertTrue(resources.contains(fileA));
		assertTrue(resources.contains(fileB));
	}
	
	@Test
	public void testClear() {
		String fileA = "a.txt";
		String fileB = "b.java";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		memoryStore.write(fileB, contents.getBytes());

		memoryStore.clear();
		
		Collection<String> resources = memoryStore.getAll();
		assertFalse(resources.contains(fileA));
		assertFalse(resources.contains(fileB));
	}
	
	@Test
	public void testFireAdded() {
		String fileA = "a.txt";
		
		TestMemoryStoreListener testMemoryStoreListener = new TestMemoryStoreListener();
		memoryStore.addStoreListener(testMemoryStoreListener);
		
		memoryStore.startListening();
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		
		memoryStore.stopListening();
		
		assertTrue(testMemoryStoreListener.addedResources.contains(fileA));
	}
	
	@Test
	public void testFireRemoved() {
		String fileA = "a.txt";
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		
		TestMemoryStoreListener testMemoryStoreListener = new TestMemoryStoreListener();
		memoryStore.addStoreListener(testMemoryStoreListener);
		
		memoryStore.startListening();

		memoryStore.remove(fileA);
		
		memoryStore.stopListening();
		
		assertTrue(testMemoryStoreListener.removedResources.contains(fileA));
	}
	
	@Test
	public void testNoFireRemovedOnUnexistingResource() {
		String fileA = "a.txt";
		
		TestMemoryStoreListener testMemoryStoreListener = new TestMemoryStoreListener();
		memoryStore.addStoreListener(testMemoryStoreListener);
		
		memoryStore.startListening();
		
		memoryStore.remove(fileA);
		
		memoryStore.stopListening();
		
		assertFalse(testMemoryStoreListener.removedResources.contains(fileA));
	}
	
	@Test
	public void testFireChanged() {
		String fileA = "a.txt";
		
		TestMemoryStoreListener testMemoryStoreListener = new TestMemoryStoreListener();
		memoryStore.addStoreListener(testMemoryStoreListener);
		
		String contents = "hello world";
		memoryStore.write(fileA, contents.getBytes());
		
		memoryStore.startListening();
		
		memoryStore.write(fileA, contents.getBytes());
		
		memoryStore.stopListening();
		
		assertTrue(testMemoryStoreListener.changedResources.contains(fileA));
	}
	
	protected class TestMemoryStoreListener implements StoreListener {

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
