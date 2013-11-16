package kuleuven.group2.data.testrun;

import java.util.Date;

public class FailedTestRun extends TestRun {

	public FailedTestRun(Date timestamp) {
		super(timestamp);
	}

	@Override
	public boolean isSuccesfulRun() {
		return false;
	}

	@Override
	public boolean isFailedRun() {
		return true;
	}

}
