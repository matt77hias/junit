package kuleuven.group2.compile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A {@code CompilationResult} represents the result of a compilation. It
 * contains either compilation errors (which caused the compilation to fail) or
 * warnings (which the compilation detected but ignored).
 */
public class CompilationResult {

	private final List<String> compiled = new ArrayList<String>();

	private final List<CompilationProblem> errors = new ArrayList<CompilationProblem>();

	private final List<CompilationProblem> warnings = new ArrayList<CompilationProblem>();

	public CompilationResult(Collection<CompilationProblem> problems, Collection<String> compiled) {
		this.compiled.addAll(compiled);
		for (CompilationProblem problem : problems) {
			if (problem.isError()) {
				this.errors.add(problem);
			} else {
				this.warnings.add(problem);
			}
		}
	}

	public CompilationResult(Collection<CompilationProblem> problems) {
		this(problems, Collections.<String> emptyList());
	}

	/**
	 * Was the compilation successful?
	 * 
	 * @return true if the compilation completed without errors.
	 */
	public boolean isSuccess() {
		return getErrors().isEmpty();
	}

	/**
	 * Get the names of all compiled resources.
	 */
	public List<String> getCompiledResources() {
		return Collections.unmodifiableList(compiled);
	}

	/**
	 * Get the list of compilation errors.
	 */
	public List<CompilationProblem> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	/**
	 * Get the list of compilation warnings.
	 */
	public List<CompilationProblem> getWarnings() {
		return Collections.unmodifiableList(warnings);
	}

}