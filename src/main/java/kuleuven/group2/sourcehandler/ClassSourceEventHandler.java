package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.MethodChangeUpdater;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;

/**
 * Handles events in the (non test) source code, such as deletions, adds and changes.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public class ClassSourceEventHandler extends SourceEventHandler {

	protected final Store classSourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;
	protected final ClassLoader testClassLoader;

	protected final MethodChangeUpdater methodChangeUpdater;

	public ClassSourceEventHandler(Store classSourceStore, Store binaryStore, TestDatabase testDatabase,
			ClassLoader testClassLoader) {
		this.classSourceStore = classSourceStore;
		this.binaryStore = binaryStore;
		this.testDatabase = testDatabase;
		this.testClassLoader = testClassLoader;

		this.methodChangeUpdater = new MethodChangeUpdater(testDatabase);
	}

	@Override
	public void handleEvents(List<StoreEvent> events) throws Exception {
		// Collect changes in class sources
		Changes changes = collectChanges(events, classSourceStore);

		// Remove methods in old classes
		methodChangeUpdater.removeClasses(NameUtils.toClassNames(changes.getRemovedResources()));

		// Compile changed class sources
		JavaCompiler classCompiler = new EclipseCompiler(classSourceStore, binaryStore, testClassLoader);
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());

		// Detect changes in compiled classes
		methodChangeUpdater.detectChanges(result.getCompiledClasses());

		if (!result.isSuccess()) {
			// TODO What exception should be thrown when EclipseCompiler.compile returns false?
			throw new Exception("Compilation of class sources failed: " + result.getErrors());
		}
	}

}
