package kuleuven.group2.data.updating;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

import com.sun.tools.attach.VirtualMachine;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.updating.signature.JavaSignatureParser;
import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;

/**
 * This class updates the link between methods and tests, when tests are run.
 * 
 * At construction it launches an ossrewriter agent and it registers itself as a monitor.
 * By using this and a given currentRunningTestHolder it couples methods and tests, and it
 * saves this link by adding the test to the method. 
 * 
 * @author Vital D'haveloose, Ruben Pieters
 */
public class MethodTestLinkUpdater extends Monitor{
	
	// ATTRIBUTES
	
	protected ICurrentRunningTestHolder currentRunningTestHolder; //TODO: doen met een RunListener (inner class), en als een test start: alle links met die test flushen
	protected Set<TestedMethod> methods;
	
	// CONSTRUCTION

	public MethodTestLinkUpdater(ICurrentRunningTestHolder currentRunningTestHolder, Set<TestedMethod> methods) {
		super();
		this.currentRunningTestHolder = currentRunningTestHolder;
		this.methods = methods;
		
		launchOSSRewriter();
		registerMonitor(this);
	}
	
	// DESTRUCTION
	
	public void destroy() {
		unregisterMonitor(this);
		stopOSSRewriter();
		currentRunningTestHolder = null;
		methods = null;
	}
	
	// LINKS

	@Override
	public void enterMethod(String methodName) {
		Test currentRunningTest = currentRunningTestHolder.getCurrentRunningTest();
		TestedMethod enteredMethod = new JavaSignatureParser(methodName)
			.parseSignature();
		enteredMethod.addTest(currentRunningTest);
		methods.add(enteredMethod);
	}

	public void printMethodLinks() {
		Set<Test> tests;
		for(TestedMethod method : methods) {
			System.out.println(method);
			tests = new HashSet<Test>(method.getTests());
			for(Test test : tests) {
				System.out.println(test);
			}
		}
	}
	
	//TODO: delete links? => impossibruh
	
	// MANAGE OSSREWRITER
	
	/**
	 * Loads the ossrewriter to the current VM, enables it, sets a filter (for exclusion of org/junit code)
	 * and finally, lets it retransform all classes.
	 */
	private void launchOSSRewriter() {
	    String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
	    int p = nameOfRunningVM.indexOf('@');
	    String pid = nameOfRunningVM.substring(0, p);

	    try {
	        VirtualMachine vm = VirtualMachine.attach(pid);
	        // TODO: link to project jar
	        vm.loadAgent("U:\\vital.dhaveloose\\Lokaal\\Eclipse_Workspace\\junit\\bin\\lib\\ossrewriter-1.0.jar", ""); // Vitals locatie
	        //vm.loadAgent("D:\\DLS\\ossrewriter-1.0.jar", ""); Rubens locatie
	        vm.detach();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		
	    OSSRewriter.enable();
	    OSSRewriter.setUserExclusionFilter(new Predicate<String>() {
			public boolean apply(String arg0) {
				return arg0.startsWith("org/junit"); // TODO: determine what to do with this
			}
		});
	    OSSRewriter.retransformAllClasses();
	}
	
	private void stopOSSRewriter() {
		OSSRewriter.disable();
	}
	
	private void registerMonitor(Monitor monitor) {
		MonitorEntrypoint.register(monitor);
	}
	
	private void unregisterMonitor(Monitor monitor) {
		MonitorEntrypoint.unregister(monitor);
	}
}
