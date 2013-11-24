package kuleuven.group2.ui.model;

import java.util.Date;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestRun;

public class TestRunModel {

	private final Test test;
	private final TestRun run;

	public TestRunModel(Test test, TestRun testRun) {
		this.test = test;
		this.run = testRun;
	}

	public String getTestClassName() {
		return test.getTestClassName();
	}

	public String getTestMethodName() {
		return test.getTestMethodName();
	}

	public Date getTimeStamp() {
		return run.getTimeStamp();
	}

	public boolean isSuccessfulRun() {
		return run.isSuccessfulRun();
	}

}
