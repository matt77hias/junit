package kuleuven.group2.classloader;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

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

	@Test
	public void testLoadClassAnnotations() throws ClassNotFoundException {
		String className = "ATest";
		String source =
				"import org.junit.Test; \n" + 
						"public class " + className + " {\n" +
						"@Test\n" +
						"public void foo() { int i = 0; }\n" +
						"}";
		
		sourceStore.write(NameUtils.toSourceName(className), source.getBytes());
		
		compiler.compileAll();
	
		Class<?> loadedClass = classLoader.loadClass(className);
		
		assertEquals(className, loadedClass.getName());
		
		for(Method method : loadedClass.getMethods()) {
			if (method.getName().equals("foo")) {
				// if this test fails, could also be problem with compiler
				assertNotNull(method.getAnnotation(Test.class));
			}
		}
	}
	
	@Test(expected=ClassNotFoundException.class)
	public void testLoadClassNotInStore() throws ClassNotFoundException {
		classLoader.loadClass("NonExistingClass");
	}

}
