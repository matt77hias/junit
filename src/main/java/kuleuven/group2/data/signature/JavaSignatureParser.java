package kuleuven.group2.data.signature;

import java.util.List;

public class JavaSignatureParser {

	protected final String signature;

	public JavaSignatureParser(String signature) {
		this.signature = signature;
	}

	/*
	 * TODO Parse whole signature with one regular expression
	 * using capture groups?
	 */
	public JavaSignature parseSignature() {
		String name = parseMethodName();
		String className = parseClassName();
		String packageName = parsePackageName();
		List<String> arguments = parseArguments();
		String returnType = parseReturnType();

		return new JavaSignature(name, className, packageName, arguments, returnType);
	}

	protected String parsedClassName = null;

	public String parseClassName() {
		if (parsedClassName == null)
			parsedClassName = null; // TODO
		return parsedClassName;
	}

	protected String parsedPackageName = null;

	public String parsePackageName() {
		if (parsedPackageName == null)
			parsedPackageName = new JavaSignaturePackageNameParser(signature).parseComponent();
		return parsedPackageName;
	}

	protected String parsedMethodName = null;

	public String parseMethodName() {
		if (parsedMethodName == null)
			parsedMethodName = new JavaSignatureMethodNameParser(signature, parsePackageName()).parseComponent();
		return parsedMethodName;
	}

	protected String parsedReturnType = null;

	public String parseReturnType() {
		if (parsedReturnType == null) parsedReturnType = new JavaSignatureReturnTypeParser(signature).parseComponent();
		return parsedReturnType;
	}

	protected List<String> parsedArguments = null;

	public List<String> parseArguments() {
		if (parsedArguments == null) parsedArguments = new JavaSignatureArgumentParser(signature).parseComponent();
		return parsedArguments;
	}

}
