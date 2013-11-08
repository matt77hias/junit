package kuleuven.group2.compile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A CompilationResult represents the result of a compilation. It contains
 * either compilation errors (which caused the compilation to fail) or warnings
 * (which the compilation detected but ignored).
 */
public class CompilationResult {

	private final List<CompilationProblem> errors;

	private final List<CompilationProblem> warnings;

	public CompilationResult(Collection<CompilationProblem> problems) {
		errors= new ArrayList<CompilationProblem>();
		warnings= new ArrayList<CompilationProblem>();

		for (CompilationProblem problem : problems) {
			if (problem.isError()) {
				errors.add(problem);
			} else {
				warnings.add(problem);
			}
		}
	}

	/**
	 * Was the compilation successful?
	 * 
	 * @return true if the compilation completed without errors.
	 */
	public boolean isSuccess() {
		return getErrors().isEmpty();
	}

	public List<CompilationProblem> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	public List<CompilationProblem> getWarnings() {
		return Collections.unmodifiableList(warnings);
	}

}