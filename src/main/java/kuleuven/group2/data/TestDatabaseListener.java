package kuleuven.group2.data;

public interface TestDatabaseListener {

	public void methodAdded(TestedMethod testedMethod);

	public void methodRemoved(TestedMethod testedMethod);

	public void testAdded(Test test);

	public void testRemoved(Test test);

	public void testRunAdded(Test test, TestRun testRun);

}
