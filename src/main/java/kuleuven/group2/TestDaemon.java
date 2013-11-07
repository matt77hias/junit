package kuleuven.group2;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

/**
 * A class of test daemons for running tests periodically.
 */
public class TestDaemon {
	
	/**
	 * Starts a test daemon.
	 * 
	 * @param 	args
	 * 			Not used arguments.
	 */
	public static void main(String[] args) {
		new TestDaemon();
	}
	
	/**
	 * The executor service used for periodically running the tests.
	 */
	private final ScheduledExecutorService scheduler;
	
	/**
	 * The default initial delay for delaying the first execution. [sec]
	 */
	public static final long DEFAULT_INITIAL_DELAY = 30l;
	
	/**
	 * The default period between successive executions. [sec]
	 */
	public static final long DEFAULT_PERIOD = 30l;
	
	/**
	 * The run task of this test deamon.
	 */
	private RunTask runTask;
	
	/**
	 * The JUnitCore facade used for building a test runner
	 * for the test suite and running that same test runner
	 * for the test suite of this test deamon.
	 */
	private final JUnitCore jUnitCore;
	
	/**
	 * The class that contains the test suite that has to be run.
	 */
	private Class<?> testSuite; 
	
	/**
	 * Creates a new test daemon with default initial delay
	 * and default period.
	 */
	public TestDaemon() {
		this(DEFAULT_INITIAL_DELAY, DEFAULT_PERIOD);
	}
	
	/**
	 * Creates a new test daemon with given initial delay
	 * and given period.
	 * 
	 * @param	initialDelay
	 * 			The initial delay for this new test daemon.
	 * @param	period
	 * 			The period for this new test daemon.
	 * @throws	RejectedExecutionException
	 * 			If the run task cannot be scheduled for execution.
	 * @throws	IllegalArgumentException
	 * 			if the given period less than or equal to zero.
	 */
	public TestDaemon(long initialDelay, long period) 
		throws RejectedExecutionException, IllegalArgumentException{
		this.jUnitCore = new JUnitCore();
		this.runTask = new RunTask();
		this.scheduler = Executors.newScheduledThreadPool(1);
		this.scheduler.scheduleAtFixedRate(this.runTask, initialDelay, period, TimeUnit.SECONDS);
	}
	
	/**
	 * Sets the class with the test suite of this test daemon
	 * to the given class.
	 * 
	 * @param 	testSuite
	 * 			The class with the test suite.
	 */
	public void setTestSuite(Class<?> testSuite) {
		if (!this.runTask.isRunning) {
			this.testSuite = testSuite;
		} else {
			this.notYetAcceptedClass = testSuite;
		}
	}
	
	/**
	 * The not yet accepted class to set to the class of this test daemon after the current test.
	 */
	private Class<?> notYetAcceptedClass;
	
	/**
	 * Sets the not yet accepted class to the class with the test suite
	 * of this test daemon.
	 */
	private void classSetUp() {
		if (this.notYetAcceptedClass != null) { // TODO: Needs to be atomic
			this.testSuite = this.notYetAcceptedClass;
		}
	}
	
	/**
	 * Returns the class with the test suite of this test daemon.
	 * 
	 * @return	The class with the test suite of this test daemon.
	 */
	public Class<?> getTestSuite() {
		return this.testSuite;
	}
	
	/**
     * Add a listener to be notified as the tests run.
     *
     * @param 	listener 
     * 			the listener to add
     * @see 	org.junit.runner.notification.RunListener
     */
	public void addListener(RunListener listener) {
		 if (!this.runTask.isRunning) { // TODO: Needs to be atomic
			 this.jUnitCore.addListener(listener);
		 } else {
			 this.notYetAcceptedListeners.add(listener);
		 }
	}
	
	/**
	 * The not yet accepted listeners who needs to be registered after the current run.
	 */
	private Set<RunListener> notYetAcceptedListeners = new HashSet<RunListener>();
	
	/**
	 * Adds the not yet accepted listeners.
	 */
	private void listenerSetUp() {
		for (RunListener listener : notYetAcceptedListeners) {
			addListener(listener);
		}
		notYetAcceptedListeners = new HashSet<RunListener>();
	}
	
	private void setUp() {
		classSetUp();
		listenerSetUp();
	}
    
	/**
	 * A class of run tasks for running the current test suite of the test daemon.
	 */
    private class RunTask implements Runnable {
    	
    	/**
    	 * Is this run task running.
    	 */
    	private boolean isRunning;
    	
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
			
			setUp();
			
			if (getTestSuite() != null) {
				jUnitCore.run(getTestSuite());
			}
			
			this.isRunning = false;
		}
    }
}
