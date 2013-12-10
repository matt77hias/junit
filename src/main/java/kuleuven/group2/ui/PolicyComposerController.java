package kuleuven.group2.ui;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import kuleuven.group2.policy.CompositePolicy;
import kuleuven.group2.ui.model.CompositePolicyModel;
import kuleuven.group2.ui.model.PolicyModel;
import kuleuven.group2.ui.model.PolicyRecordModel;

import com.google.common.collect.ImmutableList;

public class PolicyComposerController {

	/*
	 * Components
	 */

	@FXML
	private TableView<CompositePolicyModel> policiesTable;

	@FXML
	private TableView<PolicyRecordModel> policyRecordsTable;

	/*
	 * Properties
	 */

	private final ListProperty<PolicyModel> allPolicies = new SimpleListProperty<>();
	private final ListProperty<CompositePolicyModel> compositePolicies = new SimpleListProperty<>();
	private final ObjectProperty<CompositePolicyModel> selectedPolicy = new SimpleObjectProperty<>();

	public ListProperty<PolicyModel> allPoliciesProperty() {
		return allPolicies;
	}

	public ListProperty<CompositePolicyModel> compositePoliciesProperty() {
		return compositePolicies;
	}

	public ObjectProperty<CompositePolicyModel> selectedPolicyProperty() {
		return selectedPolicy;
	}

	public ListBinding<PolicyRecordModel> selectedPolicy_recordsProperty() {
		return new ListBinding<PolicyRecordModel>() {
			{
				super.bind(selectedPolicyProperty());
			}

			@Override
			protected ObservableList<PolicyRecordModel> computeValue() {
				CompositePolicyModel selectedPolicy = selectedPolicyProperty().get();
				if (selectedPolicy == null) {
					return FXCollections.emptyObservableList();
				}
				return selectedPolicy.recordsProperty();
			}
		};
	}

	@FXML
	public void initialize() {
		setupPolicies();
		setupRecords();
	}

	protected void setupPolicies() {
		// Bind to model
		policiesTable.itemsProperty().bindBidirectional(compositePoliciesProperty());
		// Bind selected policy
		selectedPolicyProperty().bind(policiesTable.getSelectionModel().selectedItemProperty());

		// Set up columns
		TableColumn<CompositePolicyModel, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<CompositePolicyModel, String>("name"));
		nameColumn.setCellFactory(TextFieldTableCell.<CompositePolicyModel> forTableColumn());
		nameColumn.setEditable(true);
		nameColumn.setPrefWidth(120);

		policiesTable.getColumns().setAll(ImmutableList.of(nameColumn));
	}

	protected void setupRecords() {
		// Bind to model
		policyRecordsTable.itemsProperty().bind(selectedPolicy_recordsProperty());

		// Set up columns
		TableColumn<PolicyRecordModel, String> policyNameColumn = new TableColumn<>("Policy name");
		policyNameColumn.setCellValueFactory(new PropertyValueFactory<PolicyRecordModel, String>("policyName"));
		policyNameColumn.setPrefWidth(120);

		TableColumn<PolicyRecordModel, Number> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<PolicyRecordModel, Number>("weight"));
		weightColumn.setPrefWidth(60);

		policyRecordsTable.getColumns().setAll(ImmutableList.of(policyNameColumn, weightColumn));
	}

	@FXML
	public void addComposedPolicy() {
		compositePoliciesProperty().add(new CompositePolicyModel("New composed policy", new CompositePolicy()));
	}

	@FXML
	public void removeComposedPolicies() {
		compositePoliciesProperty().remove(policiesTable.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void addPolicyRecord() {
		selectedPolicy_recordsProperty().add(new PolicyRecordModel(allPoliciesProperty().get(0), 5));
	}

	@FXML
	public void removePolicyRecords() {
		selectedPolicy_recordsProperty().remove(policyRecordsTable.getSelectionModel().getSelectedItem());
	}

}
