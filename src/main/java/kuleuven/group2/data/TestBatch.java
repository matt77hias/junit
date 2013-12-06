package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestBatch {

	private final Date startDate;
	private Date endDate;

	private final List<TestRun> testRuns = new ArrayList<TestRun>();

	public TestBatch(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isRunning() {
		return getEndDate() == null;
	}

	public Long getDuration() {
		return isRunning() ? null : (getEndDate().getTime() - getStartDate().getTime());
	}

	public List<TestRun> getTestRuns() {
		return Collections.unmodifiableList(testRuns);
	}

	public void addTestRun(TestRun testRun) {
		testRuns.add(testRun);
	}

}
