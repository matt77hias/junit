package kuleuven.group2.compile;

/**
 * Represents a problem that occured during compilation.
 * 
 * @author Group2
 * @version 5 November 2013
 */
public interface CompilationProblem {

	/**
	 * Is the problem an error and compilation cannot continue or just a warning
	 * and compilation can proceed?
	 * 
	 * @return true if the problem is an error
	 */
	boolean isError();

	/**
	 * Name of the resource where the problem occurred.
	 * 
	 * @return resource name
	 */
	String getResourceName();

	/**
	 * Line number of where the problem starts in the source code.
	 * 
	 * @return line number of problem start
	 */
	int getStartLine();

	/**
	 * Column number of where the problem starts in the source code.
	 * 
	 * @return column number of start of problem
	 */
	int getStartColumn();

	/**
	 * Line number of where the problem stops in the source code.
	 * 
	 * @return line number of end of problem
	 */
	int getEndLine();

	/**
	 * Column number of where the problem stops in the source code.
	 * 
	 * @return column number of end of problem
	 */
	int getEndColumn();

	/**
	 * Description of the problem.
	 * 
	 * @return problem description
	 */
	String getMessage();

}
