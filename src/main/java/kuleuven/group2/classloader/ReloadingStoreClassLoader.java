package kuleuven.group2.classloader;

import kuleuven.group2.store.Store;

/**
 * A {@link ReloadingClassLoader} using {@link StoreClassLoader}s.
 * 
 * <p>
 * Each time this class loader is {@link #reload() reloaded}, a new
 * {@link StoreClassLoader} is created for the given {@link Store} (and parent
 * {@link ClassLoader}).
 * </p>
 * 
 * @author Group2
 * @version 19 November 2013
 */
public class ReloadingStoreClassLoader extends ReloadingClassLoader {

	protected final Store classStore;

	public ReloadingStoreClassLoader(Store classStore, ClassLoader parent) {
		super(parent);
		this.classStore = classStore;
	}

	@Override
	protected ClassLoader createClassLoader(ClassLoader parent) {
		return new StoreClassLoader(classStore, parent);
	}

}