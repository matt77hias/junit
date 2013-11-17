package kuleuven.group2.policy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kuleuven.group2.data.Test;

public class LastFailureFirst implements Policy {
	
	public LastFailureFirst() {
		
	}
	
	@Override
	public Test[] getSortedTestAccordingToPolicy(Test[] tests) {
		Arrays.sort(tests, 0, tests.length, this);
		return tests;
	}
	
	@Override
	public List<Test> getSortedTestAccordingToPolicy(List<Test> tests) {
		Collections.sort(tests, this);
		return tests;
	}

	@Override
	public int compare(Test o1, Test o2) {
		return o2.getLastFailureTime().compareTo(o1.getLastFailureTime());
	}

}
