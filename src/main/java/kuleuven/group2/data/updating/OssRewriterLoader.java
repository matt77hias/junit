package kuleuven.group2.data.updating;

import java.lang.management.ManagementFactory;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;

import com.sun.tools.attach.VirtualMachine;

/**
 * Manages the loading of the OssRewriter agent in the current virtual machine.
 * 
 * @author Ruben, Vital
 *
 */
public class OssRewriterLoader {
	/**
	 * Loads the ossrewriter to the current VM, enables it then retransforms all classes.
	 */
	public void launchOssRewriter() {
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
	    OSSRewriter.retransformAllClasses();
	}
	
	// stopOssRewriter removed
	// 'starting' and 'stopping' must be done by registering and unregistering your monitor
	
	public void registerMonitor(Monitor monitor) {
		MonitorEntrypoint.register(monitor);
	}
	
	public void unregisterMonitor(Monitor monitor) {
		MonitorEntrypoint.unregister(monitor);
	}
}
