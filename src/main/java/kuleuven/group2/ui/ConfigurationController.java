package kuleuven.group2.ui;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import kuleuven.group2.ui.model.PoliciesModel;
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
	private final PoliciesModel policies = new PoliciesModel(PoliciesModel.DEFAULTS);

	private BooleanBinding configured;
	private BooleanProperty canConfigure = new SimpleBooleanProperty();

	@FXML
	public void initialize() {
		// Configured when all fields set
		configured = classSourceField.directoryProperty().isNotNull()
				.and(testSourceField.directoryProperty().isNotNull()).and(binaryField.directoryProperty().isNotNull())
				.and(policyField.valueProperty().isNotNull());

		// Disable reconfigurations when not allowed
		classSourceField.disableProperty().bind(canConfigure().not());
		testSourceField.disableProperty().bind(canConfigure().not());
		binaryField.disableProperty().bind(canConfigure().not());

		// Bind policy field to policies list
		policyField.itemsProperty().bind(policies);
		policyField.valueProperty().bindBidirectional(selectedPolicy);

		// Set up policy display
		PolicyListCellFactory cellFactory = new PolicyListCellFactory();
		policyField.setButtonCell(cellFactory.call(null));
		policyField.setCellFactory(cellFactory);

		// Default policy
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

	public boolean isConfigured() {
		return configured().get();
	}

	public BooleanBinding configured() {
		return configured;
	}

	public BooleanProperty canConfigure() {
		return canConfigure;
	}

	protected static class PolicyListCellFactory implements Callback<ListView<PolicyModel>, ListCell<PolicyModel>> {
		@Override
		public ListCell<PolicyModel> call(ListView<PolicyModel> view) {
			return new PolicyListCell();
		}
	}

	protected static class PolicyListCell extends ListCell<PolicyModel> {
		@Override
		protected void updateItem(PolicyModel model, boolean empty) {
			super.updateItem(model, empty);
			if (!empty) {
				setText(model.getName());
			} else {
				setText(null);
			}
		}
	}

}
