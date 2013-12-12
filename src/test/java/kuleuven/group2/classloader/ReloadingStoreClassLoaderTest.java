package kuleuven.group2.classloader;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import kuleuven.group2.classloader.ReloadingStoreClassLoader;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.MemoryStore;
import kuleuven.group2.store.Store;

import org.junit.BeforeClass;
import org.junit.Test;

public class ReloadingStoreClassLoaderTest {

	private static final Store sourceStore = new MemoryStore();
	private static final Store binaryStore = new MemoryStore();
	private static JavaCompiler compiler;
	private static ClassLoader parentClassLoader;

	private static final String A_className = "A";
	private static final String A_methodName = "a";
	private static final String A_v1 = "public class A { public int a() { return 1; } }";
	private static final String A_v2 = "public class A { public int a() { return 2; } }";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parentClassLoader = ReloadingStoreClassLoaderTest.class.getClassLoader();
		compiler = new EclipseCompiler(sourceStore, binaryStore, parentClassLoader);
	}

	protected static void compile(String className, String source) {
		String resourceName = NameUtils.toSourceName(className);
		sourceStore.write(resourceName, source.getBytes());
		compiler.compile(Collections.singleton(resourceName));
	}

	@Test
	public void reloadTest() throws ReflectiveOperationException {
		ReloadingStoreClassLoader classLoader = new ReloadingStoreClassLoader(binaryStore, parentClassLoader);
		// Compile and test version 1
		compile(A_className, A_v1);
		Class<?> clazz = classLoader.loadClass(A_className);
		Object result = clazz.getMethod(A_methodName).invoke(clazz.newInstance());
		assertEquals(1, result);
		// Reload
		classLoader.reload();
		// Compile and test version 2
		compile(A_className, A_v2);
		clazz = classLoader.loadClass(A_className);
		result = clazz.getMethod(A_methodName).invoke(clazz.newInstance());
		assertEquals(2, result);
	}

}
