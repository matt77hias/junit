package kuleuven.group2.signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaSignatureArgumentParser extends JavaSignatureParserComponent<List<String>> {

	public JavaSignatureArgumentParser(String signature) {
		super(signature);
	}

	@Override
	public List<String> parseComponent() {
		int indexStartingBracket = signature.indexOf("(") + 1;
		int indexEndingBracket = signature.indexOf(")");
		String filteredSignature = signature.substring(indexStartingBracket, indexEndingBracket);

		String[] arguments = filteredSignature.split(";");
		List<String> argumentList =  new ArrayList<String>(Arrays.asList(arguments));
		if (argumentList.contains("")) {
			argumentList.remove("");
		}
		
		return argumentList;
	}

}
