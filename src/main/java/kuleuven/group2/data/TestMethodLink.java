package kuleuven.group2.data;

public class TestMethodLink {
	
	protected TestedMethod testedMethod;
	protected Test test;
	
	public TestMethodLink(TestedMethod testedMethod, Test test) {
		super();
		this.testedMethod = testedMethod;
		this.test = test;
	}

	public TestedMethod getTestedMethod() {
		return testedMethod;
	}

	public Test getTest() {
		return test;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((test == null) ? 0 : test.hashCode());
		result = prime * result + ((testedMethod == null) ? 0 : testedMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TestMethodLink other = (TestMethodLink) obj;
		if (test == null) {
			if (other.test != null) return false;
		} else if (!test.equals(other.test)) return false;
		if (testedMethod == null) {
			if (other.testedMethod != null) return false;
		} else if (!testedMethod.equals(other.testedMethod)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestMethodLink [testedMethod=" + testedMethod + ", test=" + test + "]";
	}
	
	

}
