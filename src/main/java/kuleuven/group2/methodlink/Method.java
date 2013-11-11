package kuleuven.group2.methodlink;

import java.util.ArrayList;
import java.util.List;

public class Method {

	protected String name;
	protected String packageName;
	protected List<String> argumentTypes = new ArrayList<String>();
	protected String returnType;
	
	public Method(String name, String packageName, List<String> arguments,
			String returnType) {
		super();
		this.name = name;
		this.packageName = packageName;
		this.argumentTypes = arguments;
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public String getPackageName() {
		return packageName;
	}

	public List<String> getArguments() {
		return argumentTypes;
	}

	public String getReturnType() {
		return returnType;
	}

	@Override
	public int hashCode() {
		final int prime= 31;
		int result= 1;
		result= prime * result
				+ ((argumentTypes == null) ? 0 : argumentTypes.hashCode());
		result= prime * result + ((name == null) ? 0 : name.hashCode());
		result= prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result= prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other= (Method) obj;
		if (argumentTypes == null) {
			if (other.argumentTypes != null)
				return false;
		} else if (!argumentTypes.equals(other.argumentTypes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Method [name=" + name + ", packageName=" + packageName
				+ ", argumentTypes=" + argumentTypes + ", returnType="
				+ returnType + "]";
	}
	
	
	
}
