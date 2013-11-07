package kuleuven.group2.compile;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.jci.ReloadingClassLoader;
import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.EclipseJavaCompiler;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.listeners.CompilingListener;
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.MemoryResourceStore;
import org.apache.commons.jci.stores.TransactionalResourceStore;

public class Compiler {

	private final Path sourceDir;

	private final TransactionalResourceStore binaryStore;

	private final JavaCompiler compiler;

	private final CompilingListener compilingListener;

	private final ReloadingClassLoader classLoader;

	private boolean isListening= false;

	private final FilesystemAlterationMonitor fam;

	public Compiler(Path sourceDir) {
		this.sourceDir= sourceDir;

		// Compile with the Eclipse compiler
		this.compiler= new EclipseJavaCompiler();

		// Compile when source changes
		// Store binary classes in memory
		binaryStore= new TransactionalResourceStore(new MemoryResourceStore());
		// Alternative using file system
		// binaryStore = new TransactionalResourceStore(new
		// FileResourceStore(binaryDir.toFile()));
		compilingListener= new CompilingListener(compiler, binaryStore);

		// Reload classes after compilation
		classLoader= new ReloadingClassLoader(this.getClass().getClassLoader());
		compilingListener.addReloadNotificationListener(classLoader);

		// Listen for source changes
		// TODO Replace with our own implementation using WatchService
		fam= new FilesystemAlterationMonitor();
		fam.addListener(sourceDir.toFile(), compilingListener);
	}

	public Path getSourceDirectory() {
		return sourceDir;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Compile all source files in the {@link #getSourceDirectory() source
	 * directory}.
	 * 
	 * @return The compilation result.
	 */
	public CompilationResult compile() {
		// Pause listening
		boolean wasListening= isListening;
		if (wasListening) {
			stop();
		}

		// Collect all Java files
		File sourceFile= sourceDir.toFile();
		Collection<File> sourceFiles= FileUtils.listFiles(sourceFile,
				new String[] { ".java" }, true);
		String[] sourcePaths= new String[sourceFiles.size()];
		int i= 0;
		for (File javaFile : sourceFiles) {
			sourcePaths[i]= javaFile.getAbsolutePath();
			i++;
		}

		// Compile
		CompilationResult result= compiler.compile(sourcePaths,
				new FileResourceReader(sourceFile), binaryStore);

		// Resume listening
		if (wasListening) {
			start();
		}

		return result;
	}

	/**
	 * Start listening for source changes.
	 */
	public void start() {
		if (!isListening) {
			isListening= true;
			fam.start();
		}
	}

	/**
	 * Stop listening for source changes.
	 */
	public void stop() {
		if (isListening) {
			isListening= false;
			fam.stop();
		}
	}

}
