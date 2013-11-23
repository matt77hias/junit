package kuleuven.group2.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kuleuven.group2.policy.ChangedCodeFirst;
import kuleuven.group2.policy.DistinctFailureFirst;
import kuleuven.group2.policy.FrequentFailureFirst;
import kuleuven.group2.policy.LastFailureFirst;
import kuleuven.group2.policy.Policy;

import com.google.common.collect.ImmutableList;

public class PolicyModel {

	public static final ImmutableList<PolicyModel> ALL = ImmutableList.<PolicyModel> copyOf(new PolicyModel[] {
			new PolicyModel("Changed code first", new ChangedCodeFirst()),
			new PolicyModel("Distinct failure first", new DistinctFailureFirst()),
			new PolicyModel("Frequent failure first", new FrequentFailureFirst()),
			new PolicyModel("Last failure first", new LastFailureFirst())
	});

	private final StringProperty name = new SimpleStringProperty();
	private final Policy policy;

	public PolicyModel(String name, Policy policy) {
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

	public Policy getPolicy() {
		return policy;
	}

}
