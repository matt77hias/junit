package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.TestChangeUpdater;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;

/**
 * A handler for events that happen with test source code.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public class TestSourceEventHandler extends SourceEventHandler {

	protected final Store testSourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;
	protected final ClassLoader testClassLoader;

	protected final TestChangeUpdater testChangeUpdater;

	public TestSourceEventHandler(Store testSourceStore, Store binaryStore, TestDatabase testDatabase,
			ClassLoader testClassLoader) {
		this.testSourceStore = testSourceStore;
		this.binaryStore = binaryStore;
		this.testDatabase = testDatabase;
		this.testClassLoader = testClassLoader;

		this.testChangeUpdater = new TestChangeUpdater(testDatabase, testClassLoader);
	}

	@Override
	public void setup() throws Exception {
		// Compile all test sources
		JavaCompiler classCompiler = new EclipseCompiler(testSourceStore, binaryStore, testClassLoader);
		CompilationResult result = classCompiler.compileAll();

		handleCompilation(result);
	}

	@Override
	public void handleEvents(List<StoreEvent> events) throws Exception {
		// Collect changes in test sources
		Changes changes = collectChanges(events, testSourceStore);

		// Remove test methods in old test classes
		testChangeUpdater.removeTests(NameUtils.toClassNames(changes.getRemovedResources()));

		// Compile changed test sources
		JavaCompiler classCompiler = new EclipseCompiler(testSourceStore, binaryStore, testClassLoader);
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());

		handleCompilation(result);
	}

	protected void handleCompilation(CompilationResult result) throws Exception {
		// Update test methods in compiled test classes
		testChangeUpdater.updateTestClasses(result.getCompiledClassNames());

		if (!result.isSuccess()) {
			// TODO What exception should be thrown when EclipseCompiler.compile returns false?
			throw new Exception("Compilation of test sources failed: " + result.getErrors());
		}
	}

}
