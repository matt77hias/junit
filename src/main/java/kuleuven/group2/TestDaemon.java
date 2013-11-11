package kuleuven.group2;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

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
public class TestDaemon {
	
	/**
	 * The executor service used for periodically running the tests.
	 */
	private final ScheduledExecutorService scheduler;
	
	/**
	 * The default delay for delaying the execution
	 * (initially & in case of a new change notification).
	 */
	public static final long DEFAULT_DELAY = 30l;
	
	/**
	 * The default time unit used for delaying the execution.
	 */
	public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
	
	/**
	 * The JUnitCore facade used for building a test runner
	 * for the test suite and running that same test runner
	 * for the test suite of this test deamon.
	 */
	private final JUnitCore jUnitCore;
	
	/**
	 * Creates a new test daemon with default delay time unit.
	 */
	public TestDaemon() {
		this(DEFAULT_DELAY, DEFAULT_TIME_UNIT);
	}
	
	/**
	 * Creates a new test daemon with given delay and given time unit
	 * for the execution of this test deamon's test suite.
	 * 
	 * @param	delay
	 * 			The delay for the execution of this new test daemon's test suite.
	 * @param	timeUnit
	 * 			The time unit for the delay of the execution
	 * 			of this new test daemon's test suite.
	 */
	public TestDaemon(long delay, TimeUnit timeUnit) {
		this.jUnitCore = new JUnitCore();
		setScheduleParameters(delay, timeUnit);
		this.scheduler = Executors.newScheduledThreadPool(1);
	}
	
	/*
	 * Pool management
	 */
	
	/**
	 * The delay for delaying the execution of this test daemon.
	 * (initially & in case of a new change notification).
	 */
	private long delay;
	
	/**
	 * The time unit used for delaying the execution of this test daemon.
	 */
	private TimeUnit timeUnit;
	
	/**
	 * Sets the initial delay and time unit for the execution of this
	 * test daemon.
	 * 
	 * @param	delay
	 * 			The delay for the execution of this test daemon's test suite.
	 * @param	timeUnit
	 * 			The time unit for the delay of the execution
	 * 			of this test daemon's test suite.
	 */
	private void setScheduleParameters(long delay, TimeUnit timeUnit) {
		this.delay = delay;
		this.timeUnit = timeUnit;
	}
	
	/**
	 * Returns the delay for the execution of this test daemon's test suite.
	 * 
	 * @return	The delay for the execution of this test daemon's test suite.
	 */
	public long getDelay() {
		return this.delay;
	}
	
	/**
	 * Returns the time unit for the delay of the execution 
	 * of this test daemon's test suite.
	 * 
	 * @return	The time unit for the delay of the execution
	 * 			of this test daemon's test suite.
	 */
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}
	
	/**
	 * The run task of this test deamon.
	 */
	private RunTask runTask;
	
	/**
	 * Requests an execution of the test suite of this test daemon.
	 * 
	 * If an execution is scheduled and not yet started, the scheduled time
	 * will be delayed. Once the execution is started, it cannot be canceled
	 * anymore unless the user forces the test daemon to stop immediately.
	 */
	public synchronized void start() {
		if (currentRunTaskFinished()) {
			createRunTask();
			scheduleRunTask();
		} else {
			delay();
			// TODO in case of running? forget call?
		}
	}
	
	/**
	 * Checks if the current run task is finished.
	 * 
	 * @return	True if and only if the current run task is finished.
	 */
	public boolean currentRunTaskFinished() {
		if (this.scheduledFuture == null) {
			return true;
		} else {
			try {
				boolean notFinished = (this.scheduledFuture.get() != null);
				return notFinished;
			} catch (CancellationException e) {
				return true;
			} catch (InterruptedException e) {
				return true;
			} catch (ExecutionException e) {
				return true;
			}
		}
	}
	
	/**
	 * Delays the scheduled execution if the current run task is not yet running.
	 */
	public void delay() {
		if (this.scheduledFuture != null) {
			boolean cancelled = this.scheduledFuture.cancel(false);
			if (cancelled) {
				scheduleRunTask();
			}
		}
	}
	
	/**
	 * Stops the execution of tests even if the currenr run task is running.
	 */
	public void stop() {
		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(true);
		}
	}
	
	/**
	 * Creates a new run task for this test daemon.
	 */
	private void createRunTask() {
		this.runTask = new RunTask();
	}
	
	/**
	 * Returns the run task of this test daemon.
	 * 
	 * @return	The run task of this test daemon.
	 */
	private RunTask getRunTask() {
		return this.runTask;
	}
	
	/**
	 * The scheduled future of the run task of this test daemon.
	 */
	private ScheduledFuture<?> scheduledFuture;
	
	/**
	 * Schedules the run task of this test daemon.
	 * 
	 * @pre		May not be called when no run task is created.
	 */
	private void scheduleRunTask() {
		try {
			this.scheduledFuture = this.scheduler.schedule(getRunTask(), getDelay(), getTimeUnit());
		} catch (RejectedExecutionException e){
			System.out.println(e.getMessage());
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
}
