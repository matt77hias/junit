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

	private final Test test;
	private final TestRun run;

	private final StringProperty testClassName = new SimpleStringProperty();
	private final StringProperty testMethodName = new SimpleStringProperty();
	private final BooleanProperty successfulRun = new SimpleBooleanProperty();
	private final BooleanProperty failedRun = new SimpleBooleanProperty();
	private final ObjectProperty<Date> timeStamp = new SimpleObjectProperty<Date>();
	private final ObjectProperty<Throwable> exception = new SimpleObjectProperty<Throwable>();
	private final ObjectProperty<StackTraceElement[]> trace = new SimpleObjectProperty<StackTraceElement[]>();

	public TestRunModel(Test test, TestRun testRun) {
		this.test = test;
		this.run = testRun;

		// Fill in properties
		testClassName.set(test.getTestClassName());
		testMethodName.set(test.getTestMethodName());
		successfulRun.set(testRun.isSuccessfulRun());
		failedRun.set(testRun.isFailedRun());
		timeStamp.set(testRun.getTimeStamp());
		if (failedRun.get()) {
			exception.set(testRun.getException());
			trace.set(testRun.getTrace());
		}
	}

	public Test getTest() {
		return test;
	}

	public TestRun getRun() {
		return run;
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

	public ObjectProperty<Date> timeStampProperty() {
		return timeStamp;
	}

	public ObjectProperty<Throwable> exceptionProperty() {
		return exception;
	}

	public ObjectProperty<StackTraceElement[]> traceProperty() {
		return trace;
	}

}
