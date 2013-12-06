package kuleuven.group2.ui.model;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestBatch;
import kuleuven.group2.data.TestDatabaseListener;
import kuleuven.group2.data.TestRun;
import kuleuven.group2.data.TestedMethod;

public class TestBatchesModel extends ListPropertyBase<TestBatchModel> implements TestDatabaseListener {

	private final Map<TestBatch, TestBatchModel> batchModels = new HashMap<>();

	public TestBatchesModel() {
		super(FXCollections.<TestBatchModel> observableArrayList());
	}

	protected TestBatchModel getModel(TestBatch testBatch) {
		TestBatchModel model = batchModels.get(testBatch);
		if (model == null) {
			// Add new model
			model = new TestBatchModel(testBatch);
			addModel(model);
		}
		return model;
	}

	protected void addModel(TestBatchModel testBatchModel) {
		// Store mapping
		batchModels.put(testBatchModel.getBatch(), testBatchModel);
		// Add to list
		add(testBatchModel);
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
	public void testRunAdded(TestRun testRun, TestBatch testBatch) {
		getModel(testBatch).addTestRun(new TestRunModel(testRun));
	}

	@Override
	public void testBatchStarted(TestBatch testBatch) {
		addModel(new TestBatchModel(testBatch));
	}

	@Override
	public void testBatchFinished(TestBatch testBatch) {
		getModel(testBatch).setRunning(false);
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
