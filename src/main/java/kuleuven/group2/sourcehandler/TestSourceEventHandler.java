package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestRemoveUpdater;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreClassLoader;
import kuleuven.group2.store.StoreEvent;

public class TestSourceEventHandler extends SourceEventHandler {

	protected final Store testSourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;
	protected final StoreClassLoader testClassLoader;

	protected final TestRemoveUpdater testRemoveUpdater;

	public TestSourceEventHandler(Store testSourceStore, Store binaryStore, TestDatabase testDatabase,
			StoreClassLoader testClassLoader) {
		this.testSourceStore = testSourceStore;
		this.binaryStore = binaryStore;
		this.testDatabase = testDatabase;
		this.testClassLoader = testClassLoader;

		this.testRemoveUpdater = new TestRemoveUpdater(testDatabase);
	}

	@Override
	public void handleEvents(List<StoreEvent> events) throws Exception {
		// Collect changes in test sources
		Changes changes = collectChanges(events, testSourceStore);

		// Compile changed test sources
		JavaCompiler classCompiler = new EclipseCompiler(testSourceStore, binaryStore, testClassLoader);
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());
		if (!result.isSuccess()) {
			// TODO What exception should be thrown?
			throw new Exception("Compilation of test sources failed: " + result.getErrors());
		}

		// Update database
		testRemoveUpdater.removeTestClasses(NameUtils.toClassNames(changes.getRemovedResources()));
	}

}
