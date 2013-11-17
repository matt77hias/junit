package kuleuven.group2.data.methodlink;

import kuleuven.group2.data.ICurrentRunningTestHolder;
import kuleuven.group2.data.MethodTestLinkUpdater;
import kuleuven.group2.data.OssRewriterLoader;
import kuleuven.group2.data.TestDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodLinkerTest {
	
	private static MethodTestLinkUpdater methodLinker;
	private static TestDatabase testDatabase;
	private static OssRewriterLoader ossRewriterLoader;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDatabase = new TestDatabase();
		ossRewriterLoader = new OssRewriterLoader();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		methodLinker = new MethodTestLinkUpdater(
				testDatabase,
				ossRewriterLoader,
				new TestCurrentRunningTestHolder("A", "MethodA"));
	}

	@After
	public void tearDown() throws Exception {
		methodLinker.printMethodLinks();
	}
	
	public class TestCurrentRunningTestHolder implements ICurrentRunningTestHolder {
		
		protected kuleuven.group2.data.Test currentTest; 
		
		public TestCurrentRunningTestHolder(String testClassName, String testMethodName) {
			this.currentTest = new
					kuleuven.group2.data.Test(testClassName, testMethodName);
		}

		public kuleuven.group2.data.Test getCurrentRunningTest() {
			return currentTest;
		}
	}
	
	public class A {
		public int testMethodA(String arg1, int arg2) {
			return 0;
		}
	}

	@Test
	public void test() {
		A a = new A();
		
		// OssRewriter is started here because we only want to enter this specific method and not all previous method calls
		ossRewriterLoader.launchOssRewriter();
		
		a.testMethodA("", 0);
		
		ossRewriterLoader.stopOssRewriter();
	}

}
