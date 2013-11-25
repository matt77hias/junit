package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import kuleuven.group2.ui.model.PolicyModel;

public class Configuration extends BorderPane {

	private ConfigurationController controller;

	public Configuration() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Configuration.fxml"));
		Node view = (Node) loader.load();
		controller = (ConfigurationController) loader.getController();
		setCenter(view);
	}

	public String getClassSourceDir() {
		return classSourceDirProperty().get();
	}

	public void setClassSourceDir(String classSourceDir) {
		classSourceDirProperty().set(classSourceDir);
	}

	public StringProperty classSourceDirProperty() {
		return controller.classSourceDirProperty();
	}

	public String getTestSourceDir() {
		return testSourceDirProperty().get();
	}

	public void setTestSourceDir(String testSourceDir) {
		testSourceDirProperty().set(testSourceDir);
	}

	public StringProperty testSourceDirProperty() {
		return controller.testSourceDirProperty();
	}

	public String getBinaryDir() {
		return binaryDirProperty().get();
	}

	public void setBinaryDir(String binaryDir) {
		binaryDirProperty().set(binaryDir);
	}

	public StringProperty binaryDirProperty() {
		return controller.binaryDirProperty();
	}

	public PolicyModel getPolicy() {
		return selectedPolicyProperty().get();
	}

	public void setPolicy(PolicyModel policy) {
		selectedPolicyProperty().set(policy);
	}

	public ObjectProperty<PolicyModel> selectedPolicyProperty() {
		return controller.selectedPolicyProperty();
	}

	public boolean isConfigured() {
		return configured().get();
	}

	public BooleanBinding configured() {
		return controller.configured();
	}

	public BooleanProperty canConfigure() {
		return controller.canConfigure();
	}

}