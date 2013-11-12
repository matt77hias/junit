package kuleuven.group2.data.updating;

import java.lang.management.ManagementFactory;
import java.util.Set;

import com.sun.tools.attach.VirtualMachine;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.signature.JavaSignatureParser;
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
	
	protected ICurrentRunningTestHolder currentRunningTestHolder;
	protected Set<TestedMethod> methods;
	
	// CONSTRUCTION

	public MethodTestLinkUpdater(ICurrentRunningTestHolder currentRunningTestHolder, Set<TestedMethod> methods) {
		super();
		this.currentRunningTestHolder = currentRunningTestHolder;
		this.methods = methods;
	}
	
	// ADD LINK

	@Override
	public void enterMethod(String methodName) {
		Test currentRunningTest = currentRunningTestHolder.getCurrentRunningTest();
		TestedMethod enteredMethod = new JavaSignatureParser(methodName)
			.parseSignature();
		enteredMethod.addTest(currentRunningTest);
		methods.add(enteredMethod);
		
		launchOSSRewriter();
		registerMonitor(this);
	}
	
	// MANAGE OSSREWRITER
	
	/**
	 * Loads the ossrewriter to the current VM, enables it, sets a filter (for exclusion of org/junit code)
	 * and finally, lets it retransform all classes.
	 */
	public static void launchOSSRewriter() {
	    String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
	    int p = nameOfRunningVM.indexOf('@');
	    String pid = nameOfRunningVM.substring(0, p);

	    try {
	        VirtualMachine vm = VirtualMachine.attach(pid);
	        vm.loadAgent("D:\\DLS\\ossrewriter-1.0.jar", ""); // TODO: link to project jar
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
	
	public static void stopOSSRewriter() {
		OSSRewriter.disable();
	}
	
	public static void registerMonitor(Monitor monitor) {
		MonitorEntrypoint.register(monitor);
	}
	
	public static void unregisterMonitor(Monitor monitor) {
		MonitorEntrypoint.unregister(monitor);
	}
}
