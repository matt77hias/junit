package kuleuven.group2.data;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;
import kuleuven.group2.methodchange.MethodHash;

/**
 * A class that represents a method that is used by at least one test. It also
 * keeps the time of its latest change.
 * 
 * @author Ruben Pieters, Vital D'haveloose
 */
public class TestedMethod {

	protected final JavaSignature signature;
	protected Collection<Test> tests = new HashSet<Test>(); // TODO: concurrency?
	protected MethodHash hash;
	protected Date lastChanged;

	public TestedMethod(String signature) {
		this(new JavaSignatureParser(signature).parseSignature());
	}

	public TestedMethod(JavaSignature signature) {
		this.signature = signature;
	}

	public JavaSignature getSignature() {
		return signature;
	}

	public MethodHash getHash() {
		return hash;
	}

	public void setHash(MethodHash hash) {
		this.hash = hash;
	}

	public Date getLastChange() {
		return lastChanged;
	}

	public void setLastChange(Date time) {
		this.lastChanged = time;
	}

	public void addTest(Test test) {
		tests.add(test);
	}

	public void removeTest(Test test) {
		tests.remove(test);
	}

	public boolean containsTest(Test test) {
		return tests.contains(test);
	}

	public Collection<Test> getTests() {
		return new HashSet<Test>(tests);
		// TODO: nodig? origineel doorgeven in de plaats?
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TestedMethod other = (TestedMethod) obj;
		if (signature == null) {
			if (other.signature != null) return false;
		} else if (!signature.equals(other.signature)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestedMethod [signature=" + getSignature() + ", lastChange=" + getLastChange() + ", tests="
				+ getTests() + "]";
	}

}
