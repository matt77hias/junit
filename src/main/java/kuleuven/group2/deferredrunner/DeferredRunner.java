package kuleuven.group2.deferredrunner;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import kuleuven.group2.deferredrunner.RunnableFactory;

/**
 * A class of deferred runners. If a deferred test runner is called for starting
 * an execution of its runnable implementing object, it will schedule an
 * execution of this runnable object. If an execution is scheduled and not yet
 * started, the scheduled time is delayed when a start is called for. Once the
 * execution is started, it cannot be canceled anymore unless the user forces
 * the deferred test runner to stop immediately.
 * 
 * @author Group 2
 * @version 9 November 2013
 */
public class DeferredRunner {

	/**
	 * Returns the scheduler of this deferred runner.
	 * 
	 * @return The scheduler of this deferred runner.
	 */
	protected ScheduledExecutorService getScheduledExecutorService() {
		return this.scheduler;
	}

	/**
	 * The executor service used for periodically running the tests.
	 */
	private final ScheduledExecutorService scheduler;

	/**
	 * The default delay for delaying the execution (initially & in case of a
	 * new change notification).
	 */
	public static final long DEFAULT_DELAY = 5l;

	/**
	 * The default time unit used for delaying the execution.
	 */
	public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * Checks if the given runnable factory is a valid runnable factory.
	 * 
	 * @param The
	 *            runnable factory that has to be checked.
	 * @return True if and only if the given runnable factory does not refer the
	 *         null reference.
	 */
	public static boolean isValidRunnableFactory(RunnableFactory runnableFactory) {
		return (runnableFactory != null);
	}

	/**
	 * Creates a new deferred runner with given runnable factory and default
	 * delay and time unit.
	 * 
	 * @param runnableFactory
	 *            The runnable factory for this new deferred runner.
	 * @throws IllegalArgumentException
	 *             The given runnable factory must be a valid runnable factory.
	 *             | !isValidRunnableFactory(runnableFactory)
	 */
	public DeferredRunner(RunnableFactory runnableFactory) throws IllegalArgumentException {
		this(runnableFactory, DEFAULT_DELAY, DEFAULT_TIME_UNIT);
	}

	/**
	 * Creates a new deferred runner with given runnable factory and given delay
	 * and given time unit for the execution of this deferred runner's runnable
	 * object.
	 * 
	 * @param runnableFactory
	 *            The runnable factory for this new deferred runner.
	 * @param delay
	 *            The delay for the execution of this new deferred runner's
	 *            runnable object.
	 * @param timeUnit
	 *            The time unit for the delay of this new deferred runner's
	 *            runnable object.
	 * @throws IllegalArgumentException
	 *             The given runnable factory must be a valid runnable factory.
	 *             | !isValidRunnableFactory(runnableFactory)
	 */
	public DeferredRunner(RunnableFactory runnableFactory, long delay, TimeUnit timeUnit)
			throws IllegalArgumentException {

		if (!isValidRunnableFactory(runnableFactory)) {
			throw new IllegalArgumentException("The given runnable factory must be a valid runnable factory.");
		}
		this.runnableFactory = runnableFactory;

		setScheduleParameters(delay, timeUnit);
		this.scheduler = Executors.newScheduledThreadPool(1);
	}

	/*
	 * Runnable management
	 */

	/**
	 * Returns the runnable factory of this deferred runner.
	 * 
	 * @return The runnable factory of this deferred runner.
	 */
	protected RunnableFactory getRunnableFactory() {
		return this.runnableFactory;
	}

	/**
	 * The runnable factory of this deferred runner.
	 */
	private final RunnableFactory runnableFactory;

	/**
	 * Returns the runnable of this deferred runner.
	 * 
	 * @return The runnable of this deferred runner.
	 */
	protected RunTask getRunnable() {
		return this.runnable;
	}

	/**
	 * Creates a new runnable for this deferred runner.
	 */
	protected void createRunnable() {
		this.runnable = new RunTask(getRunnableFactory().createRunnable());
	}

	/**
	 * The runnable object of this deferred runner.
	 */
	private RunTask runnable;

	/*
	 * Pool management
	 */

	/**
	 * Returns the delay for the execution of this deferred runner's runnable
	 * object.
	 * 
	 * @return The delay for the execution of this deferred runner's runnable
	 *         object.
	 */
	public long getDelay() {
		return this.delay;
	}

	/**
	 * The delay for delaying the execution of this deferred runner. (initially
	 * & in case of a new change notification).
	 */
	private long delay;

	/**
	 * Returns the time unit for the delay of the execution of this deferred
	 * runner's runnable object.
	 * 
	 * @return The time unit for the delay of the execution of this deferred
	 *         runner's runnable object.
	 */
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	/**
	 * The time unit used for delaying the execution of this deferred runner.
	 */
	private TimeUnit timeUnit;

	/**
	 * Sets the initial delay and time unit for the execution of this deferred
	 * runner.
	 * 
	 * @param delay
	 *            The delay for the execution of this deferred runner's runnable
	 *            object.
	 * @param timeUnit
	 *            The time unit for the delay of the execution of this deferred
	 *            runner's runnable object.
	 */
	protected void setScheduleParameters(long delay, TimeUnit timeUnit) {
		this.delay = delay;
		this.timeUnit = timeUnit;
	}

	/**
	 * Requests an execution of the runnable object of this deferred runner.
	 * 
	 * If an execution is scheduled and not yet started, the scheduled time will
	 * be delayed. Once the execution is started, it cannot be canceled anymore
	 * unless the user forces the deferred runner to stop immediately.
	 */
	public void start() {
		start(false);
	}

