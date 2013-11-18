package kuleuven.group2.data.updating;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;

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
			Collection<TestedMethod> classMethods = database.getMethodsIn(className);
			for (TestedMethod method : classMethods) {
				database.removeMethod(method);
			}
		}
	}

}
