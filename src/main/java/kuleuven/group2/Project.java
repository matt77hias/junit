package kuleuven.group2;

import static com.google.common.base.Preconditions.checkNotNull;
import kuleuven.group2.classloader.ReloadingStoreClassLoader;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;
import kuleuven.group2.store.StoreFilter;
import kuleuven.group2.store.StoreWatcher;
import kuleuven.group2.util.Consumer;

/**
 * A class of projects. A project contains three stores:
 * one for the class (non-testing) source code, one for
 * the test source code and one for all the binaries.
 * 
 * A project contains support for compiling and for
 * letting consumers listen to them indirectly so that
 * they can consume store events.
 * 
 * @author	Group 2
 * @version	19 December 2013
 *
 */
public class Project {

	/**
	 * The class source store of this project.
	 */
	private final Store classSourceStore;
	
	/**
	 * The class test source store of this project.
	 */
	private final Store testSourceStore;
	
	/**
	 * The class binary store of this project.
	 */
	private final Store binaryStore;

	/**
	 * The reloading class store loader of this project.
	 */
	private final ReloadingStoreClassLoader testClassLoader;

	/**
	 * The store watcher of the class source store
	 * of this project.
	 */
	protected final StoreWatcher classSourceWatcher;
	
	/**
	 * The store watcher of the test source store
	 * of this project.
	 */
	protected final StoreWatcher testSourceWatcher;

	/**
	 * Creates a new project with given class source store,
	 * test source store and binary store.
	 * 
	 * @param 	classSourceStore
	 * 			The class source store for this new project.
	 * @param 	testSourceStore
	 * 			The test source store for this new project.
	 * @param 	binaryStore
	 * 			The binary store for this new project.
	 */
	public Project(Store classSourceStore, Store testSourceStore, Store binaryStore) {
		this.classSourceStore = checkNotNull(classSourceStore);
		this.testSourceStore = checkNotNull(testSourceStore);
		this.binaryStore = checkNotNull(binaryStore);

		this.testClassLoader = new ReloadingStoreClassLoader(binaryStore, getClass().getClassLoader());

		this.classSourceWatcher = new StoreWatcher(classSourceStore, StoreFilter.SOURCE);
		this.testSourceWatcher = new StoreWatcher(testSourceStore, StoreFilter.SOURCE);
	}

	/**
	 * Returns the class source store of this project.
	 * 
	 * @return	The class source store of this project.
	 */
	public Store getClassSourceStore() {
		return classSourceStore;
	}

	/**
	 * Returns the test source store of this project.
	 * 
	 * @return	The test source store of this project.
	 */
	public Store getTestSourceStore() {
		return testSourceStore;
	}

	/**
	 * Returns the binary store of this project.
	 * 
	 * @return	The binary store of this project.
	 */
	public Store getBinaryStore() {
		return binaryStore;
	}

	/**
	 * Starts listening in order to let the given consumer consume
	 * store events.
	 * 
	 * @param 	consumer
	 * 			The consumer.
	 */
	public void startListening(Consumer<StoreEvent> consumer) {
		this.classSourceWatcher.registerConsumer(consumer);
		this.testSourceWatcher.registerConsumer(consumer);
		this.classSourceStore.addStoreListener(this.classSourceWatcher);
		this.testSourceStore.addStoreListener(this.testSourceWatcher);
		this.classSourceStore.startListening();
		this.testSourceStore.startListening();
	}

	/**
	 * Stops listening in order to let the given consumer consume
	 * store events.
	 * 
	 * @param 	consumer
	 * 			The consumer.
	 */
	public void stopListening(Consumer<StoreEvent> consumer) {
		this.classSourceWatcher.unregisterConsumer(consumer);
		this.testSourceWatcher.unregisterConsumer(consumer);
		this.classSourceStore.removeStoreListener(this.classSourceWatcher);
		this.testSourceStore.removeStoreListener(this.testSourceWatcher);
		this.classSourceStore.stopListening();
		this.testSourceStore.stopListening();
	}

	/**
	 * Returns the classloader of this project.
	 * 
	 * @return	The classloader of this project.
	 */
	public ClassLoader getClassLoader() {
		return this.testClassLoader;
	}

	/**
	 * Removes the wrapped instance of the classloader such that
	 * the next time a ClassLoader method of the classloader of
	 * this project is called, a new instance must be created.
	 * This new instance won't have the loaded classes from the
	 * previous instance, forcing it to load them again.
	 */
	public void reloadClasses() {
		this.testClassLoader.reload();
	}

	/**
	 * Checks if the given class name belongs to a class
	 * stored in the binary store of this project.
	 * 
	 * @param 	className
	 * 			The class name that needs be checked.
	 * @return	True if and only if the given class name
	 * 			belongs to a class stored in the binary
	 * 			store of this project.
	 */
	public boolean isBinaryClass(String className) {
		return getBinaryStore().contains(NameUtils.toBinaryName(className));
	}

	/**
	 * Creates a compiler for the class source code
	 * stored in the class source store of this project.
	 * 
	 * @return	A compiler for the class source code
	 * 			stored in the class source store of this project.
	 */
	public JavaCompiler createClassCompiler() {
		return new EclipseCompiler(getClassSourceStore(), getBinaryStore(), getClassLoader());
	}

	/**
	 * Creates a compiler for the test source code
	 * stored in the class source store of this project.
	 * 
	 * @return	A compiler for the test source code
	 * 			stored in the class source store of this project.
	 */
	public JavaCompiler createTestCompiler() {
		return new EclipseCompiler(getTestSourceStore(), getBinaryStore(), getClassLoader());
	}
}
