package kuleuven.group2.data.methodlink;

import static org.junit.Assert.*;
import kuleuven.group2.data.updating.ICurrentRunningTestHolder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodLinkerTest {
	
	private static MethodLinker methodLinker = MethodLinker.getInstance();
	private MethodLinkRegistry methodLinkRegistry;
	private MethodLinkRegistryMonitor monitor;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		methodLinker.launchOSSRewriter();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		methodLinkRegistry = new MethodLinkRegistry();
		monitor
			= new MethodLinkRegistryMonitor(
					methodLinkRegistry,
					new TestCurrentRunningTestHolder("A", "MethodA"));
		methodLinker.registerMonitor(monitor);
	}

	@After
	public void tearDown() throws Exception {
		methodLinker.unregisterMonitor(monitor);
		System.out.println(methodLinkRegistry.tests);
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
		
		a.testMethodA("", 0);
	}

}
