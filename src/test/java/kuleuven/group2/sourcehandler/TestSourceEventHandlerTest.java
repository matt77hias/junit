package kuleuven.group2.sourcehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.classloader.StoreClassLoader;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.sourcehandler.SourceEventHandlerTest.TestStoreListener;
import kuleuven.group2.store.MemoryStore;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;
import kuleuven.group2.store.StoreWatcher;
import kuleuven.group2.util.Consumer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSourceEventHandlerTest {

	protected Store classSourceStore;
	protected Store binaryStore;
	protected ClassLoader testClassLoader;
	protected TestDatabase testDatabase;
	
	protected TestConsumer testConsumer;
	protected StoreWatcher storeWatcher;
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
		testDatabase = new TestDatabase();
		
		storeWatcher = new StoreWatcher(classSourceStore);
		classSourceStore.addStoreListener(storeWatcher);
		
		testConsumer = new TestConsumer();
		storeWatcher.registerConsumer(testConsumer);
		
		events = new ArrayList<StoreEvent>();
		sourceEventHandler = new TestSourceEventHandler(
				classSourceStore,
				binaryStore,
				testDatabase,
				testClassLoader);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public class TestConsumer implements Consumer<StoreEvent> {

		@Override
		public void consume(StoreEvent item) {
			TestSourceEventHandlerTest.this.events.add(item);
		}
		
	}
	

	@Test
	public void test() throws Exception {
		classSourceStore.startListening();
		
		classSourceStore.write("A", "public class A { @Test public void a() {} }".getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.handleEvents(events);
		
		System.out.println(testDatabase.getAllTests());
	}

}
