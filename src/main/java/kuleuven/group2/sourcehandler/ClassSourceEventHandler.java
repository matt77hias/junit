package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.MethodChangeUpdater;
import kuleuven.group2.data.updating.MethodRemoveUpdater;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreClassLoader;
import kuleuven.group2.store.StoreEvent;

public class ClassSourceEventHandler extends SourceEventHandler {

	protected final Store classSourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;
	protected final StoreClassLoader testClassLoader;

	protected final MethodChangeUpdater methodChangeUpdater;
	protected final MethodRemoveUpdater methodRemoveUpdater;

	public ClassSourceEventHandler(Store classSourceStore, Store binaryStore, TestDatabase testDatabase,
			StoreClassLoader testClassLoader) {
		this.classSourceStore = classSourceStore;
		this.binaryStore = binaryStore;
		this.testDatabase = testDatabase;
		this.testClassLoader = testClassLoader;

		this.methodChangeUpdater = new MethodChangeUpdater(testDatabase);
		this.methodRemoveUpdater = new MethodRemoveUpdater(testDatabase);
	}

	@Override
	public void handleEvents(List<StoreEvent> events) throws Exception {
		// Collect changes in class sources
		Changes changes = collectChanges(events, classSourceStore);

		// Compile changed class sources
		JavaCompiler classCompiler = new EclipseCompiler(classSourceStore, binaryStore, testClassLoader);
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());
		if (!result.isSuccess()) {
			// TODO What exception should be thrown?
			throw new Exception("Compilation of class sources failed: " + result.getErrors());
		}

		// Update database
		methodRemoveUpdater.removeClasses(NameUtils.toClassNames(changes.getRemovedResources()));
		methodChangeUpdater.detectChanges(result.getCompiledClasses());
	}

}
