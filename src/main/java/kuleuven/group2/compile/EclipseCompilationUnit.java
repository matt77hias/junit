package kuleuven.group2.compile;

import java.util.Arrays;

import kuleuven.group2.store.Store;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public class EclipseCompilationUnit implements ICompilationUnit {

	private final String className;
	private final String sourceName;
	private final char[] typeName;
	private final char[][] packageName;
	private final Store store;

	public EclipseCompilationUnit(final Store store, final String sourceName) {
		this.store = store;
		this.sourceName = sourceName;
		this.className = NameUtils.toClassName(sourceName);

		char[][] compoundName = NameUtils.getCompoundName(className);
		this.typeName = compoundName[compoundName.length - 1];
		this.packageName = Arrays.copyOf(compoundName, compoundName.length - 1);
	}

	public char[] getFileName() {
		return sourceName.toCharArray();
	}

	public char[] getContents() {
		final byte[] content = store.read(sourceName);
		if (content == null) {
			return null;
		}
		return new String(content).toCharArray();
	}

	public char[] getMainTypeName() {
		return typeName;
	}

	public char[][] getPackageName() {
		return packageName;
	}

	public boolean ignoreOptionalProblems() {
		return false;
	}

}
