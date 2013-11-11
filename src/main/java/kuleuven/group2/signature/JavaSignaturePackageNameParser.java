package kuleuven.group2.signature;

public class JavaSignaturePackageNameParser extends JavaSignatureStringParser {

	public JavaSignaturePackageNameParser(String signature) {
		super(signature);
	}

	@Override
	public String getComponentRegex() {
		return "((.*)/)+";
	}

	@Override
	public String getRegexFilter() {
		return "\\.(.*)";
	}
}
