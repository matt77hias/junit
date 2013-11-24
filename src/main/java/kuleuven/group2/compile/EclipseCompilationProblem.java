package kuleuven.group2.compile;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 * A compilation problem which occured when using the Eclipse Compiler.
 * 
 * @author Group2
 * @version 5 November 2013
 */
public class EclipseCompilationProblem implements CompilationProblem {

	protected final IProblem problem;

	public EclipseCompilationProblem(final IProblem problem) {
		this.problem = problem;
	}

	public boolean isError() {
		return problem.isError();
	}

	public String getResourceName() {
		return new String(problem.getOriginatingFileName());
	}

	public int getStartLine() {
		return problem.getSourceLineNumber();
	}

	public int getStartColumn() {
		return problem.getSourceStart();
	}

	public int getEndLine() {
		return getStartLine();
	}

	public int getEndColumn() {
		return problem.getSourceEnd();
	}

	public String getMessage() {
		return problem.getMessage();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getResourceName()).append(" (");
		sb.append(getStartLine());
		sb.append(":");
		sb.append(getStartColumn());
		sb.append(") : ");
		sb.append(getMessage());
		return sb.toString();
	}

}
