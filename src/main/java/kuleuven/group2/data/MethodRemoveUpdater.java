package kuleuven.group2.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MethodRemoveUpdater {

	protected final TestDatabase database;

	public MethodRemoveUpdater(TestDatabase database) {
		this.database = checkNotNull(database);
	}

	/**
	 * Remove all methods from the given classes in the database.
	 * 
	 * @param removedClassNames
	 *            The names of removed classes.
	 */
	public void removeClasses(Collection<String> removedClassNames) {
		for (String className : removedClassNames) {
			// TODO
			// Set<TestedMethod> classMethods = database.getMethodsIn(className);
			Set<TestedMethod> classMethods = new HashSet<>();
			for (TestedMethod method : classMethods) {
				database.removeMethod(method);
			}
		}
	}

}
