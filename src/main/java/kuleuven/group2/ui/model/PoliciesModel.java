package kuleuven.group2.ui.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kuleuven.group2.policy.ChangedCodeFirst;
import kuleuven.group2.policy.DistinctFailureFirst;
import kuleuven.group2.policy.FrequentFailureFirst;
import kuleuven.group2.policy.LastFailureFirst;

import com.google.common.collect.ImmutableList;

public class PoliciesModel {

	public static final ImmutableList<PolicyModel> SIMPLE_POLICIES = ImmutableList
			.<PolicyModel> copyOf(new PolicyModel[] {
					new PolicyModel("Changed code first", new ChangedCodeFirst()),
					new PolicyModel("Distinct failure first", new DistinctFailureFirst()),
					new PolicyModel("Frequent failure first", new FrequentFailureFirst()),
					new PolicyModel("Last failure first", new LastFailureFirst())
			});

	private final ListProperty<PolicyModel> allPolicies = new SimpleListProperty<>();
	private final ListProperty<PolicyModel> simplePolicies = new SimpleListProperty<>();
	private final ListProperty<PolicyModel> compositePolicies = new SimpleListProperty<>();

	public PoliciesModel() {
		// Create unmodifiable list of all policies
		ObservableList<PolicyModel> list = FXCollections.observableArrayList();
		allPolicies.set(FXCollections.unmodifiableObservableList(list));
		// Create unmodifiable list simple policies
		ObservableList<PolicyModel> simples = FXCollections.observableArrayList(SIMPLE_POLICIES);
		simplePolicies.set(FXCollections.unmodifiableObservableList(simples));
		// Add simple policies to all policies
		list.addAll(simplePolicies);
		// Put composite policies at end of list
		compositePolicies.set(FXCollections.observableList(list.subList(list.size(), list.size())));
	}

	public ReadOnlyListProperty<PolicyModel> getAllPolicies() {
		return allPolicies;
	}

	public ReadOnlyListProperty<PolicyModel> getSimplePolicies() {
		return simplePolicies;
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public ReadOnlyListProperty<CompositePolicyModel> getCompositePolicies() {
		return (ListProperty) compositePolicies;
	}

}
