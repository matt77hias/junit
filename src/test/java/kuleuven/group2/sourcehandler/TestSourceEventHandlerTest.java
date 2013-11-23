package kuleuven.group2.sourcehandler;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.classloader.StoreClassLoader;
import kuleuven.group2.compile.NameUtils;
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
	public void testHandleEventsOneTestClassOneTest() throws Exception {
		String className = "ATest";
		String source =
				"import org.junit.Test; \n" + 
						"public class " + className + " {\n" +
						"@Test\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		classSourceStore.startListening();
		
		classSourceStore.write(className, source.getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.handleEvents(events);

		assertNotNull(testDatabase.getTest(className, "foo"));
	}

	@Test
	public void testHandleEventsNoTestMethods() throws Exception {
		String className = "A";
		String source =
				"public class " + className + " {\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		classSourceStore.startListening();
		
		classSourceStore.write(className, source.getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.handleEvents(events);

		assertFalse(testDatabase.getAllTests().contains(new kuleuven.group2.data.Test("A", "initializationError")));
		assertFalse(testDatabase.getAllTests().contains(new kuleuven.group2.data.Test("A", "foo")));
	}

}
