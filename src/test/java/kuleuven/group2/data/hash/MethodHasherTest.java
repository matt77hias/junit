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
	
	protected static CompilationResult compiledClassAResult;

	protected static Store sourceStore;
	protected static Store binaryStore;
	protected static EclipseCompiler compiler;
	protected static ClassLoader classLoader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sourceStore = new MemoryStore();
		binaryStore = new MemoryStore();
		classLoader = new StoreClassLoader(binaryStore);
		compiler = new EclipseCompiler(sourceStore, binaryStore, classLoader);
		
		String className = "A";
		String source =
				"public class A {\n" +
						"public boolean foo() { return true; }\n" +
						"}";
		sourceStore.write(NameUtils.toSourceName(className), source.getBytes());

		compiledClassAResult = compiler.compileAll();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	public void setup() {
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetHashes() {
		MethodHasher methodHasher = new MethodHasher(compiledClassAResult.getCompiledClass("A"));
		
		Map<String, MethodHash> mapWithMethodHashes = methodHasher.getHashes();
		
		assertTrue(mapWithMethodHashes.containsKey("foo()Z"));
	}
	
	/*
	 * The precondition for the constructor expects valid class bytes
	 * here we see if we will atleast get notified if invalid class bytes are passed
	 * This is necessary because the constructor itself will not throw the exception and
	 * we can't guarantee which exception will be thrown
	 */
	@Test(expected=RuntimeException.class)
	public void testConstructorInvalidCompiled() {
		new MethodHasher("invalid".getBytes());
	}

}
