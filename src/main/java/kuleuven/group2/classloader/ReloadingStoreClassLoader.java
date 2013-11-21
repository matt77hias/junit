package kuleuven.group2.classloader;

import kuleuven.group2.store.Store;

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