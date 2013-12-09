package kuleuven.group2.ui.model;

import java.util.List;

import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import kuleuven.group2.policy.ChangedCodeFirst;
import kuleuven.group2.policy.DistinctFailureFirst;
import kuleuven.group2.policy.FrequentFailureFirst;
import kuleuven.group2.policy.LastFailureFirst;

import com.google.common.collect.ImmutableList;

public class PoliciesModel extends ListPropertyBase<PolicyModel> {

	public static final ImmutableList<PolicyModel> DEFAULTS = ImmutableList.<PolicyModel> copyOf(new PolicyModel[] {
			new PolicyModel("Changed code first", new ChangedCodeFirst()),
			new PolicyModel("Distinct failure first", new DistinctFailureFirst()),
			new PolicyModel("Frequent failure first", new FrequentFailureFirst()),
			new PolicyModel("Last failure first", new LastFailureFirst())
	});

	public PoliciesModel() {
		super(FXCollections.<PolicyModel> observableArrayList());
	}

	public PoliciesModel(List<PolicyModel> list) {
		this();
		addAll(list);
	}

	@Override
	public Object getBean() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

}
