package kuleuven.group2.testrunner;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import kuleuven.group2.deferredrunner.DeferredRunner;
import kuleuven.group2.deferredrunner.RunnableFactory;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

/**
 * A class of test daemons for running tests. If the test daemon is notified of
 * changes in its test suite, which it guards, it will schedule an execution of this
 * test suite. If an execution is scheduled and not yet started, the scheduled time
 * is delayed when a new change is notified. Once the execution is started, it cannot
 * be canceled anymore unless the user forces the test daemon to stop immediately.
 * 
 * @author	Group 2
 * @version 9 November 2013
 */
public class TestDaemon implements RunnableFactory {
	
	/**
	 * The deferred runner of this test daemon.
	 */
	private DeferredRunner deferredRunner;
	
	/**
	 * The JUnitCore facade used for building a test runner
	 * for the test suite and running that same test runner
	 * for the test suite of this test deamon.
	 */
	private final JUnitCore jUnitCore;
	
	/**
	 * Creates a new test daemon.
	 */
	public TestDaemon() {
		this(null);
	}
	
	/**
	 * Creates a new test daemon with given deferred runner.
	 * 
	 * @param	deferredRunner
	 * 			The deferred runner of this test daemon.
	 * @note	You probably want to call createDeferredRunner()
	 * 			afterwards.
	 */
	public TestDaemon(DeferredRunner deferredRunner) {
		this.deferredRunner = deferredRunner;
		this.jUnitCore = new JUnitCore();
	}
	
	/**
	 * Creates a deferred runner for this test daemon.
	 */
	public void createDeferredRunner() {
		this.deferredRunner =  new DeferredRunner(this);
	}
	
	/**
	 * Returns the deferred runner of this test daemon.
	 */
	private DeferredRunner getDeferredRunner() {
		return this.deferredRunner;
	}
	
	/**
	 * Starts this test daemon.
	 */
	public void start() {
		if (getDeferredRunner() != null) {
			getDeferredRunner().start();
		}
	}
	
	/**
	 * Stops this test daemon immediately.
	 */
	public void stop() {
		if (getDeferredRunner() != null) {
			getDeferredRunner().stop();
		}
	}
	
	/*
	 * Test suite and listeners management
	 */
	
	/**
	 * Sets the class with the test suite of this test daemon
	 * to the given class.
	 * 
	 * @param 	testSuite
	 * 			The class with the test suite.
	 */
	public void setTestSuite(Class<?> testSuite) {
		classUpdater.set(this, testSuite);
	}
	
	/**
	 * The class that contains the test suite that has to be run.
	 */
	@SuppressWarnings("unused") //Stupid Eclipse
	private volatile Class<?> testSuite; 
	
	@SuppressWarnings("rawtypes")
	private static AtomicReferenceFieldUpdater<TestDaemon, Class> classUpdater 
		= AtomicReferenceFieldUpdater.newUpdater(TestDaemon.class, Class.class, "testSuite");
	
	/**
	 * Returns the class with the test suite of this test daemon.
	 * 
	 * @return	The class with the test suite of this test daemon.
	 */
	public Class<?> getTestSuite() {
		return classUpdater.get(this);
	}
	
	/**
     * Add a listener to be notified as the tests run.
     *
     * @param 	listener 
     * 			the listener to add
     * @see 	org.junit.runner.notification.RunListener
     */
	public void addListener(RunListener listener) {
		 if (!this.runTask.isRunning) {
			 this.jUnitCore.addListener(listener);
		 }
	}
	
	/*
	 * Run task management
	 */
    
	/**
	 * A class of run tasks for running the current test suite of the test daemon.
	 */
    private class RunTask implements Runnable {
    	
    	/**
    	 * Is this run task running the test suite.
    	 */
    	private volatile boolean isRunning;
    	
    	/**
    	 * Creates a new run task.
    	 */
    	public RunTask() {
    		
    	}
    	
    	/**
    	 * Runs this run task.
    	 */
		public void run() {
			
			this.isRunning = true;
			
			try {
				jUnitCore.run(getTestSuite());
			} catch (NullPointerException e){
				
			} finally {
				this.isRunning = false;
			}
		}
    }
    
    /**
     * The run task of this deferred runner.
     */
    private RunTask runTask;

    /**
	 * Creates a new runnable.
	 * 
	 * @return	The new runnable.
	 */
	public Runnable createRunnable() {
		this.runTask = new RunTask();
		return this.runTask;
	}
}
