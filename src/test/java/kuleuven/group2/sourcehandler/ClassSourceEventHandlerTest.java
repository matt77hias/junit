package kuleuven.group2.sourcehandler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.Project;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.signature.JavaSignatureParser;
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

public class ClassSourceEventHandlerTest {
	
	protected Project project;
	protected TestDatabase testDatabase;
	protected Store classSourceStore;
	
	protected TestConsumer testConsumer;
	protected List<StoreEvent> events;
	protected SourceEventHandler sourceEventHandler;
	private StoreWatcher storeWatcher;

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
		
		storeWatcher = new StoreWatcher(classSourceStore);
		classSourceStore.addStoreListener(storeWatcher);
        testConsumer = new TestConsumer();
        storeWatcher.registerConsumer(testConsumer);
		
		this.events = new ArrayList<StoreEvent>();
		this.sourceEventHandler = new ClassSourceEventHandler(this.project, this.testDatabase);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public class TestConsumer implements Consumer<StoreEvent> {

		@Override
		public void consume(StoreEvent item) {
			ClassSourceEventHandlerTest.this.events.add(item);
		}
		
	}

	@Test
	public void testInitialAdd() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public boolean foo() { return true; }\n" +
						"}";

		classSourceStore.startListening();
		classSourceStore.write(sourceName, source.getBytes());
		classSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertTrue(testDatabase.containsMethod(new JavaSignatureParser("A.foo()Z").parseSignature()));
	}

	@Test
	public void testChange() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public boolean foo() { return true; }\n" +
						"}";
		String sourceChange =
				"public class " + className + " {\n" +
						"public boolean bar() { return true; }\n" +
						"}";
		
		classSourceStore.write(sourceName, source.getBytes());

		classSourceStore.startListening();
		
		classSourceStore.write(sourceName, sourceChange.getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertTrue(testDatabase.containsMethod(new JavaSignatureParser("A.bar()Z").parseSignature()));
		assertFalse(testDatabase.containsMethod(new JavaSignatureParser("A.foo()Z").parseSignature()));
	}

	@Test
	public void testChangeWhileListening() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public boolean foo() { return true; }\n" +
						"}";
		String sourceChange =
				"public class " + className + " {\n" +
						"public boolean bar() { return true; }\n" +
						"}";

		classSourceStore.startListening();

		classSourceStore.write(sourceName, source.getBytes());
		classSourceStore.write(sourceName, sourceChange.getBytes());
		
		classSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertTrue(testDatabase.containsMethod(new JavaSignatureParser("A.bar()Z").parseSignature()));
		assertFalse(testDatabase.containsMethod(new JavaSignatureParser("A.foo()Z").parseSignature()));
	}

	@Test
	public void testRemove() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public boolean foo() { return true; }\n" +
						"}";

		classSourceStore.startListening();

		classSourceStore.write(sourceName, source.getBytes());
		classSourceStore.remove(sourceName);
		
		classSourceStore.stopListening();
		
		sourceEventHandler.consume(events);

		assertEquals(0, testDatabase.getMethodsIn(className).size());
	}

	@Test
	public void testSetup() throws Exception {
		String className = "A";
		String sourceName = NameUtils.toSourceName(className);
		String source =
				"public class " + className + " {\n" +
						"public boolean foo() { return true; }\n" +
						"}";

		classSourceStore.write(sourceName, source.getBytes());
		
		sourceEventHandler.setup();
		
		assertTrue(this.project.getBinaryStore().contains(NameUtils.toBinaryName(className)));
		/*
		 * constructor + foo
		 */
		assertEquals(2, testDatabase.getMethodsIn(className).size());
	}
	
}
