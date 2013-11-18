package kuleuven.group2;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.EclipseCompiler;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.MethodChangeUpdater;
import kuleuven.group2.deferredrunner.DeferredConsumer;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreClassLoader;
import kuleuven.group2.store.StoreEvent;
import kuleuven.group2.store.StoreFilter;
import kuleuven.group2.store.StoreWatcher;
import kuleuven.group2.util.Consumer;

public class Pipeline {

	protected final Store classSourceStore;
	protected final Store testSourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;

	protected final PipelineTask task;
	protected final DeferredConsumer<StoreEvent> deferredTask;
	protected final StoreWatcher classSourceWatcher;
	protected final StoreWatcher testSourceWatcher;

	protected final StoreClassLoader testClassLoader;

	protected final MethodChangeUpdater methodChangeUpdater;

	public Pipeline(Store classSourceStore, Store testSourceStore, Store binaryStore) {
		this.classSourceStore = checkNotNull(classSourceStore);
		this.testSourceStore = checkNotNull(testSourceStore);
		this.binaryStore = checkNotNull(binaryStore);

		this.task = new PipelineTask();
		this.deferredTask = new DeferredConsumer<>(task);
		this.classSourceWatcher = new StoreWatcher(classSourceStore, StoreFilter.SOURCE);
		this.testSourceWatcher = new StoreWatcher(testSourceStore, StoreFilter.SOURCE);

		this.testDatabase = new TestDatabase();
		this.testClassLoader = new StoreClassLoader(binaryStore, getClass().getClassLoader());
		this.methodChangeUpdater = new MethodChangeUpdater(testDatabase);
	}

	public void start() {
		// Start listening
		classSourceWatcher.registerConsumer(deferredTask);
		testSourceWatcher.registerConsumer(deferredTask);
		classSourceStore.startListening();
		testSourceStore.startListening();
	}

	public void stop() {
		// Stop listening
		classSourceWatcher.unregisterConsumer(deferredTask);
		testSourceWatcher.unregisterConsumer(deferredTask);
		classSourceStore.stopListening();
		testSourceStore.stopListening();
		// TODO Stop current test run as well?
	}

	protected Set<String> handleSourceEvents(List<StoreEvent> events) {
		Set<String> toCompile = new HashSet<String>();
		Set<String> toRemove = new HashSet<String>();
		// Collect resources to compile and remove
		for (StoreEvent event : events) {
			if (event.getType() == StoreEvent.Type.REMOVED) {
				toRemove.add(event.getResourceName());
				toCompile.remove(event.getResourceName());
			} else {
				toCompile.remove(event.getResourceName());
				toRemove.add(event.getResourceName());
			}
		}
		// Remove resources
		for (String removedResourceName : toRemove) {
			binaryStore.remove(removedResourceName);
			// TODO Remove methods from class in database?
			// TODO Remove tests from test class in database?
		}
		return toCompile;
	}

	protected class PipelineTask implements Consumer<List<StoreEvent>> {

		@Override
		public void consume(List<StoreEvent> events) {
			// TODO Distinguish between class and test source events?
			Set<String> classesToCompile = handleSourceEvents(events);
			Set<String> testsToCompile = handleSourceEvents(events);

			// Compile class sources
			JavaCompiler classCompiler = new EclipseCompiler(classSourceStore, binaryStore, testClassLoader);
			CompilationResult result = classCompiler.compile(classesToCompile);
			if (!result.isSuccess()) {
				// TODO Report errors on GUI?
				return;
			}

			// Compile test sources
			JavaCompiler testCompiler = new EclipseCompiler(testSourceStore, binaryStore, testClassLoader);
			result = testCompiler.compile(testsToCompile);
			if (!result.isSuccess()) {
				// TODO Report errors on GUI?
				return;
			}

			// Update database
			methodChangeUpdater.detectChanges(result.getCompiledClasses());

			// TODO Sort and run tests
		}
	}

}
