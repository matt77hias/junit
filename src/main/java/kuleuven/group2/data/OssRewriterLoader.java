package kuleuven.group2.data;

import java.lang.management.ManagementFactory;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;

import com.sun.tools.attach.VirtualMachine;

/**
 * Manages the loading of the OssRewriter agent in the current virtual machine.
 * 
 * @author Ruben, Vital
 *
 */
public class OssRewriterLoader {
	/**
	 * Loads the ossrewriter to the current VM, enables it, sets a filter (for exclusion of org/junit code)
	 * and finally, lets it retransform all classes.
	 */
	public void launchOssRewriter() {
	    String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
	    int p = nameOfRunningVM.indexOf('@');
	    String pid = nameOfRunningVM.substring(0, p);

	    try {
	        VirtualMachine vm = VirtualMachine.attach(pid);
	        // TODO: link to project jar
	        vm.loadAgent("U:\\vital.dhaveloose\\Lokaal\\Eclipse_Workspace\\junit\\bin\\lib\\ossrewriter-1.0.jar", ""); // Vitals locatie
	        //vm.loadAgent("D:\\DLS\\ossrewriter-1.0.jar", ""); //Rubens locatie
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
	
	// TODO: check how restarting of ossrewriter works
	public void stopOssRewriter() {
		OSSRewriter.disable();
	}
	
	public void registerMonitor(Monitor monitor) {
		MonitorEntrypoint.register(monitor);
	}
	
	public void unregisterMonitor(Monitor monitor) {
		MonitorEntrypoint.unregister(monitor);
	}
}
