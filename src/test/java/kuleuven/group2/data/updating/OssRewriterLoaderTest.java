package kuleuven.group2.data.updating;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.ossrewriter.Monitor;

public class OssRewriterLoaderTest {

	protected A a;
	protected static OssRewriterLoader ossRewriterLoader = new OssRewriterLoader();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ossRewriterLoader.launchOssRewriter();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		a = new A();
	}

	@After
	public void tearDown() throws Exception {
	}

	public class A {

		public A() {

		}

		private boolean visited = false;

		public void visit() {
			visited = true;
		}

		public boolean getVisited() {
			return visited;
		}
	}

	public class VisitedMethodsTracker {

		private List<String> visitedMethodNames = new ArrayList<String>();

		public VisitedMethodsTracker() {

		}

		public boolean methodIsVisited(String method) {
			return visitedMethodNames.contains(method);
		}

		public void addVisitedMethod(String visitedMethod) {
			visitedMethodNames.add(visitedMethod);
		}

	}
	
	@Test
	public void methodIsVisitedTest() {
		final VisitedMethodsTracker visitedMethodsTracker = new VisitedMethodsTracker();
		Monitor monitor = new Monitor() {

			@Override
			public void enterMethod(String arg0) {
				visitedMethodsTracker.addVisitedMethod(arg0);
			}
		};
		ossRewriterLoader.registerMonitor(monitor);

		a.visit();
		
		ossRewriterLoader.unregisterMonitor(monitor);
		
		assertTrue(visitedMethodsTracker.methodIsVisited("kuleuven/group2/data/updating/OssRewriterLoaderTest$A.visit()V"));
	}

}
