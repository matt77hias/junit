package kuleuven.group2.data.signature;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class JavaSignature {

	protected final String name;
	protected final String className;
	protected final String packageName;
	protected final List<String> argumentTypes;
	protected final String returnType;

	public JavaSignature(String name, String className, String packageName, List<String> arguments, String returnType) {
		this.name = name;
		this.className = className;
		this.packageName = packageName;
		this.argumentTypes = ImmutableList.copyOf(arguments);
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getFullClassName() {
		if (Strings.isNullOrEmpty(getPackageName())) {
			// Default package
			return getClassName();
		} else {
			return getPackageName() + "." + getClassName();
		}
	}

	public List<String> getArguments() {
		return argumentTypes;
	}

	public String getReturnType() {
		return returnType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argumentTypes == null) ? 0 : argumentTypes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		JavaSignature other = (JavaSignature) obj;
		if (argumentTypes == null) {
			if (other.argumentTypes != null) return false;
		} else if (!argumentTypes.equals(other.argumentTypes)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (className == null) {
			if (other.className != null) return false;
		} else if (!className.equals(other.className)) return false;
		if (packageName == null) {
			if (other.packageName != null) return false;
		} else if (!packageName.equals(other.packageName)) return false;
		if (returnType == null) {
			if (other.returnType != null) return false;
		} else if (!returnType.equals(other.returnType)) return false;
		return true;
	}

	@Override
	public String toString() {
		return getFullClassName() + "." + getName() + "(" + Joiner.on(", ").join(getArguments()) + ")"
				+ getReturnType();
	}

}
