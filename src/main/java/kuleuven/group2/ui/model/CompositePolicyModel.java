package kuleuven.group2.ui.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import kuleuven.group2.policy.CompositePolicy;

public class CompositePolicyModel extends PolicyModel {

	private final ObservableList<PolicyRecordModel> records = FXCollections.observableArrayList();

	public CompositePolicyModel(String name, CompositePolicy policy) {
		super(name, policy);
		records.addListener(new PoliciesChangeListener());
	}

	@Override
	public CompositePolicy getPolicy() {
		return (CompositePolicy) super.getPolicy();
	}

	public ObservableList<PolicyRecordModel> getRecords() {
		return records;
	}

	/**
	 * Forwards changes on the {@link CompositePolicyModel#getRecords() record
	 * models list} to the managed {@link CompositePolicyModel#getPolicy()
	 * composite policy}.
	 */
	protected class PoliciesChangeListener implements ListChangeListener<PolicyRecordModel> {

		@Override
		public void onChanged(ListChangeListener.Change<? extends PolicyRecordModel> c) {
			CompositePolicy policy = getPolicy();
			while (c.next()) {
				if (c.wasPermutated()) {
					// Permute
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						int newIndex = c.getPermutation(i);
						PolicyRecordModel permutedModel = c.getList().get(newIndex);
						policy.setPolicyRecordAt(newIndex, permutedModel.getRecord());
					}
				} else if (c.wasUpdated()) {
					// Update, ignore
				} else {
					// Remove
					for (PolicyRecordModel removedModel : c.getRemoved()) {
						policy.removePolicy(removedModel.getRecord());
					}
					// Add
					for (int i = c.getFrom(); i < c.getTo(); ++i) {
						PolicyRecordModel addedModel = c.getList().get(i);
						policy.addPolicyRecordAt(i, addedModel.getRecord());
					}
				}
			}
		}

	}

}
