package kuleuven.group2.compile;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.io.Files;

public class NameUtils {

	private NameUtils() {
	}

	public static String toSourceName(String className) {
		return className.replace('.', '/') + ".java";
	}

	public static String toBinaryName(String className) {
		return className.replace('.', '/') + ".class";
	}

	public static String toClassName(String resourceName) {
		return Files.getNameWithoutExtension(resourceName).replace('/', '.');
	}

	public static Collection<String> toClassNames(Collection<String> resourceNames) {
		return Collections2.transform(resourceNames, new Function<String, String>() {
			@Override
			public String apply(String resourceName) {
				return toClassName(resourceName);
			}
		});
	}

	public static String getClassName(char[][] compoundName) {
		return join(compoundName).toString();
	}

	public static String getClassName(char[] typeName, char[][] packageName) {
		return joinPrefix(packageName).append(typeName).toString();
	}

	public static String getPackageName(char[][] parentPackageName, char[] packageName) {
		return joinPrefix(parentPackageName).append(packageName).toString();
	}

	public static char[][] getCompoundName(String className) {
		return toCharArrays(Splitter.on('.').splitToList(className));
	}

	protected static char[][] toCharArrays(List<String> strings) {
		char[][] charArrays = new char[strings.size()][];
		for (ListIterator<String> it = strings.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			charArrays[index] = it.next().toCharArray();
		}
		return charArrays;
	}

	protected static StringBuilder join(char[][] parts) {
		StringBuilder joined = new StringBuilder();
		if (parts != null) {
			for (int i = 0; i < parts.length; i++) {
				if (i != 0) joined.append('.');
				joined.append(parts[i]);
			}
		}
		return joined;
	}

	protected static StringBuilder joinPrefix(char[][] parts) {
		StringBuilder joined = new StringBuilder();
		if (parts != null) {
			for (int i = 0; i < parts.length; i++) {
				joined.append(parts[i]);
				joined.append('.');
			}
		}
		return joined;
	}

}
