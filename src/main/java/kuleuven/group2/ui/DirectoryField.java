package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class DirectoryField extends BorderPane {

	private DirectoryFieldController controller;

	public DirectoryField() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DirectoryField.fxml"));
		Node view = (Node) loader.load();
		controller = (DirectoryFieldController) loader.getController();
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