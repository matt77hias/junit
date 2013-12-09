package kuleuven.group2.rewrite;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.rewrite.OssRewriterLoader;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.ossrewriter.Monitor;

public class OssRewriterLoaderTest {

	protected A a;
	protected static OssRewriterLoader ossRewriterLoader = OssRewriterLoader.getInstance();

	@Before
	public void setUp() throws Exception {
		a = new A();
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
		ossRewriterLoader.enable();

		a.visit();

		ossRewriterLoader.disable();
		ossRewriterLoader.unregisterMonitor(monitor);

		assertTrue(visitedMethodsTracker.methodIsVisited(NameUtils.toInternalName(A.class.getName()) + ".visit()V"));
	}

}
