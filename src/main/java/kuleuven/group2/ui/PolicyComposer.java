package kuleuven.group2.ui;

import java.io.IOException;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import kuleuven.group2.ui.model.CompositePolicyModel;
import kuleuven.group2.ui.model.PolicyModel;

public class PolicyComposer extends BorderPane {

	private PolicyComposerController controller;

	public PolicyComposer() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PolicyComposer.fxml"));
		Node view = (Node) loader.load();
		controller = (PolicyComposerController) loader.getController();
		setCenter(view);
	}

	public ListProperty<PolicyModel> allPoliciesProperty() {
		return controller.allPoliciesProperty();
	}

	public ListProperty<CompositePolicyModel> compositePoliciesProperty() {
		return controller.compositePoliciesProperty();
	}

	public ObjectProperty<CompositePolicyModel> selectedPolicyProperty() {
		return controller.selectedPolicyProperty();
	}

}