package kuleuven.group2.ui.model;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import kuleuven.group2.policy.CompositeTestSortingPolicy;

public class CompositePolicyModel extends PolicyModel {

	private final SimpleListProperty<WeightedPolicyModel> weightedPolicies = new SimpleListProperty<>(
			FXCollections.<WeightedPolicyModel> observableArrayList());

	public CompositePolicyModel(String name, CompositeTestSortingPolicy policy) {
		super(name, policy);
		weightedPolicies.addListener(new PoliciesChangeListener());
	}

	@Override
	public CompositeTestSortingPolicy getPolicy() {
		return (CompositeTestSortingPolicy) super.getPolicy();
	}

	public ObservableList<WeightedPolicyModel> getWeightedPolicies() {
		return weightedPoliciesProperty().get();
	}

	public ReadOnlyListProperty<WeightedPolicyModel> weightedPoliciesProperty() {
		return weightedPolicies;
	}

	/**
	 * Forwards changes on the
	 * {@link CompositePolicyModel#getWeightedPolicies() weighted policy models
	 * list} to the managed {@link CompositePolicyModel#getPolicy() composite
	 * policy}.
	 */
	protected class PoliciesChangeListener implements ListChangeListener<WeightedPolicyModel> {

		@Override
		public void onChanged(ListChangeListener.Change<? extends WeightedPolicyModel> c) {
			CompositeTestSortingPolicy policy = getPolicy();
			while (c.next()) {
				if (c.wasPermutated()) {
					// Permute
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						int newIndex = c.getPermutation(i);
						WeightedPolicyModel permutedModel = c.getList().get(newIndex);
						policy.setWeightedPolicyAt(newIndex, permutedModel.getWeightedPolicy());
					}
				} else if (c.wasUpdated()) {
					// Update, ignore
				} else {
					// Remove
					for (WeightedPolicyModel removedModel : c.getRemoved()) {
						policy.removeWeightedPolicy(removedModel.getWeightedPolicy());
					}
					// Add
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						WeightedPolicyModel addedModel = c.getList().get(i);
						policy.addWeightedPolicyAt(i, addedModel.getWeightedPolicy());
					}
				}
			}
		}

	}

}
