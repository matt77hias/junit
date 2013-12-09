package kuleuven.group2.rewrite;

import java.lang.management.ManagementFactory;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
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
	}

	/*
	 * Enabling/disabling
	 */

	/**
	 * Start rewriting and re-transform all classes.
	 * 
	 * @see #enable(boolean)
	 */
	public void enable() {
		enable(true);
	}

	/**
	 * Start rewriting.
	 * 
	 * @param retransformAllClasses
	 *            If all loaded classes should be re-transformed as well.
	 */
	public void enable(boolean retransformAllClasses) {
		ensureLoaded();
		if (!OSSRewriter.isEnabled()) {
			OSSRewriter.enable();
			OSSRewriter.retransformAllClasses();
		}
	}

	/**
	 * Stop rewriting.
	 */
	public void disable() {
		ensureLoaded();
		if (OSSRewriter.isEnabled()) {
			OSSRewriter.disable();
		}
	}

	/*
	 * Monitoring
	 */

	/**
	 * Register a monitor.
	 * 
	 * @param monitor
	 *            The monitor to be registered.
	 */
	public void registerMonitor(Monitor monitor) {
		ensureLoaded();
		MonitorEntrypoint.register(monitor);
	}

	/**
	 * Unregister a monitor.
	 * 
	 * @param monitor
	 *            The monitor to be unregistered.
	 */
	public void unregisterMonitor(Monitor monitor) {
		ensureLoaded();
		MonitorEntrypoint.unregister(monitor);
	}

	/*
	 * Transformation filtering
	 */

	protected Predicate<String> transformFilter = Predicates.alwaysTrue();

	/**
	 * Set the (inclusion) filter to use when transforming classes.
	 * 
	 * <p>
	 * The filter receives the full class name. The class will be transformed
	 * iff the filter returns {@code true} for that class.
	 * </p>
	 * 
	 * @param filter
	 *            The new transformation filter.
	 */
	public void setTransformFilter(final Predicate<String> filter) {
		transformFilter = filter;
		updateExclusionFilter();
	}

	/**
	 * Removes the transformation filter.
	 */
	public void removeTransformFilter() {
		setTransformFilter(null);
	}

	/**
	 * Updates the rewriter's exclusion filter using the configured
	 * {@link #transformFilter}.
	 */
	protected void updateExclusionFilter() {
		ensureLoaded();
		if (transformFilter == null) {
			// No filter set
			OSSRewriter.resetUserExclusionFilter();
		} else {
			/*
			 * OSS Rewriter uses an exclusion filter, so we need to invert our
			 * inclusion filter
			 */
			Predicate<String> exclusionFilter = Predicates.not(transformFilter);
			OSSRewriter.setUserExclusionFilter(new PredicateAdapter<String>(exclusionFilter));
		}
	}

	/**
	 * Adapts a {@link com.google.common.base.Predicate} to be used as a
	 * {@link be.kuleuven.cs.ossrewriter.Predicate}.
	 */
	protected static class PredicateAdapter<T> implements be.kuleuven.cs.ossrewriter.Predicate<T> {

		private final Predicate<T> predicate;

		public PredicateAdapter(Predicate<T> predicate) {
			this.predicate = predicate;
		}

		@Override
		public boolean apply(T value) {
			return predicate.apply(value);
		}

	}

}
