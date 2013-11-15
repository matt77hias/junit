package kuleuven.group2.data.signature;

public abstract class JavaSignatureParserComponent<T> {
	
	protected String signature;
	
	public JavaSignatureParserComponent(String signature) {
		this.signature = signature;
	}
	
	public abstract T parseComponent();
}
