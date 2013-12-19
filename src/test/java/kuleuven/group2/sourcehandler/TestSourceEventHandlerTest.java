package kuleuven.group2.sourcehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.Project;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
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

	protected Project project;
	protected TestDatabase testDatabase;
	protected Store testSourceStore;
	
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
		this.project = new Project(new MemoryStore(), new MemoryStore(), new MemoryStore());
		this.testDatabase = new TestDatabase();
		this.testSourceStore = this.project.getTestSourceStore();
		
		storeWatcher = new StoreWatcher(testSourceStore);
		testSourceStore.addStoreListener(storeWatcher);
        testConsumer = new TestConsumer();
        storeWatcher.registerConsumer(testConsumer);
		
		this.events = new ArrayList<StoreEvent>();
		this.sourceEventHandler = new TestSourceEventHandler(this.project, this.testDatabase);
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
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"import org.junit.Test; \n" + 
						"public class " + className + " {\n" +
						"@Test\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		testSourceStore.startListening();
		
		testSourceStore.write(sourceName, source.getBytes());
		
		testSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertNotNull(testDatabase.getTest(className, "foo"));
	}

	@Test
	public void testHandleEventsNoTestMethods() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		testSourceStore.startListening();
		
		testSourceStore.write(sourceName, source.getBytes());
		
		testSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertFalse(testDatabase.getAllTests().contains(new kuleuven.group2.data.Test("A", "initializationError")));
		assertFalse(testDatabase.getAllTests().contains(new kuleuven.group2.data.Test("A", "foo")));
	}
	
	@Test
	public void testSetup() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"import org.junit.Test; \n" + 
						"public class " + className + " {\n" +
						"@Test\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		testSourceStore.write(sourceName, source.getBytes());
		
		sourceEventHandler.setup();
		
		assertTrue(this.project.getBinaryStore().contains(NameUtils.toBinaryName(className)));
		/*
		 * constructor + foo
		 */
		assertEquals(1, testDatabase.getTestsIn(className).size());
	}

}
