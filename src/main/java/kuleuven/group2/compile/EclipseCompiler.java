package kuleuven.group2.compile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kuleuven.group2.store.Store;
import kuleuven.group2.store.StoreFilter;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

import com.google.common.io.ByteStreams;

public class EclipseCompiler implements JavaCompiler {

	protected final Store sourceStore;
	protected final Store binaryStore;

	public EclipseCompiler(Store sourceStore, Store binaryStore) {
		this.sourceStore = sourceStore;
		this.binaryStore = binaryStore;
	}

	public Store getSourceStore() {
		return sourceStore;
	}

	public Store getBinaryStore() {
		return binaryStore;
	}

	@Override
	public CompilationResult compile(ClassLoader classLoader) {
		return compile(getSourceStore().getFiltered(StoreFilter.SOURCE), classLoader);
	}

	@Override
	public CompilationResult compile(final Collection<String> sourceNames, final ClassLoader classLoader) {
		final Map<String, byte[]> compiled = new HashMap<String, byte[]>();
		final List<CompilationProblem> problems = new ArrayList<CompilationProblem>();

		// Collect compilation units
		final List<ICompilationUnit> compilationUnits = new ArrayList<ICompilationUnit>(sourceNames.size());
		for (String sourceName : sourceNames) {
			if (getSourceStore().contains(sourceName)) {
				compilationUnits.add(new EclipseCompilationUnit(getSourceStore(), sourceName));
			} else {
				// Source not found, error
				problems.add(new SourceNotFoundProblem(sourceName));
			}
		}

		// Exit if problems
		if (!problems.isEmpty()) {
			return new CompilationResult(problems);
		}

		// Setup compiler environment
		final IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
		final IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
		final INameEnvironment nameEnvironment = new NameEnvironment(classLoader);
		final ICompilerRequestor compilerRequestor = new CompilerRequestor(problems, compiled);

		// Compile
		final Compiler compiler = new Compiler(nameEnvironment, policy, new CompilerOptions(), compilerRequestor,
				problemFactory);
		compiler.compile(compilationUnits.toArray(new ICompilationUnit[0]));

		// Return result
		return new CompilationResult(problems, compiled);
	}

	/**
	 * Name environment which looks up types and packages in the given class
	 * loader and the binary store.
	 */
	protected class NameEnvironment implements INameEnvironment {

		private final ClassLoader classLoader;

		private NameEnvironment(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
			return findType(NameUtils.getClassName(compoundTypeName));
		}

		public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
			return findType(NameUtils.getClassName(typeName, packageName));
		}

		protected NameEnvironmentAnswer findType(String className) {
			if (isPackage(className)) {
				return null;
			}
			String classResourceName = NameUtils.toBinaryName(className);

			// Find in binary store
			byte[] classBytes = getBinaryStore().read(classResourceName);
			if (classBytes != null) {
				// Found, produce answer
				return createFindTypeAnswer(className, classBytes);
			}

			// Find in class loader
			try (InputStream is = classLoader.getResourceAsStream(classResourceName)) {
				if (is != null) {
					// Found, produce answer
					return createFindTypeAnswer(className, ByteStreams.toByteArray(is));
				}
			} catch (IOException e) {
				// Ignore
			}
			return null;
		}

		protected NameEnvironmentAnswer createFindTypeAnswer(String className, byte[] classBytes) {
			final char[] fileName = className.toCharArray();
			try {
				final ClassFileReader classFileReader = new ClassFileReader(classBytes, fileName, true);
				return new NameEnvironmentAnswer(classFileReader, null);
			} catch (final ClassFormatException e) {
				// Wrong class format
				return null;
			}
		}

		public boolean isPackage(char[][] parentPackageName, char[] packageName) {
			return isPackage(NameUtils.getPackageName(parentPackageName, packageName));
		}

		private boolean isPackage(final String className) {
			// Early and cheap reject: "-" is not valid in package names
			if (className.contains("-")) {
				return false;
			}

			// Check for loaded class
			String classResourceName = NameUtils.toBinaryName(className);
			try (InputStream is = classLoader.getResourceAsStream(classResourceName)) {
				if (is != null) {
					// Class found, not a package
					return false;
				}
			} catch (IOException e) {
				// Ignore
			}

			// Check for source resource
			String sourceResourceName = NameUtils.toSourceName(className);
			if (getSourceStore().contains(sourceResourceName)) {
				// Source found, not a package
				return false;
			}

			// Assume everything else is a package
			return true;
		}

		public void cleanup() {
			// No cleaning up to do
		}
	}

	/**
	 * Compiler requester which adds problems to the given problems list and
	 * writes compiled classes into the binary store.
	 */
	protected class CompilerRequestor implements ICompilerRequestor {

		protected final List<CompilationProblem> problems;
		protected final Map<String, byte[]> compiled;

		public CompilerRequestor(List<CompilationProblem> problems, Map<String, byte[]> compiled) {
			this.problems = problems;
			this.compiled = compiled;
		}

		public void acceptResult(final org.eclipse.jdt.internal.compiler.CompilationResult result) {
			if (result.hasProblems()) {
				// Store compilation problems
				for (IProblem iproblem : result.getProblems()) {
					problems.add(new EclipseCompilationProblem(iproblem));
				}
			}
			if (!result.hasErrors()) {
				final ClassFile[] classFiles = result.getClassFiles();
				for (ClassFile classFile : classFiles) {
					// Write class file to store
					String className = NameUtils.getClassName(classFile.getCompoundName());
					String resourceName = NameUtils.toBinaryName(className);
					byte[] classBytes = classFile.getBytes();
					getBinaryStore().write(resourceName, classBytes);
					// Add to compiled resources
					compiled.put(resourceName, classBytes);
				}
			}
		}

	};

}
