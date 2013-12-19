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
import kuleuven.group2.policy.RoundRobinTestSortingPolicy;
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
	private TableView<WeightedPolicyModel> weightedPoliciesTable;

	@FXML
	private TextField newComposedName;

	@FXML
	private Button addComposed;

	@FXML
	private Button removeComposed;

	@FXML
	private ComboBox<PolicyModel> newWeightedPolicy;

	@FXML
	private Button addWeightedPolicy;

	@FXML
	private Button removeWeightedPolicy;

	/*
	 * Properties
	 */

	private final ListProperty<PolicyModel> allPolicies = new SimpleListProperty<>();
	private final ListProperty<CompositePolicyModel> compositePolicies = new SimpleListProperty<>();

	private final ObjectProperty<CompositePolicyModel> selectedPolicy = new SimpleObjectProperty<>();
	private final StringProperty newPolicyName = new SimpleStringProperty();
	private final ObjectProperty<WeightedPolicyModel> selectedWeightedPolicy = new SimpleObjectProperty<>();
	private final ObjectProperty<PolicyModel> selectedNewWeightedPolicy = new SimpleObjectProperty<>();

	public ListProperty<PolicyModel> allPoliciesProperty() {
		return allPolicies;
	}

	public ListProperty<CompositePolicyModel> compositePoliciesProperty() {
		return compositePolicies;
	}

	public ObjectProperty<CompositePolicyModel> selectedPolicyProperty() {
		return selectedPolicy;
	}

	public ListBinding<WeightedPolicyModel> weightedPoliciesProperty() {
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
				return selectedPolicy.weightedPoliciesProperty();
			}
		};
	}

	public ObjectProperty<WeightedPolicyModel> selectedWeightedPolicyProperty() {
		return selectedWeightedPolicy;
	}

	public StringProperty newPolicy_nameProperty() {
		return newPolicyName;
	}

	public ListBinding<PolicyModel> newWeightedPolicy_policiesProperty() {
		return new ListBinding<PolicyModel>() {
			{
				super.bind(allPoliciesProperty());
				super.bind(selectedPolicyProperty());
			}

			@Override
			protected ObservableList<PolicyModel> computeValue() {
				ObservableList<PolicyModel> policies = allPoliciesProperty().get();
				if (policies == null) return null;
				CompositePolicyModel selected = selectedPolicyProperty().get();
				if (selected == null) return policies;
				// Add allowed policies
				ObservableList<PolicyModel> filtered = FXCollections.observableArrayList();
				for (PolicyModel policyModel : policies) {
					if (selected.getPolicy().canHaveAsTestSortingPolicy(policyModel.getPolicy())) {
						filtered.add(policyModel);
					}
				}
				return filtered;
			}
		};
	}

	public ObjectProperty<PolicyModel> newWeightedPolicy_policyProperty() {
		return selectedNewWeightedPolicy;
	}

	@FXML
	public void initialize() {
		setupPolicies();
		setupPolicyButtons();
		setupWeightedPolicies();
		setupWeightedPolicyButtons();
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

	protected void setupWeightedPolicies() {
		// Bind to model
		weightedPoliciesTable.itemsProperty().bind(weightedPoliciesProperty());
		// Bind selected weighted policy
		selectedWeightedPolicyProperty().bind(weightedPoliciesTable.getSelectionModel().selectedItemProperty());
		// Disable when no composed policy selected
		weightedPoliciesTable.disableProperty().bind(selectedPolicyProperty().isNull());

		// Set up columns
		TableColumn<WeightedPolicyModel, String> policyNameColumn = new TableColumn<>("Policy name");
		policyNameColumn.setCellValueFactory(new PropertyValueFactory<WeightedPolicyModel, String>("policyName"));
		policyNameColumn.setPrefWidth(120);

		TableColumn<WeightedPolicyModel, Number> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<WeightedPolicyModel, Number>("weight"));
		weightColumn.setCellFactory(new NumberFieldCellFactory<WeightedPolicyModel>());
		weightColumn.setEditable(true);
		weightColumn.setPrefWidth(60);

		weightedPoliciesTable.getColumns().setAll(ImmutableList.of(policyNameColumn, weightColumn));
	}

	protected void setupWeightedPolicyButtons() {
		// Bind to model
		newWeightedPolicy.itemsProperty().bind(newWeightedPolicy_policiesProperty());
		newWeightedPolicy_policyProperty().bind(newWeightedPolicy.valueProperty());

		// Set up display
		PolicyListCellFactory cellFactory = new PolicyListCellFactory();
		newWeightedPolicy.setButtonCell(cellFactory.call(null));
		newWeightedPolicy.setCellFactory(cellFactory);

		// Disable when no composed policy selected
		newWeightedPolicy.disableProperty().bind(selectedPolicyProperty().isNull());
		// Disable when no new policy selected
		addWeightedPolicy.disableProperty().bind(
				selectedPolicyProperty().isNull().or(newWeightedPolicy_policyProperty().isNull()));
		// Disable when no weighted policy selected
		removeWeightedPolicy.disableProperty().bind(
				selectedPolicyProperty().isNull().or(selectedWeightedPolicyProperty().isNull()));
	}

	@FXML
	public void addComposedPolicy() {
		String policyName = newPolicy_nameProperty().get();
		compositePoliciesProperty().add(new CompositePolicyModel(policyName, new RoundRobinTestSortingPolicy()));
	}

	@FXML
	public void removeComposedPolicies() {
		compositePoliciesProperty().remove(policiesTable.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void addWeightedPolicy() {
		PolicyModel policy = newWeightedPolicy_policyProperty().get();
		weightedPoliciesProperty().add(new WeightedPolicyModel(policy, 5));
	}

	@FXML
	public void removeWeightedPolicies() {
		weightedPoliciesProperty().remove(weightedPoliciesTable.getSelectionModel().getSelectedItem());
	}

}
