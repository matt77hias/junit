package kuleuven.group2.compile;

public interface JavaCompiler {

	/**
	 * Compile the given sources.
	 * 
	 * @return The compilation result.
	 */
	public abstract CompilationResult compile(String... sourceNames);

}