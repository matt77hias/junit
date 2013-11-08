package kuleuven.group2.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kuleuven.group2.store.Store;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

public class EclipseCompiler implements JavaCompiler {

	protected final Store sourceStore;

	protected final Store binaryStore;

	public EclipseCompiler(Store sourceStore, Store binaryStore) {
		this.sourceStore= sourceStore;
		this.binaryStore= binaryStore;
	}

	public CompilationResult compile(String... sourceNames) {
		final List<CompilationProblem> problems= new ArrayList<CompilationProblem>();

		// Collect compilation units
		final ICompilationUnit[] compilationUnits= new ICompilationUnit[sourceNames.length];
		for (int i= 0; i < sourceNames.length; i++) {
			String sourceName= sourceNames[i];
			if (sourceStore.contains(sourceName)) {
				compilationUnits[i]= new EclipseCompilationUnit(sourceStore,
						sourceName);
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
		final IErrorHandlingPolicy policy= DefaultErrorHandlingPolicies
				.proceedWithAllProblems();
		final IProblemFactory problemFactory= new DefaultProblemFactory(
				Locale.getDefault());
		final INameEnvironment nameEnvironment= new INameEnvironment() {
			public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
				// TODO Auto-generated method stub
				return null;
			}

			public NameEnvironmentAnswer findType(char[] typeName,
					char[][] packageName) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isPackage(char[][] parentPackageName,
					char[] packageName) {
				// TODO Auto-generated method stub
				return false;
			}

			public void cleanup() {
				// TODO Auto-generated method stub
			}
		};
		final ICompilerRequestor compilerRequestor= new CompilerRequestor(
				problems, binaryStore);

		// Compile
		final Compiler compiler= new Compiler(nameEnvironment, policy,
				new CompilerOptions(), compilerRequestor, problemFactory);
		compiler.compile(compilationUnits);

		// Return result
		return new CompilationResult(problems);
	}

	protected static String getClassName(ClassFile classFile) {
		final char[][] compoundName= classFile.getCompoundName();
		final StringBuilder className= new StringBuilder();
		for (int j= 0; j < compoundName.length; j++) {
			if (j != 0) {
				className.append('.');
			}
			className.append(compoundName[j]);
		}
		return className.toString();
	}

	protected static String getClassResourceName(ClassFile classFile) {
		return getClassName(classFile).replace('.', '/') + ".class";
	}

	/**
	 * Compiler requester which receives the compilation results.
	 * 
	 * <p>
	 * It adds problems to the given problems list and writes compiled classes
	 * into the given store.
	 * </p>
	 */
	protected static class CompilerRequestor implements ICompilerRequestor {

		protected final List<CompilationProblem> problems;

		protected final Store binaryStore;

		public CompilerRequestor(List<CompilationProblem> problems,
				Store binaryStore) {
			this.problems= problems;
			this.binaryStore= binaryStore;
		}

		public void acceptResult(
				final org.eclipse.jdt.internal.compiler.CompilationResult result) {
			if (result.hasProblems()) {
				// Store compilation problems
				for (IProblem iproblem : result.getProblems()) {
					problems.add(new EclipseCompilationProblem(iproblem));
				}
			}
			if (!result.hasErrors()) {
				// Write class files to store
				final ClassFile[] classFiles= result.getClassFiles();
				for (ClassFile classFile : classFiles) {
					binaryStore.write(getClassResourceName(classFile),
							classFile.getBytes());
				}
			}
		}

	};

}
