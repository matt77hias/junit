package kuleuven.group2.signatureparser;

public abstract class JavaSignatureParserComponent<T> {
	
	protected String signature;
	
	public JavaSignatureParserComponent(String signature) {
		this.signature = signature;
	}
	
	public abstract T parseComponent();
}
