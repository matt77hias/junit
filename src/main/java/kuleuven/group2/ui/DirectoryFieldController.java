package kuleuven.group2.ui;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class DirectoryFieldController {

	private final StringProperty title = new SimpleStringProperty("");
	private final StringProperty directory = new SimpleStringProperty();

	@FXML
	private TextField directoryText;

	@FXML
	public void initialize() {
		directoryText.textProperty().bind(directoryProperty());
	}

	public StringProperty titleProperty() {
		return title;
	}

	public StringProperty directoryProperty() {
		return directory;
	}

	@FXML
	public void onBrowse(ActionEvent e) {
		browseForDirectory();
	}

	public void browseForDirectory() {
		DirectoryChooser chooser = new DirectoryChooser();
		String directory = directoryProperty().get();
		if (directory != null) {
			chooser.setInitialDirectory(new File(directory));
		}
		chooser.titleProperty().bind(titleProperty());
		File chosenDirectory = chooser.showDialog(directoryText.getScene().getWindow());
		if (chosenDirectory != null) {
			directoryProperty().set(chosenDirectory.getAbsolutePath());
		}
	}
}
