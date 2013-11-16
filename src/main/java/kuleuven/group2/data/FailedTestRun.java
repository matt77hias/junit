package kuleuven.group2.data;

public class FailedTestRun extends TestRun {

	public FailedTestRun(float timestamp) {
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
