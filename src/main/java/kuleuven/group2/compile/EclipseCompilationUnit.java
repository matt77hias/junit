package kuleuven.group2.compile;

import java.util.Deque;
import java.util.LinkedList;
import java.util.StringTokenizer;

import kuleuven.group2.store.Store;
import org.apache.commons.jci.utils.ConversionUtils;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public class EclipseCompilationUnit implements ICompilationUnit {

	private final String className;

	private final String sourceName;

	private final char[] typeName;

	private final char[][] packageName;

	private final Store store;

	public EclipseCompilationUnit(final Store store, final String sourceName) {
		this.store= store;
		this.sourceName= sourceName;
		className= ConversionUtils.convertResourceToClassName(sourceName);

		Deque<char[]> classParts= getClassNameParts(className);
		typeName= classParts.removeLast();
		packageName= classParts.toArray(new char[0][]);
	}

	public char[] getFileName() {
		return sourceName.toCharArray();
	}

	public char[] getContents() {
		final byte[] content= store.read(sourceName);
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

	protected static Deque<char[]> getClassNameParts(String className) {
		// Split on dots
		final StringTokenizer tokenizer= new StringTokenizer(className, ".");
		LinkedList<char[]> parts= new LinkedList<char[]>();
		while (tokenizer.hasMoreTokens()) {
			parts.add(tokenizer.nextToken().toCharArray());
		}
		return parts;
	}

}
