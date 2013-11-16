package kuleuven.group2.data.testrun;

import java.util.Date;

public class SuccesfullTestRun extends TestRun {

	public SuccesfullTestRun(Date timestamp) {
		super(timestamp);
	}

	@Override
	public boolean isSuccesfulRun() {
		return true;
	}

	@Override
	public boolean isFailedRun() {
		return false;
	}

}
