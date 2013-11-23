package kuleuven.group2.compile;

/**
 * A problem caused by the not existing of a referenced source file.
 * 
 * @author Group2
 * @version 5 November 2013
 */
public class SourceNotFoundProblem implements CompilationProblem {

	private final String resourceName;

	public SourceNotFoundProblem(String resourceName) {
		this.resourceName = resourceName;
	}

	public boolean isError() {
		return true;
	}

	public String getResourceName() {
		return resourceName;
	}

	public int getStartLine() {
		return 0;
	}

	public int getStartColumn() {
		return 0;
	}

	public int getEndLine() {
		return 0;
	}

	public int getEndColumn() {
		return 0;
	}

	public String getMessage() {
		return "Source " + getResourceName() + " could not be found";
	}

	@Override
	public String toString() {
		return getMessage();
	}

}
