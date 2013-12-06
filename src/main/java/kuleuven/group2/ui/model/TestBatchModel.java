package kuleuven.group2.ui.model;

import java.util.Date;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import kuleuven.group2.data.TestBatch;
import kuleuven.group2.data.TestRun;

public class TestBatchModel {

	private final TestBatch batch;

	private final ObjectProperty<Date> timestamp = new SimpleObjectProperty<Date>();
	private final ListProperty<TestRunModel> testRuns = new SimpleListProperty<>(
			FXCollections.<TestRunModel> observableArrayList());

	public TestBatchModel(TestBatch batch) {
		this.batch = batch;

		// Fill in properties
		timestamp.set(batch.getTimestamp());
		for (TestRun testRun : batch.getTestRuns()) {
			addTestRun(new TestRunModel(testRun));
		}
	}

	public TestBatch getBatch() {
		return batch;
	}

	public ObjectProperty<Date> timestampProperty() {
		return timestamp;
	}

	public ListProperty<TestRunModel> testRunsProperty() {
		return testRuns;
	}

	protected void addTestRun(TestRunModel testRunModel) {
		testRuns.add(testRunModel);
	}

}
