package kuleuven.group2.data.updating;

import java.lang.management.ManagementFactory;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;

import com.sun.tools.attach.VirtualMachine;

/**
 * Manages the loading of the OssRewriter agent in the current virtual machine.
 * 
 * @author Group2
 * @version 16 November 2013
 */
public class OssRewriterLoader {

	/*
	 * Lazy singleton
	 */

	protected static volatile OssRewriterLoader INSTANCE;

	public static OssRewriterLoader getInstance() {
		// Double-checked locking for lazy initialization
		OssRewriterLoader instance = INSTANCE;
		if (instance == null) {
			synchronized (OssRewriterLoader.class) {
				instance = INSTANCE;
				if (instance == null) {
					INSTANCE = instance = new OssRewriterLoader();
				}
			}
		}
		return instance;
	}

	protected OssRewriterLoader() {
	}

	/*
	 * Lazy loading
	 */

	private volatile boolean isLoaded = false;

	/**
	 * Ensures that the OSS Rewriter is loaded.
	 */
	protected void ensureLoaded() {
		// Double-checked locking for lazy loading
		if (!isLoaded) {
			synchronized (this) {
				if (!isLoaded) {
					load();
					isLoaded = true;
				}
			}
		}
	}

	/**
	 * Loads the OSS Rewriter to the current VM, enables it and re-transforms
	 * all loaded classes.
	 */
	protected void load() {
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

	public void registerMonitor(Monitor monitor) {
		ensureLoaded();
		MonitorEntrypoint.register(monitor);
	}

	public void unregisterMonitor(Monitor monitor) {
		ensureLoaded();
		MonitorEntrypoint.unregister(monitor);
	}

}
