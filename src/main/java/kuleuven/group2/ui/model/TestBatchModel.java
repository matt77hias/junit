package kuleuven.group2.ui.model;

import java.util.Date;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
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

	public BooleanBinding isSuccessfulProperty() {
		return new BooleanBinding() {
			{
				super.bind(testRunsProperty());
			}

			@Override
			protected boolean computeValue() {
				for (TestRunModel testRunModel : testRunsProperty().get()) {
					if (!testRunModel.getRun().isSuccessfulRun()) return false;
				}
				return true;
			}
		};
	}

	public ReadOnlyIntegerProperty runsCountProperty() {
		return testRunsProperty().sizeProperty();
	}

	public IntegerBinding failedRunsCountProperty() {
		return new IntegerBinding() {
			{
				super.bind(testRunsProperty());
			}

			@Override
			protected int computeValue() {
				int result = 0;
				for (TestRunModel testRunModel : testRunsProperty().get()) {
					if (testRunModel.getRun().isFailedRun()) result++;
				}
				return result;
			}
		};
	}

	public NumberBinding failureFractionProperty() {
		/*
		 * Note: a bug in JavaFX causes the division to be evaluated when
		 * constructing the when binding, throwing an ArithmeticException.
		 * 
		 * As a workaround, we implement the conditional division ourselves.
		 */
		// Bindings.when(runsCountProperty().isEqualTo(0)).then(0)
		// .otherwise(failedRunsCountProperty().divide(runsCountProperty()));

		return new FloatBinding() {
			{
				super.bind(failedRunsCountProperty());
				super.bind(testRunsProperty());
			}

			@Override
			protected float computeValue() {
				int failedRuns = failedRunsCountProperty().get();
				int totalRuns = runsCountProperty().get();
				return (totalRuns == 0) ? 0f : ((float) failedRuns) / ((float) totalRuns);
			}
		};
	}

}
