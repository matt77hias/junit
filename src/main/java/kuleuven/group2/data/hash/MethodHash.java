package kuleuven.group2.data.hash;

import java.util.Arrays;

/**
 * A hash code for a method.
 * 
 * @author Group2
 * @version 6 November 2013
 */
public class MethodHash {

	private final byte[] hash;

	public MethodHash(byte[] hash) {
		this.hash = hash;
	}

	public byte[] getHash() {
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
