package kuleuven.group2.testrunner;

import kuleuven.group2.data.Test;

import org.junit.runner.Result;

/**
 * Interface for classes that have to listen to the starting and stopping
 * of individual tests as well as whole pipeline-runs.
 * 
 * @author Group 2
 * @version 22 November 2013
 */
public interface TestRunnerListener {
	
	public void pipelineRunStarted(Test[] tests);
	
	public void testStarted(Test test);
	
	public void testStopped(Result result);
	
	public void pipelineRunStopped(Result[] results);

}
