package kuleuven.group2.data.updating.signature;

public class JavaSignatureReturnTypeParser extends JavaSignatureStringParser {

	public JavaSignatureReturnTypeParser(String signature) {
		super(signature);
	}

	@Override
	public String getComponentRegex() {
		return "(.*)";
	}

	@Override
	public String getRegexFilter() {
		return "(.*)\\)";
	}

}
