package kuleuven.group2.data.updating;

import static org.junit.Assert.assertTrue;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;

import com.sun.tools.attach.VirtualMachine;

public class OssRewriterTest {

	A a;

	@Before
	public void setUp() throws Exception {
		a = new A();

		String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
		int p = nameOfRunningVM.indexOf('@');
		String pid = nameOfRunningVM.substring(0, p);

		try {
			VirtualMachine vm = VirtualMachine.attach(pid);
	        vm.loadAgent("res\\ossrewriter-1.0.jar", "");
			vm.detach();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		OSSRewriter.enable();
		OSSRewriter.setUserExclusionFilter(new Predicate<String>() {
			public boolean apply(String arg0) {
				return arg0.startsWith("org/junit");
			}
		});
		OSSRewriter.retransformAllClasses();
	}

	@After
	public void tearDown() throws Exception {
		OSSRewriter.disable();
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
		MonitorEntrypoint.register(new Monitor() {

			@Override
			public void enterMethod(String arg0) {
				visitedMethodsTracker.addVisitedMethod(arg0);
			}
		});

		a.visit();
		
		assertTrue(visitedMethodsTracker.methodIsVisited("kuleuven/group2/data/updating/OssRewriterTest$A.visit()V"));
	}

}
