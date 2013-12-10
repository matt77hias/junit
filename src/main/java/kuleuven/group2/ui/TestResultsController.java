package kuleuven.group2.ui;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import kuleuven.group2.ui.model.TestBatchModel;
import kuleuven.group2.ui.model.TestBatchModel.TestBatchState;
import kuleuven.group2.ui.model.TestRunModel;
import kuleuven.group2.ui.util.BooleanImageCellFactory;
import kuleuven.group2.ui.util.EnumImageCellFactory;
import kuleuven.group2.ui.util.FormattedDateCellFactory;
import kuleuven.group2.ui.util.FormattedDurationCellFactory;
import kuleuven.group2.ui.util.FormattedNumberCellFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class TestResultsController {

	/*
	 * Date format for time stamp
	 */
	protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/*
	 * Images for test results
	 */
	protected static final Image IMAGE_BATCH_RUNNING = new Image(TestResultsController.class.getResource(
			"icons/batch-run.png").toExternalForm());
	protected static final Image IMAGE_BATCH_SUCCESS = new Image(TestResultsController.class.getResource(
			"icons/batch-ok.png").toExternalForm());
	protected static final Image IMAGE_BATCH_FAILURE = new Image(TestResultsController.class.getResource(
			"icons/batch-err.png").toExternalForm());
	protected static final Image IMAGE_TEST_SUCCESS = new Image(TestResultsController.class.getResource(
			"icons/test-ok.png").toExternalForm());
	protected static final Image IMAGE_TEST_FAILURE = new Image(TestResultsController.class.getResource(
			"icons/test-err.png").toExternalForm());

	/*
	 * Components
	 */

	@FXML
	private TableView<TestBatchModel> batchesTable;

	@FXML
	private TableView<TestRunModel> runsTable;

	@FXML
	private Pane selectedRunDetail;

	@FXML
	private Label selectedRunTitle;

	@FXML
	private Label selectedRunTimestamp;

	@FXML
	private Label selectedRunExceptionLabel;

	@FXML
	private Label selectedRunException;

	@FXML
	private Label selectedRunTraceLabel;

	@FXML
	private TextArea selectedRunTrace;

	/*
	 * Properties
	 */

	private final ListProperty<TestBatchModel> batches = new SimpleListProperty<>(
			FXCollections.<TestBatchModel> observableArrayList());
	private final ObjectProperty<TestBatchModel> selectedBatch = new SimpleObjectProperty<>();
	private final ObjectProperty<TestRunModel> selectedRun = new SimpleObjectProperty<>();

	public ListProperty<TestBatchModel> batchesProperty() {
		return batches;
	}

	public ObjectProperty<TestBatchModel> selectedBatchProperty() {
		return selectedBatch;
	}

	public ListBinding<TestRunModel> runsProperty() {
		return new ListBinding<TestRunModel>() {
			{
				super.bind(selectedBatchProperty());
			}

			@Override
			protected ObservableList<TestRunModel> computeValue() {
				TestBatchModel selectedBatch = selectedBatchProperty().get();
				if (selectedBatch == null) {
					return FXCollections.emptyObservableList();
				}
				return selectedBatchProperty().get().testRunsProperty();
			}
		};
	}

	public ObjectProperty<TestRunModel> selectedRunProperty() {
		return selectedRun;
	}

	public BooleanBinding selectedRun_isSuccessful() {
		return Bindings.selectBoolean(selectedRunProperty(), "successfulRun");
	}

	public BooleanBinding selectedRun_isFailed() {
		return Bindings.selectBoolean(selectedRunProperty(), "failedRun");
	}

	public ObjectBinding<Image> selectedRun_resultImage() {
		return new When(selectedRun_isSuccessful()).then(IMAGE_TEST_SUCCESS).otherwise(IMAGE_TEST_FAILURE);
	}

	public ObjectBinding<ImageView> selectedRun_resultImageView() {
		return Bindings.createObjectBinding(new Callable<ImageView>() {
			@Override
			public ImageView call() throws Exception {
				return new ImageView(selectedRun_resultImage().get());
			}
		}, selectedRunProperty());
	}

	public StringBinding selectedRun_testClassName() {
		return Bindings.selectString(selectedRunProperty(), "testClassName");
	}

	public StringBinding selectedRun_testMethodName() {
		return Bindings.selectString(selectedRunProperty(), "testMethodName");
	}

	public ObjectBinding<Date> selectedRun_timeStamp() {
		return Bindings.select(selectedRunProperty(), "timestamp");
	}

	public StringBinding selectedRunTest_formattedTimeStamp() {
		return Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				if (selectedRun_timeStamp().get() != null) {
					return DATE_FORMAT.format(selectedRun_timeStamp().get());
				} else {
					return null;
				}
			}
		}, selectedRunProperty());
	}

	public ObjectBinding<Throwable> selectedRun_exception() {
		return Bindings.select(selectedRunProperty(), "exception");
	}

	public ObjectBinding<StackTraceElement[]> selectedRun_trace() {
		return Bindings.select(selectedRunProperty(), "trace");
	}

	public StringBinding selectedRun_formattedTrace() {
		return Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				if (selectedRun_trace().get() != null) {
					return Joiner.on('\n').join(selectedRun_trace().get());
				} else {
					return null;
				}
			}
		}, selectedRunProperty());
	}

	@FXML
	public void initialize() {
		setupBatches();
		setupRuns();
		setupDetail();
	}

	protected void setupBatches() {
		// Bind to model
		batchesTable.itemsProperty().bindBidirectional(batches);
		// Bind selected test batch
		selectedBatchProperty().bind(batchesTable.getSelectionModel().selectedItemProperty());

		// Set up columns
		TableColumn<TestBatchModel, TestBatchState> stateColumn = new TableColumn<>();
		stateColumn.setCellValueFactory(new StateBatchCellValueFactory());
		stateColumn.setCellFactory(new StateBatchCellFactory(IMAGE_BATCH_RUNNING, IMAGE_BATCH_SUCCESS,
				IMAGE_BATCH_FAILURE));
		stateColumn.setPrefWidth(30);

		TableColumn<TestBatchModel, Date> startDateColumn = new TableColumn<>("Started");
		startDateColumn.setCellValueFactory(new PropertyValueFactory<TestBatchModel, Date>("startDate"));
		startDateColumn.setCellFactory(new FormattedDateCellFactory<TestBatchModel>(DATE_FORMAT));
		startDateColumn.setPrefWidth(120);

		TableColumn<TestBatchModel, Number> durationColumn = new TableColumn<>("Duration");
		durationColumn.setCellValueFactory(new DurationBatchCellValueFactory());
		durationColumn.setCellFactory(new FormattedDurationCellFactory<TestBatchModel>());
		durationColumn.setPrefWidth(80);
		durationColumn.setVisible(false);

		TableColumn<TestBatchModel, Number> runsCountColumn = new TableColumn<>("Runs");
		runsCountColumn.setCellValueFactory(new PropertyValueFactory<TestBatchModel, Number>("runsCount"));
		runsCountColumn.setPrefWidth(50);

		TableColumn<TestBatchModel, Number> failedRunsCountColumn = new TableColumn<>("Failed");
		failedRunsCountColumn.setCellValueFactory(new FailedRunsBatchCellValueFactory());
		failedRunsCountColumn.setPrefWidth(50);
		failedRunsCountColumn.setVisible(false);

		TableColumn<TestBatchModel, Number> failurePercentageColumn = new TableColumn<>("Fail %");
		failurePercentageColumn.setCellValueFactory(new FailurePercentageBatchCellValueFactory());
		failurePercentageColumn.setCellFactory(new FormattedNumberCellFactory<TestBatchModel>(NumberFormat
				.getPercentInstance()));
		failurePercentageColumn.setPrefWidth(50);

		batchesTable.getColumns().setAll(
				ImmutableList.of(stateColumn, startDateColumn, durationColumn, runsCountColumn, failedRunsCountColumn,
						failurePercentageColumn));
	}

	protected void setupRuns() {
		// Bind to model
		runsTable.itemsProperty().bind(runsProperty());
		// Bind selected test run
		selectedRunProperty().bind(runsTable.getSelectionModel().selectedItemProperty());

		// Set up columns
		TableColumn<TestRunModel, Boolean> successColumn = new TableColumn<>();
		successColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, Boolean>("successfulRun"));
		successColumn.setCellFactory(new BooleanImageCellFactory<TestRunModel>(IMAGE_TEST_SUCCESS, IMAGE_TEST_FAILURE));
		successColumn.setPrefWidth(30);

		TableColumn<TestRunModel, String> testClassNameColumn = new TableColumn<>("Test class");
		testClassNameColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, String>("testClassName"));
		testClassNameColumn.setPrefWidth(120);

		TableColumn<TestRunModel, String> testMethodNameColumn = new TableColumn<>("Test method");
		testMethodNameColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, String>("testMethodName"));
		testMethodNameColumn.setPrefWidth(120);

		TableColumn<TestRunModel, Date> timestampColumn = new TableColumn<>("Time");
		timestampColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, Date>("timestamp"));
		timestampColumn.setCellFactory(new FormattedDateCellFactory<TestRunModel>(DATE_FORMAT));
		timestampColumn.setPrefWidth(120);

		runsTable.getColumns().setAll(
				ImmutableList.of(successColumn, testClassNameColumn, testMethodNameColumn, timestampColumn));
	}

	protected void setupDetail() {
		// Bind to properties
		selectedRunTitle.textProperty().bind(
				selectedRun_testClassName().concat(".").concat(selectedRun_testMethodName()));
		selectedRunTitle.graphicProperty().bind(selectedRun_resultImageView());
		selectedRunTimestamp.textProperty().bind(selectedRunTest_formattedTimeStamp());
		selectedRunException.textProperty().bind(Bindings.convert(selectedRun_exception()));
		selectedRunTrace.textProperty().bind(Bindings.concat(selectedRun_formattedTrace()));

		// Show only when selected
		selectedRunDetail.visibleProperty().bind(selectedRunProperty().isNotNull());
		selectedRunDetail.managedProperty().bind(selectedRunProperty().isNotNull());

		// Show only exception details when failed
		selectedRunExceptionLabel.visibleProperty().bind(selectedRun_isFailed());
		selectedRunExceptionLabel.managedProperty().bind(selectedRun_isFailed());
		selectedRunException.visibleProperty().bind(selectedRun_isFailed());
		selectedRunException.managedProperty().bind(selectedRun_isFailed());
		selectedRunTraceLabel.visibleProperty().bind(selectedRun_isFailed());
		selectedRunTraceLabel.managedProperty().bind(selectedRun_isFailed());
		selectedRunTrace.visibleProperty().bind(selectedRun_isFailed());
		selectedRunTrace.managedProperty().bind(selectedRun_isFailed());
	}

	/**
	 * Cell factory for retrieving
	 * {@code TestBatchModel#failedRunsCountProperty()}.
	 */
	protected static class StateBatchCellFactory extends EnumImageCellFactory<TestBatchModel, TestBatchState> {

		private final Image runningImage;
		private final Image successImage;
		private final Image failureImage;

		public StateBatchCellFactory(Image runningImage, Image successImage, Image failureImage) {
			this.runningImage = runningImage;
			this.successImage = successImage;
			this.failureImage = failureImage;
		}

		@Override
		protected Image getImage(TestBatchState value) {
			switch (value) {
			case RUNNING:
				return runningImage;
			case SUCCESS:
				return successImage;
			case FAILURE:
				return failureImage;
			default:
				return null;
			}
		}

	}

	/**
	 * Cell value factory for retrieving {@code TestBatchModel#stateProperty()}.
	 */
	protected static class StateBatchCellValueFactory implements
			Callback<TableColumn.CellDataFeatures<TestBatchModel, TestBatchState>, ObservableValue<TestBatchState>> {
		@Override
		public ObservableValue<TestBatchState> call(CellDataFeatures<TestBatchModel, TestBatchState> features) {
			return features.getValue().stateProperty();
		}
	}

	/**
	 * Cell value factory for retrieving
	 * {@code TestBatchModel#durationProperty()}.
	 */
	protected static class DurationBatchCellValueFactory implements
			Callback<TableColumn.CellDataFeatures<TestBatchModel, Number>, ObservableValue<Number>> {
		@Override
		public ObservableValue<Number> call(CellDataFeatures<TestBatchModel, Number> features) {
			return features.getValue().durationProperty();
		}
	}

	/**
	 * Cell value factory for retrieving
	 * {@code TestBatchModel#failedRunsCountProperty()}.
	 */
	protected static class FailedRunsBatchCellValueFactory implements
			Callback<TableColumn.CellDataFeatures<TestBatchModel, Number>, ObservableValue<Number>> {
		@Override
		public ObservableValue<Number> call(CellDataFeatures<TestBatchModel, Number> features) {
			return features.getValue().failedRunsCountProperty();
		}
	}

	/**
	 * Cell value factory for retrieving
	 * {@code TestBatchModel#failureFractionProperty()}.
	 */
	protected static class FailurePercentageBatchCellValueFactory implements
			Callback<TableColumn.CellDataFeatures<TestBatchModel, Number>, ObservableValue<Number>> {
		@Override
		public ObservableValue<Number> call(CellDataFeatures<TestBatchModel, Number> features) {
			return features.getValue().failureFractionProperty();
		}
	}

}
