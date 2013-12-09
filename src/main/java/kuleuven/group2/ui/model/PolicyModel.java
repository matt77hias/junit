package kuleuven.group2.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kuleuven.group2.policy.TestSortingPolicy;

public class PolicyModel {

	private final StringProperty name = new SimpleStringProperty();
	private final TestSortingPolicy policy;

	public PolicyModel(String name, TestSortingPolicy policy) {
		this.name.set(name);
		this.policy = policy;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getName() {
		return nameProperty().get();
	}

	public void setName(String name) {
		nameProperty().set(name);
	}

	public TestSortingPolicy getPolicy() {
		return policy;
	}

}
