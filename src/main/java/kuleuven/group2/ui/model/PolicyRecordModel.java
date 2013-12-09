package kuleuven.group2.ui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import kuleuven.group2.policy.PolicyRecord;

public class PolicyRecordModel {

	private final PolicyRecord policyRecord;
	private final PolicyModel policy;
	private final IntegerProperty weight = new SimpleIntegerProperty();

	public PolicyRecordModel(PolicyModel policy, int weight) {
		this.policyRecord = new PolicyRecord(policy.getPolicy(), weight);
		this.policy = policy;
		this.weight.set(policyRecord.getWeight());
		this.weight.addListener(new WeightChangeListener());
	}

	public PolicyRecord getRecord() {
		return policyRecord;
	}

	public PolicyModel getPolicy() {
		return policy;
	}

	public int getWeight() {
		return weightProperty().get();
	}

	public void setWeight(int weight) {
		weightProperty().set(weight);
	}

	public IntegerProperty weightProperty() {
		return weight;
	}

	protected class WeightChangeListener implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			int newWeight = newValue.intValue();
			policyRecord.setWeight(newWeight);
			// The new value may be invalid, so check the result
			int actualWeight = getRecord().getWeight();
			if (newWeight != actualWeight) {
				setWeight(actualWeight);
			}
		}
	}

}
