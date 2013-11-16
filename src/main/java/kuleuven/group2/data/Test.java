package kuleuven.group2.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import kuleuven.group2.data.testrun.TestRun;

/**
 * A class that represents a test method. It also keeps track of the running
 * of this method, that is the timings and results.
 * 
 * @author Ruben Pieters, Vital D'haveloose
 */
public class Test {

	protected String testClassName;
	protected String testMethodName;
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
	
		Collections.sort(testRuns, new Comparator<TestRun>() {
			@Override
			public int compare(TestRun o1, TestRun o2) {
				return o1.getTimeStamp().compareTo(o2.getTimeStamp());
			}
		});
	}
	
	public Collection<TestRun> getTestRuns() {
		return testRuns;
	}
	
	public Date getLastFailureTime() {
		if (testRuns.isEmpty())
			return new Date(0);
		TestRun latestFailureRun = Collections.max(testRuns, new Comparator<TestRun>() {
			@Override
			public int compare(TestRun o1, TestRun o2) {
				if (o1.isSuccesfulRun() && o2.isFailedRun())
					return -1;
				if (o2.isFailedRun() && o1.isSuccesfulRun())
					return 1;
				return o1.getTimeStamp().compareTo(o2.getTimeStamp());
			}
			
		});
		
		if (latestFailureRun == null)
			return new Date(0);
		if (latestFailureRun.isSuccesfulRun())
			return new Date(0);
		
		return latestFailureRun.getTimeStamp();
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
