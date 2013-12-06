package kuleuven.group2.ui.model;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestRun;

public class TestRunModel {

	private final TestRun run;

	private final StringProperty testClassName = new SimpleStringProperty();
	private final StringProperty testMethodName = new SimpleStringProperty();
	private final BooleanProperty successfulRun = new SimpleBooleanProperty();
	private final BooleanProperty failedRun = new SimpleBooleanProperty();
	private final ObjectProperty<Date> timestamp = new SimpleObjectProperty<Date>();
	private final ObjectProperty<Throwable> exception = new SimpleObjectProperty<Throwable>();
	private final ObjectProperty<StackTraceElement[]> trace = new SimpleObjectProperty<StackTraceElement[]>();

	public TestRunModel(TestRun testRun) {
		this.run = testRun;

		// Fill in properties
		testClassName.set(testRun.getTest().getTestClassName());
		testMethodName.set(testRun.getTest().getTestMethodName());
		successfulRun.set(testRun.isSuccessfulRun());
		failedRun.set(testRun.isFailedRun());
		timestamp.set(testRun.getTimestamp());
		if (failedRun.get()) {
			exception.set(testRun.getException());
			trace.set(testRun.getTrace());
		}
	}

	public TestRun getRun() {
		return run;
	}

	public Test getTest() {
		return getRun().getTest();
	}

	public StringProperty testClassNameProperty() {
		return testClassName;
	}

	public StringProperty testMethodNameProperty() {
		return testMethodName;
	}

	public BooleanProperty successfulRunProperty() {
		return successfulRun;
	}

	public BooleanProperty failedRunProperty() {
		return failedRun;
	}

	public ObjectProperty<Date> timestampProperty() {
		return timestamp;
	}

	public ObjectProperty<Throwable> exceptionProperty() {
		return exception;
	}

	public ObjectProperty<StackTraceElement[]> traceProperty() {
		return trace;
	}

}
