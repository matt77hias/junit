package kuleuven.group2.sourcehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.classloader.StoreClassLoader;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.sourcehandler.SourceEventHandler.Changes;
import kuleuven.group2.store.MemoryStore;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;
import kuleuven.group2.store.StoreListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SourceEventHandlerTest {
	
	protected Store classSourceStore;
	protected Store binaryStore;
	protected EclipseCompiler compiler;
	protected ClassLoader testClassLoader;
	protected TestDatabase testDatabase;
	
	protected TestStoreListener testStoreListener;
	protected List<StoreEvent> events;
	protected SourceEventHandler sourceEventHandler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		classSourceStore = new MemoryStore();
		binaryStore = new MemoryStore();
		testClassLoader = new StoreClassLoader(binaryStore);
		compiler = new EclipseCompiler(classSourceStore, binaryStore, testClassLoader);
		testDatabase = new TestDatabase();
		testStoreListener = new TestStoreListener();
		
		classSourceStore.addStoreListener(testStoreListener);
		
		events = new ArrayList<StoreEvent>();
		sourceEventHandler = new ClassSourceEventHandler(
				classSourceStore,
				binaryStore,
				testDatabase,
				testClassLoader);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	public class TestStoreListener implements StoreListener {

		@Override
		public void resourceAdded(String resourceName) {
			SourceEventHandlerTest.this.events.add(new StoreEvent(
					SourceEventHandlerTest.this.classSourceStore,
					resourceName,
					StoreEvent.Type.ADDED));
			
		}

		@Override
		public void resourceChanged(String resourceName) {
			SourceEventHandlerTest.this.events.add(new StoreEvent(
					SourceEventHandlerTest.this.classSourceStore,
					resourceName,
					StoreEvent.Type.CHANGED));
			
		}

		@Override
		public void resourceRemoved(String resourceName) {
			SourceEventHandlerTest.this.events.add(new StoreEvent(
					SourceEventHandlerTest.this.classSourceStore,
					resourceName,
					StoreEvent.Type.REMOVED));
		}
		
	}
	
	@Test
	public void testResourceAdded() {
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contents".getBytes());	
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertTrue(changes.getAddedResources().contains("res1"));
	}
	
	@Test
	public void testResourceChanged() {
		classSourceStore.write("res1", "contents".getBytes());

		classSourceStore.startListening();

		classSourceStore.write("res1", "contentsChanged".getBytes());
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertTrue(changes.getChangedResources().contains("res1"));
	}

	
	@Test
	public void testResourceRemoved() {
		classSourceStore.write("res1", "contents".getBytes());

		classSourceStore.startListening();

		classSourceStore.remove("res1");
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertTrue(changes.getRemovedResources().contains("res1"));
	}
	
	@Test
	public void testResourceAddedThenChanged() {
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contents".getBytes());	
		classSourceStore.write("res1", "contentsChanged".getBytes());	
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertTrue(changes.getAddedResources().contains("res1"));
		assertFalse(changes.getChangedResources().contains("res1"));
	}
	
	@Test
	public void testResourceAddedThenRemoved() {
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contents".getBytes());	
		classSourceStore.remove("res1");	
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertFalse(changes.getAddedResources().contains("res1"));
		assertFalse(changes.getChangedResources().contains("res1"));
		assertTrue(changes.getRemovedResources().contains("res1"));
	}
	
	@Test
	public void testResourceChangedThenRemoved() {
		classSourceStore.write("res1", "contents".getBytes());	
		
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contentsChanged".getBytes());	
		classSourceStore.remove("res1");	
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events, classSourceStore);
		
		assertFalse(changes.getAddedResources().contains("res1"));
		assertFalse(changes.getChangedResources().contains("res1"));
		assertTrue(changes.getRemovedResources().contains("res1"));
	}
	
	@Test
	public void testResourceAddedNoSourceStoreGiven() {
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contents".getBytes());	
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events);
		
		assertTrue(changes.getAddedResources().contains("res1"));
	}

	
	@Test
	public void testResourceAddedOrChangedSet() {
		classSourceStore.write("res1", "contents".getBytes());	
		classSourceStore.startListening();
		
		classSourceStore.write("res1", "contents".getBytes());	
		classSourceStore.write("res2", "contents".getBytes());
		
		classSourceStore.stopListening();
		
		Changes changes = sourceEventHandler.collectChanges(events);
		
		assertTrue(changes.getAddedOrChangedResources().contains("res1"));
		assertTrue(changes.getAddedOrChangedResources().contains("res2"));
	}
}
