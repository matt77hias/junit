package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import kuleuven.group2.ui.model.TestBatchModel;

public class TestResults extends BorderPane {

	private TestResultsController controller;

	public TestResults() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TestResults.fxml"));
		Node view = (Node) loader.load();
		controller = (TestResultsController) loader.getController();
		setCenter(view);
	}

	public ObservableList<TestBatchModel> getBatches() {
		return batchesProperty().get();
	}

	public void setBatches(ObservableList<TestBatchModel> batches) {
		batchesProperty().set(batches);
	}

	public ListProperty<TestBatchModel> batchesProperty() {
		return controller.batchesProperty();
	}

}