package kuleuven.group2.ui.model;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import kuleuven.group2.policy.CompositePolicy;

public class CompositePolicyModel extends PolicyModel {

	private final SimpleListProperty<WeightedPolicyModel> records = new SimpleListProperty<>(
			FXCollections.<WeightedPolicyModel> observableArrayList());

	public CompositePolicyModel(String name, CompositePolicy policy) {
		super(name, policy);
		records.addListener(new PoliciesChangeListener());
	}

	@Override
	public CompositePolicy getPolicy() {
		return (CompositePolicy) super.getPolicy();
	}

	public ObservableList<WeightedPolicyModel> getRecords() {
		return recordsProperty().get();
	}

	public ReadOnlyListProperty<WeightedPolicyModel> recordsProperty() {
		return records;
	}

	/**
	 * Forwards changes on the {@link CompositePolicyModel#getRecords() record
	 * models list} to the managed {@link CompositePolicyModel#getPolicy()
	 * composite policy}.
	 */
	protected class PoliciesChangeListener implements ListChangeListener<WeightedPolicyModel> {

		@Override
		public void onChanged(ListChangeListener.Change<? extends WeightedPolicyModel> c) {
			CompositePolicy policy = getPolicy();
			while (c.next()) {
				if (c.wasPermutated()) {
					// Permute
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						int newIndex = c.getPermutation(i);
						WeightedPolicyModel permutedModel = c.getList().get(newIndex);
						policy.setWeightedPolicyAt(newIndex, permutedModel.getRecord());
					}
				} else if (c.wasUpdated()) {
					// Update, ignore
				} else {
					// Remove
					for (WeightedPolicyModel removedModel : c.getRemoved()) {
						policy.removeWeightedPolicy(removedModel.getRecord());
					}
					// Add
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						WeightedPolicyModel addedModel = c.getList().get(i);
						policy.addWeightedPolicyAt(i, addedModel.getRecord());
					}
				}
			}
		}

	}

}
