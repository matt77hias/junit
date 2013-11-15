package kuleuven.group2.data.signature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JavaSignatureStringParser extends JavaSignatureParserComponent<String> {

	public JavaSignatureStringParser(String signature) {
		super(signature);
	}
	
	public abstract String getRegexFilter();

	public abstract String getComponentRegex();
	
	public Pattern getComponentPattern() {
		return Pattern.compile(getComponentRegex());
	}
	
	public String filterSignature() {
		if (getRegexFilter().isEmpty()) {
			return signature;
		}
		return signature.replaceFirst(getRegexFilter(), "");
	}
	
	@Override
	public String parseComponent() {
		String filteredSignature = filterSignature();
		Matcher matcher = getComponentPattern().matcher(filteredSignature);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

}
