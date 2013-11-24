package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import kuleuven.group2.ui.model.TestRunModel;

public class TestRuns extends BorderPane {

	private TestRunsController controller;

	public TestRuns() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TestRuns.fxml"));
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> param) {
				return controller = new TestRunsController();
			}
		});
		Node view = (Node) fxmlLoader.load();
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