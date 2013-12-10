package kuleuven.group2.ui.model;

import java.util.Date;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kuleuven.group2.data.TestBatch;
import kuleuven.group2.data.TestRun;

public class TestBatchModel {

	public enum TestBatchState {
		RUNNING, SUCCESS, FAILURE
	}

	private final TestBatch batch;

	private final ObjectProperty<Date> startDate = new SimpleObjectProperty<Date>();
	private final BooleanProperty isRunning = new SimpleBooleanProperty();
	private final ListProperty<TestRunModel> testRuns = new SimpleListProperty<>(
			FXCollections.<TestRunModel> observableArrayList());

	public TestBatchModel(TestBatch batch) {
		this.batch = batch;

		// Fill in properties
		startDate.set(batch.getStartDate());
		isRunning.set(batch.isRunning());
		for (TestRun testRun : batch.getTestRuns()) {
			addTestRun(new TestRunModel(testRun));
		}
	}

	public TestBatch getBatch() {
		return batch;
	}

	public ReadOnlyObjectProperty<Date> startDateProperty() {
		return startDate;
	}

	public boolean isRunning() {
		return isRunningProperty().get();
	}

	public void setRunning(boolean isRunning) {
		isRunningProperty().set(isRunning);
	}

	public BooleanProperty isRunningProperty() {
		return isRunning;
	}

	public ObservableList<TestRunModel> getTestRuns() {
		return testRunsProperty().get();
	}

	public ReadOnlyListProperty<TestRunModel> testRunsProperty() {
		return testRuns;
	}

	protected void addTestRun(TestRunModel testRunModel) {
		testRuns.add(testRunModel);
	}

	public ObjectBinding<Date> endDateProperty() {
		return Bindings.createObjectBinding(new Callable<Date>() {
			@Override
			public Date call() throws Exception {
				return getBatch().getEndDate();
			}
		}, isRunningProperty());
	}

	public LongBinding durationProperty() {
		return Bindings.createLongBinding(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				return getBatch().getDuration();
			}
		}, isRunningProperty());
	}

	public boolean isSuccessful() {
		return isSuccessfulProperty().get();
	}

	public BooleanBinding isSuccessfulProperty() {
		return Bindings.createBooleanBinding(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				for (TestRunModel testRunModel : getTestRuns()) {
					if (!testRunModel.getRun().isSuccessfulRun()) return false;
				}
				return true;
			}
		}, testRunsProperty());
	}

	public ReadOnlyIntegerProperty runsCountProperty() {
		return testRunsProperty().sizeProperty();
	}

	public IntegerBinding failedRunsCountProperty() {
		return Bindings.createIntegerBinding(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int result = 0;
				for (TestRunModel testRunModel : getTestRuns()) {
					if (testRunModel.getRun().isFailedRun()) result++;
				}
				return result;
			}
		}, testRunsProperty());
	}

	public ObjectBinding<TestBatchState> stateProperty() {
		return Bindings.createObjectBinding(new Callable<TestBatchState>() {
			@Override
			public TestBatchState call() throws Exception {
				if (isRunning()) {
					return TestBatchState.RUNNING;
				} else if (isSuccessful()) {
					return TestBatchState.SUCCESS;
				} else {
					return TestBatchState.FAILURE;
				}
			}
		}, isRunningProperty(), isSuccessfulProperty());
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
		return Bindings.createFloatBinding(new Callable<Float>() {
			@Override
			public Float call() throws Exception {
				int failedRuns = failedRunsCountProperty().get();
				int totalRuns = runsCountProperty().get();
				return (totalRuns == 0) ? 0f : ((float) failedRuns) / ((float) totalRuns);
			}
		}, failedRunsCountProperty(), testRunsProperty());
	}

}
