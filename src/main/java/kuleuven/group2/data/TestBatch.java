package kuleuven.group2.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TestBatch {
	
	private List<TestRun> testRuns = new LinkedList<TestRun>();

	public TestBatch() {
		// do nothing
	}
	
	public void addTestRun(TestRun testRun) {
		testRuns.add(testRun);
	}
	
	public List<TestRun> getTestRuns() {
		return Collections.unmodifiableList(testRuns);
	}

}
