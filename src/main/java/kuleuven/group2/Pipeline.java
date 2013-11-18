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
import kuleuven.group2.filewatch.SourceEvent;
import kuleuven.group2.filewatch.SourceWatcher;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreClassLoader;
import kuleuven.group2.util.Consumer;

public class Pipeline {

	protected final Store sourceStore;
	protected final Store binaryStore;
	protected final TestDatabase testDatabase;

	protected final PipelineTask task;
	protected final DeferredConsumer<SourceEvent> deferredTask;
	protected final SourceWatcher sourceWatcher;

	protected final StoreClassLoader testClassLoader;
	protected final JavaCompiler compiler;

	protected final MethodChangeUpdater methodChangeUpdater;

	public Pipeline(Store sourceStore, Store binaryStore) {
		this.sourceStore = checkNotNull(sourceStore);
		this.binaryStore = checkNotNull(binaryStore);

		this.task = new PipelineTask();
		this.deferredTask = new DeferredConsumer<>(task);
		this.sourceWatcher = new SourceWatcher();

		this.testDatabase = new TestDatabase();
		this.testClassLoader = new StoreClassLoader(binaryStore, getClass().getClassLoader());
		this.compiler = new EclipseCompiler(sourceStore, binaryStore);
		this.methodChangeUpdater = new MethodChangeUpdater(testDatabase);
	}

	public void start() {
		sourceWatcher.registerConsumer(deferredTask);
		// TODO Start listening on source store?
	}

	public void stop() {
		sourceWatcher.unregisterConsumer(deferredTask);
	}

	protected Set<String> handleSourceEvents(List<SourceEvent> events) {
		Set<String> toCompile = new HashSet<String>();
		Set<String> toRemove = new HashSet<String>();
		// Collect resources to compile and remove
		for (SourceEvent event : events) {
			if (event.getType() == SourceEvent.Type.REMOVED) {
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
		}
		return toCompile;
	}

	protected class PipelineTask implements Consumer<List<SourceEvent>> {

		@Override
		public void consume(List<SourceEvent> events) {
			Set<String> toCompile = handleSourceEvents(events);
			CompilationResult result = compiler.compile(toCompile, testClassLoader);
			if (!result.isSuccess()) {
				// TODO Report errors on GUI?
				return;
			}
			methodChangeUpdater.detectChanges(result.getCompiledClasses());
			// TODO Sort and run tests
		}

	}

}
