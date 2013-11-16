package kuleuven.group2.data;

public abstract class TestRun {

	protected float timestamp;
	
	public TestRun(float timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public abstract boolean isSuccesfulRun();
	
	public abstract boolean isFailedRun();
}
