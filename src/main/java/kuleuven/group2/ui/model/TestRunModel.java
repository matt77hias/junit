package kuleuven.group2.ui.model;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
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

	public ReadOnlyStringProperty testClassNameProperty() {
		return testClassName;
	}

	public ReadOnlyStringProperty testMethodNameProperty() {
		return testMethodName;
	}

	public ReadOnlyBooleanProperty successfulRunProperty() {
		return successfulRun;
	}

	public ReadOnlyBooleanProperty failedRunProperty() {
		return failedRun;
	}

	public ReadOnlyObjectProperty<Date> timestampProperty() {
		return timestamp;
	}

	public ReadOnlyObjectProperty<Throwable> exceptionProperty() {
		return exception;
	}

	public ReadOnlyObjectProperty<StackTraceElement[]> traceProperty() {
		return trace;
	}

}
