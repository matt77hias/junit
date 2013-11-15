package kuleuven.group2.data.signature;

public class JavaSignatureMethodNameParser extends JavaSignatureStringParser {
	
	protected String packageName;

	public JavaSignatureMethodNameParser(String signature, String packageName) {
		super(signature);
		this.packageName = packageName;
	}

	@Override
	public String getComponentRegex() {
		return "[A-Z0-9a-z_.$<>]+";
	}
	
	@Override
	public String getRegexFilter() {
		return packageName;
	}

}
