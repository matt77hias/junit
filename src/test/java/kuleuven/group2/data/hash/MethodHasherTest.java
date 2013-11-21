package kuleuven.group2.data.hash;

import static org.junit.Assert.*;

import java.util.Map;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.MemoryStore;
import kuleuven.group2.store.Store;
import kuleuven.group2.classloader.StoreClassLoader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodHasherTest {

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
	public void setup() {
		sourceStore = new MemoryStore();
		binaryStore = new MemoryStore();
		classLoader = new StoreClassLoader(binaryStore);
		compiler = new EclipseCompiler(sourceStore, binaryStore, classLoader);
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String className = "A";
		String source =
				"public class A {\n" +
						"public boolean foo() { return true; }\n" +
						"}";
		sourceStore.write(NameUtils.toSourceName(className), source.getBytes());

		CompilationResult result = compiler.compileAll();
		
		MethodHasher methodHasher = new MethodHasher(result.getCompiledClass("A"));
		
		Map<String, MethodHash> mapWithMethodHashes = methodHasher.getHashes();
		
		assertTrue(mapWithMethodHashes.containsKey("foo()Z"));
	}

}
