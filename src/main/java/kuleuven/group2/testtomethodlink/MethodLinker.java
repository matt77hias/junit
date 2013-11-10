package kuleuven.group2.testtomethodlink;

import java.lang.management.ManagementFactory;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;

import com.sun.tools.attach.VirtualMachine;

public class MethodLinker {
	
	private static MethodLinker instance;
	
	public static MethodLinker getInstance() {
		if (instance == null) {
			instance = new MethodLinker();
		}
		return instance;
	}
	
	private MethodLinker() {
		
	}
	
	public void start() {
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
	
	public void stop() {
		OSSRewriter.disable();
	}
	
	public void registerMonitor(Monitor monitor) {
		MonitorEntrypoint.register(monitor);
	}
	
	public void unregisterMonitor(Monitor monitor) {
		MonitorEntrypoint.unregister(monitor);
	}
	
}
