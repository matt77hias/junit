package kuleuven.group2.data;

import java.util.Date;

import org.junit.runner.notification.Failure;

public class TestRun {

	private final Date timestamp;
	private final Failure failure;

	protected TestRun(Date timestamp, Failure failure) {
		this.timestamp = timestamp;
		this.failure = failure;
	}

	public static TestRun createSuccessful(Date timestamp) {
		return new TestRun(timestamp, null);
	}

	public static TestRun createFailed(Date timestamp, Failure failure) {
		return new TestRun(timestamp, failure);
	}

	public Date getTimeStamp() {
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

	public StackTraceElement getTraceTop() 
			throws IllegalStateException {
		return getTrace()[0];
	}

	public StackTraceElement[] getTrace() 
			throws IllegalStateException {
		if (isSuccessfulRun()) {
			throw new IllegalStateException("Cannot get stack trace of successful run.");
		}
		return getFailure().getException().getStackTrace();
	}
}
