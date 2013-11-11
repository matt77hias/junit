package kuleuven.group2.methodchange;

import java.util.Arrays;

public class MethodHash {

	private final byte[] hash;

	public MethodHash(byte[] hash) {
		this.hash = hash;
	}

	protected byte[] getHash() {
		return hash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(hash);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MethodHash other = (MethodHash) obj;
		if (!Arrays.equals(hash, other.hash)) return false;
		return true;
	}

}
