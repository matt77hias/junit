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

public class Project {

	protected final Store classSourceStore;
	protected final Store testSourceStore;
	protected final Store binaryStore;

	protected final ReloadingStoreClassLoader testClassLoader;

	protected final StoreWatcher classSourceWatcher;
	protected final StoreWatcher testSourceWatcher;

	public Project(Store classSourceStore, Store testSourceStore, Store binaryStore) {
		this.classSourceStore = checkNotNull(classSourceStore);
		this.testSourceStore = checkNotNull(testSourceStore);
		this.binaryStore = checkNotNull(binaryStore);

		this.testClassLoader = new ReloadingStoreClassLoader(binaryStore, getClass().getClassLoader());

		this.classSourceWatcher = new StoreWatcher(classSourceStore, StoreFilter.SOURCE);
		this.testSourceWatcher = new StoreWatcher(testSourceStore, StoreFilter.SOURCE);
	}

	public Store getClassSourceStore() {
		return classSourceStore;
	}

	public Store getTestSourceStore() {
		return testSourceStore;
	}

	public Store getBinaryStore() {
		return binaryStore;
	}

	public void startListening(Consumer<StoreEvent> consumer) {
		this.classSourceWatcher.registerConsumer(consumer);
		this.testSourceWatcher.registerConsumer(consumer);
		this.classSourceStore.addStoreListener(this.classSourceWatcher);
		this.testSourceStore.addStoreListener(this.testSourceWatcher);
		this.classSourceStore.startListening();
		this.testSourceStore.startListening();
	}

	public void stopListening(Consumer<StoreEvent> consumer) {
		this.classSourceWatcher.unregisterConsumer(consumer);
		this.testSourceWatcher.unregisterConsumer(consumer);
		this.classSourceStore.removeStoreListener(this.classSourceWatcher);
		this.testSourceStore.removeStoreListener(this.testSourceWatcher);
		this.classSourceStore.stopListening();
		this.testSourceStore.stopListening();
	}

	public ClassLoader getClassLoader() {
		return this.testClassLoader;
	}

	public void reloadClasses() {
		this.testClassLoader.reload();
	}

	public boolean isBinaryClass(String className) {
		return getBinaryStore().contains(NameUtils.toBinaryName(className));
	}

	public JavaCompiler createClassCompiler() {
		return new EclipseCompiler(getClassSourceStore(), getBinaryStore(), getClassLoader());
	}

	public JavaCompiler createTestCompiler() {
		return new EclipseCompiler(getTestSourceStore(), getBinaryStore(), getClassLoader());
	}
}
