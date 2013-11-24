package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class DirectoryField extends BorderPane {

	private DirectoryFieldController controller;

	public DirectoryField() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DirectoryField.fxml"));
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> param) {
				return controller = new DirectoryFieldController();
			}
		});
		Node view = (Node) fxmlLoader.load();
		setCenter(view);
	}

	public String getDialogTitle() {
		return dialogTitleProperty().get();
	}

	public void setDialogTitle(String dialogTitle) {
		dialogTitleProperty().set(dialogTitle);
	}

	public StringProperty dialogTitleProperty() {
		return controller.dialogTitleProperty();
	}

	public String getDirectory() {
		return directoryProperty().get();
	}

	public void setDirectory(String directory) {
		directoryProperty().set(directory);
	}

	public StringProperty directoryProperty() {
		return controller.directoryProperty();
	}

}