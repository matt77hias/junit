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
import kuleuven.group2.sourcehandler.ClassSourceEventHandler;
import kuleuven.group2.sourcehandler.SourceEventHandler;
import kuleuven.group2.sourcehandler.TestSourceEventHandler;
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
	protected final StoreClassLoader testClassLoader;

	protected final StoreWatcher classSourceWatcher;
	protected final StoreWatcher testSourceWatcher;
	protected final SourceEventHandler classSourceEventHandler;
	protected final SourceEventHandler testSourceEventHandler;

	protected final PipelineTask task;
	protected final DeferredConsumer<StoreEvent> deferredTask;

	public Pipeline(Store classSourceStore, Store testSourceStore, Store binaryStore) {
		this.classSourceStore = checkNotNull(classSourceStore);
		this.testSourceStore = checkNotNull(testSourceStore);
		this.binaryStore = checkNotNull(binaryStore);
		this.testDatabase = new TestDatabase();
		this.testClassLoader = new StoreClassLoader(binaryStore, getClass().getClassLoader());

		this.classSourceWatcher = new StoreWatcher(classSourceStore, StoreFilter.SOURCE);
		this.testSourceWatcher = new StoreWatcher(testSourceStore, StoreFilter.SOURCE);
		this.classSourceEventHandler = new ClassSourceEventHandler(classSourceStore, binaryStore, testDatabase,
				testClassLoader);
		this.testSourceEventHandler = new TestSourceEventHandler(testSourceStore, binaryStore, testDatabase,
				testClassLoader);

		this.task = new PipelineTask();
		this.deferredTask = new DeferredConsumer<>(task);
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

	protected class PipelineTask implements Consumer<List<StoreEvent>> {

		@Override
		public void consume(List<StoreEvent> events) {
			// Handle events
			try {
				classSourceEventHandler.handleEvents(events);
				testSourceEventHandler.handleEvents(events);
			} catch (Exception e) {
				// TODO Show in GUI?
				System.err.println(e.getMessage());
			}

			// TODO Sort and run tests
		}
	}

}
