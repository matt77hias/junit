package kuleuven.group2.testrunner;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * A runner which runs a single failure.
 * 
 * @author Group 2
 * @version 18 December 2013
 */
public class FailureRunner extends Runner {

	private final Failure failure;

	public FailureRunner(Failure failure) {
		this.failure = failure;
	}

	@Override
	public Description getDescription() {
		return failure.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		notifier.fireTestStarted(getDescription());
		notifier.fireTestFailure(failure);
		notifier.fireTestFinished(getDescription());
	}

}
