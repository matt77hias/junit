package kuleuven.group2.ui;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import kuleuven.group2.ui.model.PolicyModel;

public class ConfigurationController {

	@FXML
	private DirectoryField classSourceField;

	@FXML
	private DirectoryField testSourceField;

	@FXML
	private DirectoryField binaryField;

	@FXML
	private ComboBox<PolicyModel> policyField;

	private final ObjectProperty<PolicyModel> selectedPolicy = new SimpleObjectProperty<PolicyModel>();
	private final ListProperty<PolicyModel> policies = new SimpleListProperty<>(
			FXCollections.observableArrayList(PolicyModel.ALL));

	@FXML
	public void initialize() {
		policyField.itemsProperty().bind(policies);
		policyField.valueProperty().bindBidirectional(selectedPolicy);
		policyField.setButtonCell(new PolicyListCell());
		policyField.setCellFactory(new Callback<ListView<PolicyModel>, ListCell<PolicyModel>>() {
			@Override
			public ListCell<PolicyModel> call(ListView<PolicyModel> arg0) {
				return new PolicyListCell();
			}
		});

		policyField.getSelectionModel().selectFirst();
	}

	public StringProperty classSourceDirProperty() {
		return classSourceField.directoryProperty();
	}

	public StringProperty testSourceDirProperty() {
		return testSourceField.directoryProperty();
	}

	public StringProperty binaryDirProperty() {
		return binaryField.directoryProperty();
	}

	public ObjectProperty<PolicyModel> selectedPolicyProperty() {
		return selectedPolicy;
	}

	protected static class PolicyListCell extends ListCell<PolicyModel> {
		@Override
		protected void updateItem(PolicyModel model, boolean empty) {
			super.updateItem(model, empty);
			if (model != null) {
				setText(model.getName());
			} else {
				setText(null);
			}
		}
	}

}
