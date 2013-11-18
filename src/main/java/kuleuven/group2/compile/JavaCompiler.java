package kuleuven.group2.compile;

import java.util.Collection;

import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreFilter;

public abstract class JavaCompiler {

	protected final Store sourceStore;
	protected final Store binaryStore;
	protected final ClassLoader classLoader;

	/**
	 * Create a new Java compiler.
	 * 
	 * @param sourceStore
	 *            Store containing sources.
	 * @param binaryStore
	 *            Store receiving binaries.
	 * @param classLoader
	 *            Class loader to lookup existing classes.
	 */
	public JavaCompiler(Store sourceStore, Store binaryStore, ClassLoader classLoader) {
		this.sourceStore = sourceStore;
		this.binaryStore = binaryStore;
		this.classLoader = classLoader;
	}

	protected Store getSourceStore() {
		return sourceStore;
	}

	protected Store getBinaryStore() {
		return binaryStore;
	}

	protected ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Compile all sources in source store to the binary store.
	 * 
	 * @return The compilation result.
	 */
	public CompilationResult compileAll() {
		return compile(getSourceStore().getFiltered(StoreFilter.SOURCE));
	}

	/**
	 * Compile the given sources from the source store to the binary store.
	 * 
	 * @param sourceNames
	 *            Names of sources to compile.
	 * @return The compilation result.
	 */
	public abstract CompilationResult compile(Collection<String> sourceNames);

}