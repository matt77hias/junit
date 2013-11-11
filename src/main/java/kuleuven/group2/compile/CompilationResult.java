package kuleuven.group2.compile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code CompilationResult} represents the result of a compilation. It
 * contains either compilation errors (which caused the compilation to fail) or
 * warnings (which the compilation detected but ignored).
 */
public class CompilationResult {

	private final Map<String, byte[]> compiled = new HashMap<String, byte[]>();
	private final List<CompilationProblem> errors = new ArrayList<CompilationProblem>();
	private final List<CompilationProblem> warnings = new ArrayList<CompilationProblem>();

	public CompilationResult(Collection<CompilationProblem> problems, Map<String, byte[]> compiled) {
		this.compiled.putAll(compiled);
		for (CompilationProblem problem : problems) {
			if (problem.isError()) {
				this.errors.add(problem);
			} else {
				this.warnings.add(problem);
			}
		}
	}

	public CompilationResult(Collection<CompilationProblem> problems) {
		this(problems, Collections.<String, byte[]> emptyMap());
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
	 * Get all compiled resources.
	 */
	public Map<String, byte[]> getCompiledResources() {
		return Collections.unmodifiableMap(compiled);
	}

	/**
	 * Get the names of all compiled resources.
	 */
	public Collection<String> getCompiledResourceNames() {
		return getCompiledResources().keySet();
	}

	/**
	 * Get the compiled resource by the given name.
	 */
	public byte[] getCompiledResource(String resourceName) {
		return getCompiledResources().get(resourceName);
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