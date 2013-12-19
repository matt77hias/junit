package kuleuven.group2.sourcehandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.Project;
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
	
	protected Project project;
	protected TestDatabase testDatabase;
	protected Store classSourceStore;
	
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
		this.project = new Project(new MemoryStore(), new MemoryStore(), new MemoryStore());
		this.testDatabase = new TestDatabase();
		this.classSourceStore = this.project.getClassSourceStore();
		
		testStoreListener = new TestStoreListener();
        classSourceStore.addStoreListener(testStoreListener);
		
		this.events = new ArrayList<StoreEvent>();
		this.sourceEventHandler = new ClassSourceEventHandler(this.project, this.testDatabase);
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
