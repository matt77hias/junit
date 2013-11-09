package kuleuven.group2.compile;

import java.util.Collection;

public interface JavaCompiler {

	/**
	 * Compile the given sources.
	 * 
	 * @param sourceNames
	 *            Names of sources to compile.
	 * @param classLoader
	 *            Class loader to lookup existing classes.
	 * @return The compilation result.
	 */
	public abstract CompilationResult compile(Collection<String> sourceNames,
			ClassLoader classLoader);

}