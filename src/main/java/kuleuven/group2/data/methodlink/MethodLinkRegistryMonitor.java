package kuleuven.group2.data.methodlink;

import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.Test;
import kuleuven.group2.data.updating.ICurrentRunningTestHolder;
import kuleuven.group2.signature.JavaSignatureParser;
import be.kuleuven.cs.ossrewriter.Monitor;

public class MethodLinkRegistryMonitor extends Monitor {
	
	protected MethodLinkRegistry methodLinkRegistry;
	protected ICurrentRunningTestHolder currentRunningTestHolder;

	public MethodLinkRegistryMonitor(MethodLinkRegistry methodLinkRegistry, ICurrentRunningTestHolder currentRunningTestHolder) {
		super();
		this.methodLinkRegistry = methodLinkRegistry;
		this.currentRunningTestHolder = currentRunningTestHolder;
	}

	@Override
	public void enterMethod(String methodName) {
		Test currentRunningTest = currentRunningTestHolder.getCurrentRunningTest();
		TestedMethod enteredMethod = new JavaSignatureParser(methodName)
			.parseSignature();
		methodLinkRegistry.addLink(enteredMethod, currentRunningTest);
	};
	
}
