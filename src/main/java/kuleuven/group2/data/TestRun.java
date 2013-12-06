package kuleuven.group2.data;

import java.util.Date;

import org.junit.runner.notification.Failure;

/**
 * Represents the running of a certain test, at a particular time and with a
 * particular result.
 * 
 * @author Group2
 * @version 7 November 2013
 */
public class TestRun {

	private final Test test;
	private final Date timestamp;
	private final Failure failure;

	protected TestRun(Test test, Date timestamp, Failure failure) {
		this.test = test;
		this.timestamp = timestamp;
		this.failure = failure;
	}

	public static TestRun createSuccessful(Test test, Date timestamp) {
		return new TestRun(test, timestamp, null);
	}

	public static TestRun createFailed(Test test, Date timestamp, Failure failure) {
		return new TestRun(test, timestamp, failure);
	}
	
	public Test getTest() {
		return test;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	protected Failure getFailure() {
		return this.failure;
	}

	public boolean isSuccessfulRun() {
		return getFailure() == null;
	}

	public boolean isFailedRun() {
		return !isSuccessfulRun();
	}

	public Throwable getException() {
		if (isSuccessfulRun()) {
			throw new IllegalStateException("Cannot get exception of successful run.");
		}
		return getFailure().getException();
	}

	public StackTraceElement getTraceTop() throws IllegalStateException {
		return getTrace()[0];
	}

	public StackTraceElement[] getTrace() throws IllegalStateException {
		if (isSuccessfulRun()) {
			throw new IllegalStateException("Cannot get stack trace of successful run.");
		}
		return getException().getStackTrace();
	}
}
