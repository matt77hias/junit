package kuleuven.group2.data;

public class SuccesfullTestRun extends TestRun {

	public SuccesfullTestRun(float timestamp) {
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
