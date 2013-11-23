package kuleuven.group2.classloader;

import static org.junit.Assert.*;
import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.MemoryStore;
import kuleuven.group2.store.Store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StoreClassLoaderTest {

	protected Store sourceStore;
	protected Store binaryStore;
	protected EclipseCompiler compiler;
	protected ClassLoader classLoader;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sourceStore = new MemoryStore();
		binaryStore = new MemoryStore();
		classLoader = new StoreClassLoader(binaryStore);
		compiler = new EclipseCompiler(sourceStore, binaryStore, classLoader);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadClassSimple() throws ClassNotFoundException {
		String className = "A";
		String source =
				"public class A {\n" +
						"public boolean foo() { return true; }\n" +
						"}";
		
		sourceStore.write(NameUtils.toSourceName(className), source.getBytes());
		
		compiler.compileAll();
	
		Class<?> loadedClass = classLoader.loadClass(className);
		
		assertEquals(className, loadedClass.getName());
	}

}
