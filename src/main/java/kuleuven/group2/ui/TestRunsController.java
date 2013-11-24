package kuleuven.group2.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import kuleuven.group2.ui.model.TestRunModel;

import com.google.common.collect.ImmutableList;

public class TestRunsController {

	@FXML
	TableView<TestRunModel> runsTable;

	private final ListProperty<TestRunModel> runs = new SimpleListProperty<>(
			FXCollections.<TestRunModel> observableArrayList());

	@FXML
	public void initialize() {
		runsTable.itemsProperty().bindBidirectional(runs);

		TableColumn<TestRunModel, Boolean> resultColumn = new TableColumn<>();
		resultColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, Boolean>("successfulRun"));
		resultColumn.setCellFactory(new ResultCellFactory());

		TableColumn<TestRunModel, String> testClassNameColumn = new TableColumn<>("Test class");
		testClassNameColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, String>("testClassName"));
		testClassNameColumn.setPrefWidth(100);

		TableColumn<TestRunModel, String> testMethodNameColumn = new TableColumn<>("Test method");
		testMethodNameColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, String>("testMethodName"));
		testMethodNameColumn.setPrefWidth(100);

		TableColumn<TestRunModel, Date> timestampColumn = new TableColumn<>("Time");
		timestampColumn.setCellValueFactory(new PropertyValueFactory<TestRunModel, Date>("timeStamp"));
		timestampColumn.setCellFactory(new TimestampCellFactory());
		timestampColumn.setPrefWidth(120);

		runsTable.getColumns().setAll(
				ImmutableList.of(resultColumn, testClassNameColumn, testMethodNameColumn, timestampColumn));
	}

	public ListProperty<TestRunModel> runsProperty() {
		return runs;
	}

	protected static class TimestampCellFactory implements
			Callback<TableColumn<TestRunModel, Date>, TableCell<TestRunModel, Date>> {

		protected static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		@Override
		public TableCell<TestRunModel, Date> call(TableColumn<TestRunModel, Date> column) {
			return new TableCell<TestRunModel, Date>() {
				@Override
				protected void updateItem(Date date, boolean empty) {
					super.updateItem(date, empty);
					if (!empty) {
						setText(dateFormat.format(date));
					} else {
						setText(null);
					}
				}
			};
		}

	}

	protected static class ResultCellFactory implements
			Callback<TableColumn<TestRunModel, Boolean>, TableCell<TestRunModel, Boolean>> {

		private final Image successImage = new Image(getClass().getResource("icons/test-ok.png").toExternalForm());
		private final Image failureImage = new Image(getClass().getResource("icons/test-err.png").toExternalForm());

		@Override
		public TableCell<TestRunModel, Boolean> call(TableColumn<TestRunModel, Boolean> column) {
			return new TableCell<TestRunModel, Boolean>() {
				@Override
				protected void updateItem(Boolean result, boolean empty) {
					super.updateItem(result, empty);
					if (result != null) {
						setGraphic(new ImageView(result ? successImage : failureImage));
					} else {
						setGraphic(null);
					}
				}
			};
		}

	}

}
