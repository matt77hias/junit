package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import kuleuven.group2.Pipeline;
import kuleuven.group2.policy.Policy;
import kuleuven.group2.store.DirectoryStore;
import kuleuven.group2.store.Store;
import kuleuven.group2.ui.model.PolicyModel;

public class MainController {

	/*
	 * Models
	 */

	private Pipeline pipeline;

	/*
	 * Components
	 */

	@FXML
	private Configuration configuration;

	/*
	 * Properties
	 */

	public StringProperty classSourcesDirProperty() {
		return configuration.classSourceDirProperty();
	}

	public StringProperty testSourcesDirProperty() {
		return configuration.testSourceDirProperty();
	}

	public StringProperty binariesDirProperty() {
		return configuration.binaryDirProperty();
	}

	public ObjectProperty<PolicyModel> policyProperty() {
		return configuration.selectedPolicyProperty();
	}

	@FXML
	public void initialize() {
		policyProperty().addListener(new ChangeListener<PolicyModel>() {
			@Override
			public void changed(ObservableValue<? extends PolicyModel> observable, PolicyModel oldValue,
					PolicyModel newValue) {
				setPolicy(newValue);
			}
		});
	}

	public void setup() throws IllegalArgumentException, IOException {
		Store classSourceStore = new DirectoryStore(classSourcesDirProperty().get());
		Store testSourceStore = new DirectoryStore(testSourcesDirProperty().get());
		Store binaryStore = new DirectoryStore(binariesDirProperty().get());
		Policy sortPolicy = policyProperty().get().getPolicy();
		pipeline = new Pipeline(classSourceStore, testSourceStore, binaryStore, sortPolicy);
	}

	public void start() {
		if (pipeline != null) {
			pipeline.start();
		}
	}

	public void setPolicy(PolicyModel policyModel) {
		policyProperty().set(policyModel);
		if (pipeline != null) {
			pipeline.setSortPolicy(policyModel.getPolicy());
		}
	}

}
