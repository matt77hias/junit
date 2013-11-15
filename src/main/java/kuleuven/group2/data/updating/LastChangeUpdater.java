package kuleuven.group2.data.updating;

import java.util.Date;

import kuleuven.group2.data.TestDatabase;

/**
 * Updates the data when a method is changed. For this, the class implements a StoreListener.
 * @author vital.dhaveloose
 *
 */
public class LastChangeUpdater implements MethodChangeListener {
	
	protected TestDatabase database;
	
	public LastChangeUpdater(TestDatabase database) {
		this.database = database;
	}

	@Override
	public void changed(String signature, Date time) {
		// TODO implement
	}

}
