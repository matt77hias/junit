package kuleuven.group2.ui;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import kuleuven.group2.ui.model.PolicyModel;
import kuleuven.group2.ui.util.PolicyListCellFactory;

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
	private final ListProperty<PolicyModel> policies = new SimpleListProperty<>();

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

	public ListProperty<PolicyModel> policiesProperty() {
		return policies;
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

}
