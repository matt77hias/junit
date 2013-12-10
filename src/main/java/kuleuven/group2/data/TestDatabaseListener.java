package kuleuven.group2.data;

public interface TestDatabaseListener {

	public void methodAdded(TestedMethod testedMethod);

	public void methodRemoved(TestedMethod testedMethod);

	public void testAdded(Test test);

	public void testRemoved(Test test);

	public void testRunAdded(TestRun testRun, TestBatch testBatch);

	public void testBatchStarted(TestBatch testBatch);

	public void testBatchFinished(TestBatch testBatch);

}
