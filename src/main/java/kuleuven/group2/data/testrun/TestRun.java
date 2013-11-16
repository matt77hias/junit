package kuleuven.group2.data.testrun;

import java.util.Date;

public abstract class TestRun {

	protected Date timestamp;
	
	public TestRun(Date timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public abstract boolean isSuccesfulRun();
	
	public abstract boolean isFailedRun();
}
