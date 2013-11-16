package kuleuven.group2.data.signature;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public class JavaSignatureParser {

	protected final String signature;

	/**
	 * Matches zero or more pattern parts separated by periods or slashes. The
	 * final period or slash is omitted in the resulting group. If no package is
	 * specified (e.g. the default package), the group doesn't matchs.
	 */
	protected static final String PACKAGE_PATTERN = "(?:((?:[^./]+[./])*[^./]+)[./])?";

	/**
	 * Matches a class name and the following period separating it from the
	 * method name.
	 */
	protected static final String CLASS_PATTERN = "([^.]+)\\.";

	/**
	 * Matches a method name.
	 */
	protected static final String METHOD_PATTERN = "([^(]+)";

	/**
	 * Matches an arguments list surrounded with parentheses.
	 */
	protected static final String ARGUMENTS_PATTERN = "\\(([^)]*)\\)";

	/**
	 * Matches a return type.
	 */
	protected static final String RETURN_PATTERN = "(.+)";

	/**
	 * Matches a complete method signature.
	 */
	protected static final Pattern SIGNATURE_PATTERN = Pattern.compile("^" + PACKAGE_PATTERN + CLASS_PATTERN
			+ METHOD_PATTERN + ARGUMENTS_PATTERN + RETURN_PATTERN + "$");

	public JavaSignatureParser(String signature) {
		this.signature = signature;
	}

	public JavaSignature parseSignature() throws IllegalArgumentException {
		Matcher match = SIGNATURE_PATTERN.matcher(signature);
		if (!match.matches()) {
			throw new IllegalArgumentException("Incorrectly formatted signature");
		}

		String packageName = Strings.nullToEmpty(match.group(1)).replace('/', '.');
		String className = match.group(2);
		String methodName = match.group(3);
		List<String> arguments = Splitter.on(';').omitEmptyStrings().splitToList(match.group(4));
		String returnType = match.group(5);

		return new JavaSignature(methodName, className, packageName, arguments, returnType);
	}

}
