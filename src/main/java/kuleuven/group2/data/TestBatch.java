package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestBatch {

	private final Date timestamp;
	private final List<TestRun> testRuns = new ArrayList<TestRun>();

	public TestBatch(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public List<TestRun> getTestRuns() {
		return Collections.unmodifiableList(testRuns);
	}

	public void addTestRun(TestRun testRun) {
		testRuns.add(testRun);
	}

}
