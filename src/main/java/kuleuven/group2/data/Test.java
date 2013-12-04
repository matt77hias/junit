package kuleuven.group2.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Iterables;

/**
 * A class that represents a test method. It also keeps track of the runnings of
 * this method.
 * 
 * @author Group2
 * @version 13 November 2013
 */
public class Test {

	protected String testClassName;
	protected String testMethodName;

	protected boolean isTestRunsSorted = true;
	protected List<TestRun> testRuns = new LinkedList<TestRun>();

	public Test(String testClassName, String testMethodName) {
		this.testClassName = checkNotNull(testClassName);
		this.testMethodName = checkNotNull(testMethodName);
	}

	public String getTestClassName() {
		return testClassName;
	}

	public String getTestMethodName() {
		return testMethodName;
	}

	public void addTestRun(TestRun testRun) {
		testRuns.add(testRun);
		isTestRunsSorted = false;
	}

	public List<TestRun> getTestRuns() {
		if (!isTestRunsSorted) {
			// Sort: last test run first
			Collections.sort(testRuns, new Comparator<TestRun>() {
				@Override
				public int compare(TestRun o1, TestRun o2) {
					return -o1.getTimeStamp().compareTo(o2.getTimeStamp());
				}
			});
			isTestRunsSorted = true;
		}
		return Collections.unmodifiableList(testRuns);
	}
	
	public float getFailurePercentage(int depth) {
		int failed = 0;
		int succeeded = 0;

		if (depth < 0) depth = 0;

		for (TestRun testRun : Iterables.limit(getTestRuns(), depth)) {
			if (testRun.isFailedRun()) failed++;
			if (testRun.isSuccessfulRun()) succeeded++;
		}

		if (failed + succeeded == 0) return 0f;

		return ((float) failed) / ((float) (failed + succeeded));
	}

	public Date getLastFailureTime() {
		List<TestRun> testRuns = getTestRuns();
		if (testRuns.isEmpty()) return new Date(0);

		for (TestRun testRun : testRuns) {
			if (testRun.isFailedRun()) return testRun.getTimeStamp();
		}

		return new Date(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + testClassName.hashCode();
		result = prime * result + testMethodName.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Test other = (Test) obj;
		if (!testClassName.equals(other.testClassName)) return false;
		if (!testMethodName.equals(other.testMethodName)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Test [testClassName=" + testClassName + ", testMethodName=" + testMethodName + ", testRuns="
				+ getTestRuns() + "]";
	}

}
