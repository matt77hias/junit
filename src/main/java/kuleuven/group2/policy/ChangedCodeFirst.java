package kuleuven.group2.policy;

import java.util.Comparator;

import kuleuven.group2.data.Test;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;

import com.google.common.primitives.Longs;

/**
 * A class representing the changed code first policy.
 * 
 * @author Group 2
 * @version 17 November 2013
 * 
 */
public class ChangedCodeFirst extends ComparingPolicy {

	/**
	 * Creates a new changed code first policy.
	 */
	public ChangedCodeFirst() {

	}

	/**
	 * Get the last change time of a test. This is the latest change time of all
	 * methods linked to the test.
	 * 
	 * @param testDatabase
	 *            The test database.
	 * @param test
	 *            The test.
	 * @return The latest change time, or {@link Long#MIN_VALUE} if the test was
	 *         never changed.
	 */
	protected long getLastChangeTime(TestDatabase testDatabase, Test test) {
		long maxChangeTime = Long.MIN_VALUE;
		for (TestedMethod method : testDatabase.getLinkedMethods(test)) {
			long changeTime = method.getLastChange().getTime();
			if (changeTime > maxChangeTime) {
				maxChangeTime = changeTime;
			}
		}
		return maxChangeTime;
	}

	@Override
	protected Comparator<? super Test> getComparator(final TestDatabase testDatabase) {
		return new Comparator<Test>() {
			@Override
			public int compare(Test left, Test right) {
				long leftTime = getLastChangeTime(testDatabase, left);
				long rightTime = getLastChangeTime(testDatabase, right);
				return Longs.compare(leftTime, rightTime);
			}
		};
	}

}
