package kuleuven.group2;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.tools.attach.VirtualMachine;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;

public class OssRewriter {

	public static void main(String args[]) {
		
		MonitorEntrypoint.register(new Monitor() {
			
			@Override
			public void enterMethod(String arg0) {
				System.out.println("test " + arg0 + " - " + System.currentTimeMillis());
			}
		});
		
	    String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
	    int p = nameOfRunningVM.indexOf('@');
	    String pid = nameOfRunningVM.substring(0, p);

	    try {
	        VirtualMachine vm = VirtualMachine.attach(pid);
	        vm.loadAgent("D:\\DLS\\ossrewriter-1.0.jar", "");
	        vm.detach();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
		
	    OSSRewriter.enable();
	    OSSRewriter.retransformAllClasses();
		
		helloWorld();
	}
	
	public static void helloWorld() {
		System.out.println("Hello world!");
	}
	
}
