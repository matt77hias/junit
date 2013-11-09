package kuleuven.group2.compile;

import org.apache.commons.lang.StringUtils;

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
		return stripExtension(resourceName).replace('/', '.');
	}

	public static String getClassName(char[][] compoundName) {
		return StringUtils.join(compoundName, '.');
	}

	public static String getClassName(char[] typeName, char[][] packageName) {
		return StringUtils.join(packageName, '.') + '.' + new String(typeName);
	}

	public static String getPackageName(char[][] parentPackageName,
			char[] packageName) {
		return StringUtils.join(parentPackageName, '.') + '.'
				+ new String(packageName);
	}

	public static char[][] getCompoundName(String className) {
		return toCharArrays(StringUtils.split(className, '.'));
	}

	protected static char[][] toCharArrays(String... strings) {
		char[][] charArrays= new char[strings.length][];
		for (int i= 0; i < strings.length; i++) {
			charArrays[i]= strings[i].toCharArray();
		}
		return charArrays;
	}

	protected static String stripExtension(String name) {
		int i= name.lastIndexOf('.');
		return (i < 0) ? name : name.substring(0, i);
	}

}
