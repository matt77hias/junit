package kuleuven.group2.data.methodlink;

import java.util.HashSet;

import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.updating.ICurrentRunningTestHolder;
import kuleuven.group2.data.updating.MethodTestLinkUpdater;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodLinkerTest {
	
	private static MethodTestLinkUpdater methodLinker;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		methodLinker = new MethodTestLinkUpdater(
				new TestCurrentRunningTestHolder("A", "MethodA"),
				new HashSet<TestedMethod>());
	}

	@After
	public void tearDown() throws Exception {
		methodLinker.printMethodLinks();
		methodLinker.destroy();
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
