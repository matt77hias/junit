package kuleuven.group2.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * A class that represents a method that is used by at least one test. It also
 * keeps the time of its latest change.
 * 
 * @author Ruben Pieters, Vital D'haveloose
 */
public class TestedMethod {

	protected String name;
	protected String className;
	protected List<String> argumentTypes = new ArrayList<String>();
	protected String returnType;
	
	protected Collection<Test> tests = new HashSet<Test>(); //TODO: concurrency?
	
	protected Date lastChangeTime;
	
	public TestedMethod(String name, String className, List<String> arguments,
			String returnType) {
		super();
		this.name = name;
		this.className = className;
		this.argumentTypes = arguments;
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public List<String> getArguments() {
		return argumentTypes;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getSignature() {
		// TODO invullen, hoe ziet die signatuur er precies uit?
		return "";
	}
	
	public Date getLastChange() {
		return lastChangeTime;
	}

	public void setLastChange(Date time) {
		this.lastChangeTime = time;
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
		//TODO: nodig? origineel doorgeven in de plaats?
	}

	@Override
	public int hashCode() {
		final int prime= 31;
		int result= 1;
		result= prime * result
				+ ((argumentTypes == null) ? 0 : argumentTypes.hashCode());
		result= prime * result + ((name == null) ? 0 : name.hashCode());
		result= prime * result
				+ ((className == null) ? 0 : className.hashCode());
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
		TestedMethod other= (TestedMethod) obj;
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
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
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
		return "Method [name=" + name + ", className=" + className
				+ ", argumentTypes=" + argumentTypes + ", returnType="
				+ returnType + "]";
	}
	
	
	
}
