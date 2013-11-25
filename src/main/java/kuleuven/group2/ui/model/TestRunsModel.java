package kuleuven.group2.ui.model;

import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabaseListener;
import kuleuven.group2.data.TestRun;
import kuleuven.group2.data.TestedMethod;

public class TestRunsModel extends ListPropertyBase<TestRunModel> implements TestDatabaseListener {

	public TestRunsModel() {
		super(FXCollections.<TestRunModel> observableArrayList());
	}

	@Override
	public void methodAdded(TestedMethod testedMethod) {
	}

	@Override
	public void methodRemoved(TestedMethod testedMethod) {
	}

	@Override
	public void testAdded(Test test) {
	}

	@Override
	public void testRemoved(Test test) {
	}

	@Override
	public void testRunAdded(Test test, TestRun testRun) {
		add(new TestRunModel(test, testRun));
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
