package kuleuven.group2.ui;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import kuleuven.group2.policy.RoundRobinPolicy;
import kuleuven.group2.ui.model.CompositePolicyModel;
import kuleuven.group2.ui.model.PolicyModel;
import kuleuven.group2.ui.model.WeightedPolicyModel;
import kuleuven.group2.ui.util.NumberFieldCellFactory;
import kuleuven.group2.ui.util.PolicyListCellFactory;

import com.google.common.collect.ImmutableList;

public class PolicyComposerController {

	/*
	 * Components
	 */

	@FXML
	private TableView<CompositePolicyModel> policiesTable;

	@FXML
	private TableView<WeightedPolicyModel> policyRecordsTable;

	@FXML
	private TextField newComposedName;

	@FXML
	private Button addComposed;

	@FXML
	private Button removeComposed;

	@FXML
	private ComboBox<PolicyModel> newRecordPolicy;

	@FXML
	private Button addRecord;

	@FXML
	private Button removeRecord;

	/*
	 * Properties
	 */

	private final ListProperty<PolicyModel> allPolicies = new SimpleListProperty<>();
	private final ListProperty<CompositePolicyModel> compositePolicies = new SimpleListProperty<>();

	private final ObjectProperty<CompositePolicyModel> selectedPolicy = new SimpleObjectProperty<>();
	private final StringProperty newPolicyName = new SimpleStringProperty();
	private final ObjectProperty<WeightedPolicyModel> selectedRecord = new SimpleObjectProperty<>();
	private final ObjectProperty<PolicyModel> selectedNewRecordPolicy = new SimpleObjectProperty<>();

	public ListProperty<PolicyModel> allPoliciesProperty() {
		return allPolicies;
	}

	public ListProperty<CompositePolicyModel> compositePoliciesProperty() {
		return compositePolicies;
	}

	public ObjectProperty<CompositePolicyModel> selectedPolicyProperty() {
		return selectedPolicy;
	}

	public ListBinding<WeightedPolicyModel> recordsProperty() {
		return new ListBinding<WeightedPolicyModel>() {
			{
				super.bind(selectedPolicyProperty());
			}

			@Override
			protected ObservableList<WeightedPolicyModel> computeValue() {
				CompositePolicyModel selectedPolicy = selectedPolicyProperty().get();
				if (selectedPolicy == null) {
					return FXCollections.emptyObservableList();
				}
				return selectedPolicy.recordsProperty();
			}
		};
	}

	public ObjectProperty<WeightedPolicyModel> selectedRecordProperty() {
		return selectedRecord;
	}

	public StringProperty newPolicy_nameProperty() {
		return newPolicyName;
	}

	public ListBinding<PolicyModel> newRecordPolicy_policiesProperty() {
		return new ListBinding<PolicyModel>() {
			{
				super.bind(allPoliciesProperty());
				super.bind(selectedPolicyProperty());
			}

			@Override
			protected ObservableList<PolicyModel> computeValue() {
				ObservableList<PolicyModel> policies = allPoliciesProperty().get();
				if (policies == null) return null;
				policies = FXCollections.observableArrayList(policies);
				// TODO Also remove recursively dependent policies?
				policies.remove(selectedPolicyProperty().get());
				return policies;
			}
		};
	}

	public ObjectProperty<PolicyModel> newRecordPolicy_policyProperty() {
		return selectedNewRecordPolicy;
	}

	@FXML
	public void initialize() {
		setupPolicies();
		setupPolicyButtons();
		setupRecords();
		setupRecordButtons();
	}

	protected void setupPolicies() {
		// Bind to model
		policiesTable.itemsProperty().bindBidirectional(compositePoliciesProperty());
		// Bind selected policy
		selectedPolicyProperty().bind(policiesTable.getSelectionModel().selectedItemProperty());

		// Disable when no policy selected
		removeComposed.disableProperty().bind(selectedPolicyProperty().isNull());

		// Set up columns
		TableColumn<CompositePolicyModel, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<CompositePolicyModel, String>("name"));
		nameColumn.setCellFactory(TextFieldTableCell.<CompositePolicyModel> forTableColumn());
		nameColumn.setEditable(true);
		nameColumn.setPrefWidth(120);

		policiesTable.getColumns().setAll(ImmutableList.of(nameColumn));
	}

	protected void setupPolicyButtons() {
		// Bind to model
		newPolicy_nameProperty().bind(newComposedName.textProperty());

		// Disable when name is empty
		addComposed.disableProperty().bind(newPolicy_nameProperty().isEqualTo(""));
		// Disable when no policy selected
		removeComposed.disableProperty().bind(selectedPolicyProperty().isNull());
	}

	protected void setupRecords() {
		// Bind to model
		policyRecordsTable.itemsProperty().bind(recordsProperty());
		// Bind selected record
		selectedRecordProperty().bind(policyRecordsTable.getSelectionModel().selectedItemProperty());
		// Disable when no composed policy selected
		policyRecordsTable.disableProperty().bind(selectedPolicyProperty().isNull());

		// Set up columns
		TableColumn<WeightedPolicyModel, String> policyNameColumn = new TableColumn<>("Policy name");
		policyNameColumn.setCellValueFactory(new PropertyValueFactory<WeightedPolicyModel, String>("policyName"));
		policyNameColumn.setPrefWidth(120);

		TableColumn<WeightedPolicyModel, Number> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<WeightedPolicyModel, Number>("weight"));
		weightColumn.setCellFactory(new NumberFieldCellFactory<WeightedPolicyModel>());
		weightColumn.setEditable(true);
		weightColumn.setPrefWidth(60);

		policyRecordsTable.getColumns().setAll(ImmutableList.of(policyNameColumn, weightColumn));
	}

	protected void setupRecordButtons() {
		// Bind to model
		newRecordPolicy.itemsProperty().bind(newRecordPolicy_policiesProperty());
		newRecordPolicy_policyProperty().bind(newRecordPolicy.valueProperty());

		// Set up display
		PolicyListCellFactory cellFactory = new PolicyListCellFactory();
		newRecordPolicy.setButtonCell(cellFactory.call(null));
		newRecordPolicy.setCellFactory(cellFactory);

		// Disable when no composed policy selected
		newRecordPolicy.disableProperty().bind(selectedPolicyProperty().isNull());
		// Disable when no new policy selected
		addRecord.disableProperty().bind(
				selectedPolicyProperty().isNull().or(newRecordPolicy_policyProperty().isNull()));
		// Disable when no record selected
		removeRecord.disableProperty().bind(selectedPolicyProperty().isNull().or(selectedRecordProperty().isNull()));
	}

	@FXML
	public void addComposedPolicy() {
		String policyName = newPolicy_nameProperty().get();
		compositePoliciesProperty().add(new CompositePolicyModel(policyName, new RoundRobinPolicy()));
	}

	@FXML
	public void removeComposedPolicies() {
		compositePoliciesProperty().remove(policiesTable.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void addPolicyRecord() {
		PolicyModel policy = newRecordPolicy_policyProperty().get();
		recordsProperty().add(new WeightedPolicyModel(policy, 5));
	}

	@FXML
	public void removePolicyRecords() {
		recordsProperty().remove(policyRecordsTable.getSelectionModel().getSelectedItem());
	}

}
