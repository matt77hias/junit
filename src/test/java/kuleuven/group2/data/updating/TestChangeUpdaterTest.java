package kuleuven.group2.data.updating;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kuleuven.group2.classloader.StoreClassLoader;
import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.store.MemoryStore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestChangeUpdaterTest {
	
	TestDatabase testDatabase;
	TestChangeUpdater testChangeUpdater;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testDatabase = new TestDatabase();
		testChangeUpdater = new TestChangeUpdater(testDatabase, getClass().getClassLoader());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRemoveTest() {
		testDatabase.addTest(new kuleuven.group2.data.Test(getClass().getName(), "testRemoveTest"));
		
		testChangeUpdater.removeTests(getClass().getName());
		
		assertFalse(testDatabase.containsTest(getClass().getName(), "testRemoveTest"));
	}

	@Test
	public void testRemoveTestFromTestList() {
		testDatabase.addTest(new kuleuven.group2.data.Test(getClass().getName(), "testRemoveTestFromTestList"));
		testDatabase.addTest(new kuleuven.group2.data.Test(getClass().getName(), "testRemoveTest"));
		testDatabase.addTest(new kuleuven.group2.data.Test(JavaSignatureParserTest.class.getName(), "testMethod2Arg"));
		
		List<String> testListToRemove = new ArrayList<String>();
		testListToRemove.add(getClass().getName());
		testListToRemove.add(JavaSignatureParserTest.class.getName());
		
		testChangeUpdater.removeTests(testListToRemove);
		
		assertFalse(testDatabase.containsTest(getClass().getName(), "testRemoveTest"));
		assertFalse(testDatabase.containsTest(getClass().getName(), "testRemoveTest"));
		assertFalse(testDatabase.containsTest(JavaSignatureParserTest.class.getName(), "testMethod2Arg"));
	}
	
	@Test
	public void testUpdatingTests() {
		testChangeUpdater.updateTestClass(getClass());
		
		assertTrue(testDatabase.containsTest(getClass().getName(), "testUpdatingTests"));
	}
	
	@Test
	public void testUpdatingTestsByName() {
		Collection<String> testClassNames = new ArrayList<String>();
		testClassNames.add(getClass().getName());
		
		testChangeUpdater.updateTestClasses(testClassNames);
		
		assertTrue(testDatabase.containsTest(getClass().getName(), "testUpdatingTests"));
	}
	
	@Test
	public void testUpdateWithStoreClassLoader() throws ClassNotFoundException {
		String className = "ATest";
		String source =
				"import org.junit.Test; \n" + 
						"public class " + className + " {\n" +
						"@Test\n" +
						"public void foo() { int i = 0; }\n" +
						"}";

		MemoryStore classSourceStore = new MemoryStore();
		MemoryStore binaryStore = new MemoryStore();
		
		StoreClassLoader binaryLoader = new StoreClassLoader(binaryStore);
		testChangeUpdater = new TestChangeUpdater(testDatabase, binaryLoader);
		
		EclipseCompiler compiler = new EclipseCompiler(classSourceStore, binaryStore, binaryLoader);
		
		classSourceStore.write(NameUtils.toSourceName(className), source.getBytes());
		
		CompilationResult result = compiler.compileAll();
		
		assertTrue(binaryStore.contains(NameUtils.toBinaryName(className)));
		
		//Class<?> a = binaryLoader.loadClass(className);
		
		testChangeUpdater.updateTestClasses(result.getCompiledClassNames());
		//testChangeUpdater.updateTestClass(a);
		
		assertTrue(testDatabase.containsTest(className, "foo"));
	}

}