	/**
	 * Requests an execution of the runnable object of this deferred runner.
	 * 
	 * If an execution is scheduled and not yet started, the scheduled time will
	 * be delayed. Once the execution is started, it cannot be canceled anymore
	 * unless the user forces the deferred runner to stop immediately.
	 * 
	 * @param createIfRunning
	 *            If the current runnable of this deferred runner is already
	 *            running, a new runnable is scheduled if this parameter is
	 *            true. No new runnable is scheduled otherwise (this also means
	 *            that the method call will have no effect on this deferred
	 *            runner).
	 */
	public void start(boolean createIfRunning) {
		// Set request flag
		setRequest(true);
		if (isRunning()) {
			// Currently running
			// Runnable will handle request flag
		} else if (isFinished()) {
			// Nothing scheduled
			// Schedule a new task
			scheduleNewRunnable();
		} else {
			// Scheduled but not yet running
			// Reschedule task
			boolean cancelled = delay();
			if (!cancelled & createIfRunning) {
				scheduleNewRunnable();
			}
		}
	}

	/**
	 * Schedules a new runnable for this deferred test runner.
	 */
	protected void scheduleNewRunnable() {
		createRunnable();
		scheduleRunnable();
	}

	/**
	 * Checks if the current runnable object of this deferred runner is running.
	 * 
	 * @return True if and only if the current runnable of this deferred runner
	 *         is running.
	 */
	public boolean isRunning() {
		return getRunnable() != null && getRunnable().isRunning() && !isFinished();
	}

	/**
	 * Checks if the currently scheduled task of this deferred runner is
	 * finished.
	 * 
	 * @return True if and only if the currently scheduled task of this deferred
	 *         runner is finished.
	 */
	public boolean isFinished() {
		return getScheduledFuture() == null || getScheduledFuture().isDone();
	}

	/**
	 * Delays the scheduled execution if the current runnable of this deferred
	 * runner is not yet running.
	 * 
	 * @return False if the task could not be cancelled, true otherwise.
	 */
	public boolean delay() {
		if (getScheduledFuture() != null) {
			boolean cancelled = getScheduledFuture().cancel(false);

			if (cancelled) {
				scheduleRunnable();
			}

			return cancelled;
		}
		// No task, so cancellation succeeded
		return true;
	}

	/**
	 * Stops the execution of the current runnable of this deferred runner even
	 * if the current runnable is running.
	 */
	public void stop() {
		// Unset request flag
		setRequest(false);
		// Cancel currently running or scheduled task
		if (getScheduledFuture() != null) {
			getScheduledFuture().cancel(false);
		}
	}

	/**
	 * Stops the service of this deferred runner immediately.
	 * 
	 * If an infinite runnable is scheduled and running, and you force a new
	 * runnable to be created. There's no way for you to stop the old runnable,
	 * because the deferred runner doesn't support such silly things. The only
	 * support the deferred runner gives you in this case is shutting down its
	 * thread pool immediately.
	 */
	public void stopService() {
		getScheduledExecutorService().shutdownNow();
	}

	/**
	 * Returns the scheduled future of this deferred runner.
	 */
	protected ScheduledFuture<?> getScheduledFuture() {
		return this.scheduledFuture;
	}

	/**
	 * Sets the scheduled future of this deferred runner to the given request.
	 */
	protected void setScheduledFuture(ScheduledFuture<?> request) {
		this.scheduledFuture = request;
	}

	/**
	 * The scheduled future of the runnable of this deferred runner.
	 */
	private ScheduledFuture<?> scheduledFuture;

	/**
	 * Schedules the runnable of this deferred runner.
	 * 
	 * @pre May not be called when no runnable is created.
	 */
	protected void scheduleRunnable() {
		try {
			setScheduledFuture(getScheduledExecutorService().schedule(getRunnable(), getDelay(), getTimeUnit()));
		} catch (RejectedExecutionException e) {
			System.out.println(e.getMessage());
		}
	}

	protected boolean hasRequest() {
		return hasRequest.get();
	}

	protected boolean setRequest(boolean request) {
		return hasRequest.getAndSet(request);
	}

	private AtomicBoolean hasRequest = new AtomicBoolean(false);

	/**
	 * 
	 */
	protected class RunTask implements Runnable {

		private final Runnable child;
		private volatile boolean running;

		public RunTask(Runnable child) {
			this.running = false;
			this.child = child;
		}

		@Override
		public void run() {
			try {
				boolean shouldStart = preRun();
				if (shouldStart) {
					this.child.run();
				}
			} finally {
				postRun();
			}
		}

		protected boolean isRunning() {
			return this.running;
		}

		/**
		 * Prepare the run.
		 * 
		 * <ol>
		 * <li>Mark the task as running.</li>
		 * <li>Reset the request flag to indicate that this task will handle it.
		 * </li>
		 * </ol>
		 * Only start the task if the request flag was set previously.
		 * 
		 * @return True if and only if the task should still be run.
		 */
		protected boolean preRun() {
			this.running = true;
			return setRequest(false);
		}

		/**
		 * Finish the run.
		 * 
		 * <ol>
		 * <li>If there are new requests, schedule a new task.</li>
		 * <li>Mark the task as not running.</li>
		 * </ol>
		 */
		protected void postRun() {
			if (hasRequest()) {
				scheduleRunnable();
			}
			this.running = false;
		}
	}
}