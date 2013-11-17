package kuleuven.group2.policy;

import java.util.Comparator;
import java.util.List;

import kuleuven.group2.data.Test;

public interface Policy extends Comparator<Test> {
	
	public Test[] getSortedTestAccordingToPolicy(Test[] tests);
			
	public List<Test> getSortedTestAccordingToPolicy(List<Test> tests);
}