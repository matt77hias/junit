package kuleuven.group2;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import kuleuven.group2.classloader.ReloadingStoreClassLoader;
import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.MethodTestLinkUpdater;
import kuleuven.group2.data.updating.TestResultUpdater;
import kuleuven.group2.defer.DeferredConsumer;
import kuleuven.group2.policy.TestSortingPolicy;
import kuleuven.group2.rewrite.BinaryStoreTransformFilter;
import kuleuven.group2.rewrite.OssRewriterLoader;
import kuleuven.group2.sourcehandler.ClassSourceEventHandler;
import kuleuven.group2.sourcehandler.SourceEventHandler;
import kuleuven.group2.sourcehandler.TestSourceEventHandler;
import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreEvent;
import kuleuven.group2.store.StoreFilter;
import kuleuven.group2.store.StoreWatcher;
import kuleuven.group2.testrunner.TestRunner;
import kuleuven.group2.util.Consumer;

/**
 * Controls the execution of the complete pipeline.
 * 
 * <p>
 * This is the main entry point for the program. It sets up the whole running
 * environment and controls the flow of information between the different
 * components.
 * </p>
 * 
 * <ul>
 * <li>Sets up {@link StoreWatcher}s on the class and test source stores.</li>
 * <li>Sets up a {@link TestDatabase} for the (intermediate) results.</li>
 * <li>Consumes the produced {@link StoreEvent}s using a deferred task execution
 * strategy.</li>
 * <li>In the first run:
 * <ol>
 * <li>Compile all class and test sources.</li>
 * <li>Initialize the database with the found classes and tests.</li>
 * <li>Sort and run all tests.</li>
 * </ol>
 * </li>
 * <li>In every other run:
 * <ol>
 * <li>Compile changed sources.</li>
 * <li>Update the database to reflect these changes.</li>
 * <li>Sort and run all tests.</li>
 * </ol>
 * </li>
 * </ul>
 * 
 * @author Group2
 * @version 19 November 2013
 */
public class Pipeline {

	protected final Store classSourceStore;
	protected final Store testSourceStore;
	protected final Store binaryStore;
	protected TestSortingPolicy sortPolicy;

	protected final TestDatabase testDatabase;
	protected final ReloadingStoreClassLoader testClassLoader;

	protected final TestRunner testRunner;
	protected final OssRewriterLoader rewriterLoader;
	protected final MethodTestLinkUpdater methodTestLinkUpdater;
	protected final TestResultUpdater testResultUpdater;

	protected final StoreWatcher classSourceWatcher;
	protected final StoreWatcher testSourceWatcher;
	protected final Collection<SourceEventHandler> storeEventConsumers = new HashSet<SourceEventHandler>();

	protected final PipelineTask task;
	protected final DeferredConsumer<StoreEvent> deferredTask;

	public Pipeline(Store classSourceStore, Store testSourceStore, Store binaryStore, TestSortingPolicy sortPolicy) {
		this.classSourceStore = checkNotNull(classSourceStore);
		this.testSourceStore = checkNotNull(testSourceStore);
		this.binaryStore = checkNotNull(binaryStore);
		this.sortPolicy = checkNotNull(sortPolicy);

		this.testDatabase = new TestDatabase();
		this.testClassLoader = new ReloadingStoreClassLoader(binaryStore, getClass().getClassLoader());
		this.testRunner = new TestRunner(testClassLoader);
		this.rewriterLoader = OssRewriterLoader.getInstance();
		rewriterLoader.setTransformFilter(new BinaryStoreTransformFilter(binaryStore));
		this.methodTestLinkUpdater = new MethodTestLinkUpdater(testDatabase, rewriterLoader);
		methodTestLinkUpdater.registerTestHolder(testRunner);
		this.testResultUpdater = new TestResultUpdater(testDatabase);
		testRunner.addRunListener(testResultUpdater);

		this.classSourceWatcher = new StoreWatcher(classSourceStore, StoreFilter.SOURCE);
		this.testSourceWatcher = new StoreWatcher(testSourceStore, StoreFilter.SOURCE);
		SourceEventHandler classSourceEventHandler = new ClassSourceEventHandler(classSourceStore, binaryStore, testDatabase,
				testClassLoader);
		SourceEventHandler testSourceEventHandler = new TestSourceEventHandler(testSourceStore, binaryStore, testDatabase,
				testClassLoader);
		storeEventConsumers.add(classSourceEventHandler);
		storeEventConsumers.add(testSourceEventHandler);

		this.task = new PipelineTask();
		this.deferredTask = new DeferredConsumer<>(task);
	}

	public TestDatabase getTestDatabase() {
		return testDatabase;
	}

	public TestSortingPolicy getSortPolicy() {
		return sortPolicy;
	}

	public void setSortPolicy(TestSortingPolicy sortPolicy) {
		this.sortPolicy = checkNotNull(sortPolicy);
	}

	public void start() {
		// Start listening
		classSourceWatcher.registerConsumer(deferredTask);
		testSourceWatcher.registerConsumer(deferredTask);
		classSourceStore.addStoreListener(classSourceWatcher);
		testSourceStore.addStoreListener(testSourceWatcher);
		classSourceStore.startListening();
		testSourceStore.startListening();
		// Start rewriting
		rewriterLoader.enable();
		// First setup
		firstRun();
	}

	private void firstRun() {
		reloadClasses();

		setupSourceEventHandlers();

		Test[] sortedTests = sortTests();

		runTests(sortedTests);
	}

	private void run(List<StoreEvent> events) {
		reloadClasses();

		handleSourceEvents(events);

		Test[] sortedTests = sortTests();

		runTests(sortedTests);
	}

	private void reloadClasses() {
		testClassLoader.reload();
	}
	
	private void setupSourceEventHandlers() {
		for(SourceEventHandler handler : storeEventConsumers) {
			try{
				handler.setup();
			} catch (Exception e) {
				// TODO show in GUI?
				System.err.println(e.getMessage());
			}
		}
	}

	private void handleSourceEvents(List<StoreEvent> events) {
		for(SourceEventHandler handler : storeEventConsumers) {
			try {
				handler.consume(events);
			} catch (Exception e) {
				// TODO Show in GUI?
				System.err.println(e.getMessage());
			}
		}
			
	}

	private Test[] sortTests() {
		return sortPolicy.getSortedTests(testDatabase);
	}

	private void runTests(Test[] tests) {
		try {
			testRunner.runTestMethods(tests);
		} catch (Exception e) {
			// TODO Show in GUI?
			e.printStackTrace();
		}
	}

	public void stop() {
		// Stop listening
		classSourceWatcher.unregisterConsumer(deferredTask);
		testSourceWatcher.unregisterConsumer(deferredTask);
		classSourceStore.removeStoreListener(classSourceWatcher);
		testSourceStore.removeStoreListener(testSourceWatcher);
		classSourceStore.stopListening();
		testSourceStore.stopListening();
		// Stop rewriting
		rewriterLoader.disable();
		// TODO Stop current test run as well?
	}

	public void shutdown() {
		// Stop
		stop();
		// Shut down
		deferredTask.stopService();
	}

	protected class PipelineTask implements Consumer<List<StoreEvent>> {

		@Override
		public void consume(List<StoreEvent> events) {
			Pipeline.this.run(events);
		}
	}

}
