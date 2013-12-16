package kuleuven.group2.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kuleuven.group2.policy.NonWeightedTestSortingPolicy;

public class PolicyModel {

	private final StringProperty name = new SimpleStringProperty();
	private final NonWeightedTestSortingPolicy policy;

	public PolicyModel(String name, NonWeightedTestSortingPolicy policy) {
		this.name.set(name);
		this.policy = policy;
	}

	public String getName() {
		return nameProperty().get();
	}

	public void setName(String name) {
		nameProperty().set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public NonWeightedTestSortingPolicy getPolicy() {
		return policy;
	}

}
