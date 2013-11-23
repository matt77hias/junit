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
	public void test() throws Exception {
		String className = "ATest";
		String source =
				"import org.junit.Test;" + 
						"public class " + className + " {\n" +
						"@Test public boolean foo() { return true; }\n" +
						"}";

		classSourceStore.startListening();
		
		classSourceStore.write(className, source.getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.handleEvents(events);
		
		assertTrue(binaryStore.contains(NameUtils.toBinaryName(className)));
		
		Class<?> loadedClass = testClassLoader.loadClass(className);

		assertEquals(className, loadedClass.getName());

		for(Method method : loadedClass.getMethods()) {
			if (method.getName().equals("foo")) {
				// if this test fails, could also be problem with compiler
				assertNotNull(method.getAnnotation(Test.class));
			}
		}

		System.out.println(testDatabase.getAllTests());
	}

}
