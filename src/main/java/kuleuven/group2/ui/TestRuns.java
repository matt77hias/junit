package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import kuleuven.group2.ui.model.TestRunModel;

public class TestRuns extends BorderPane {

	private TestRunsController controller;

	public TestRuns() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TestRuns.fxml"));
		Node view = (Node) loader.load();
		controller = (TestRunsController) loader.getController();
		setCenter(view);
	}

	public ObservableList<TestRunModel> getRuns() {
		return runsProperty().get();
	}

	public void setRuns(ObservableList<TestRunModel> runs) {
		runsProperty().set(runs);
	}

	public ListProperty<TestRunModel> runsProperty() {
		return controller.runsProperty();
	}

}