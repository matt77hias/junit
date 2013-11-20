package kuleuven.group2.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * A class that represents a test method. It also keeps track of the running
 * of this method, that is the timings and results.
 * 
 * @author Ruben Pieters, Vital D'haveloose
 */
public class Test {

	protected String testClassName;
	protected String testMethodName;
	// sorted, last failures are first, first failures are last
	protected LinkedList<TestRun> testRuns = new LinkedList<TestRun>();
	
	public Test(String testClassName, String testMethodName) {
		super();
		this.testClassName= testClassName;
		this.testMethodName= testMethodName;
	}

	public String getTestClassName() {
		return testClassName;
	}

	public String getTestMethodName() {
		return testMethodName;
	}
	
	public boolean equalName(String testClassName, String testMethodName) {
		return getTestClassName().equals(testClassName) && getTestMethodName().equals(testMethodName);
	}
	
	public void addTestRun(TestRun testRun) {
		testRuns.add(testRun);
	
		// TODO: FASTER: make use of the fact that the list is sorted to insert at the right spot with binary search
		Collections.sort(testRuns, new Comparator<TestRun>() {
			@Override
			public int compare(TestRun o1, TestRun o2) {
				return - o1.getTimeStamp().compareTo(o2.getTimeStamp());
			}
		});
	}
	
	public Collection<TestRun> getTestRuns() {
		return Collections.unmodifiableList(testRuns);
	}
	
	public float getFailurePercentage(int depth) {
		float failed = 0;
		float succeeded = 0;
		
		if (depth < 0)
			depth = 0;
		
		int count = 0;
		Iterator<TestRun> iterator = testRuns.iterator();
		while(iterator.hasNext() && count < depth) {
			TestRun testRun = iterator.next();
			if (testRun.isFailedRun())
				failed++;
			if (testRun.isSuccessfulRun())
				succeeded++;
			count++;
		}
		
		if (failed + succeeded == 0)
			return 0;
		
		return failed / (failed + succeeded);
	}
	
	public Date getLastFailureTime() {
		if (testRuns.isEmpty())
			return new Date(0);
		
		for (TestRun testRun : testRuns) {
			if (testRun.isFailedRun())
				return testRun.getTimeStamp();
		}
		
		return new Date(0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testClassName == null) ? 0 : testClassName.hashCode());
		result = prime * result + ((testMethodName == null) ? 0 : testMethodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Test other = (Test) obj;
		if (testClassName == null) {
			if (other.testClassName != null) return false;
		} else if (!testClassName.equals(other.testClassName)) return false;
		if (testMethodName == null) {
			if (other.testMethodName != null) return false;
		} else if (!testMethodName.equals(other.testMethodName)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Test [testClassName=" + testClassName + ", testMethodName=" + testMethodName + ", testRuns=" + testRuns
				+ "]";
	}
	
}
