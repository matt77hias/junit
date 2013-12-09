package kuleuven.group2.data.updating;

import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.Store;

import com.google.common.base.Predicate;

/**
 * A transformation filter which only allows classes inside the given binary
 * store to be transformed.
 * 
 * @author Group2
 * @version 9 December 2013
 */
public class BinaryStoreTransformFilter implements Predicate<String> {

	protected final Store binaryStore;

	public BinaryStoreTransformFilter(Store binaryStore) {
		this.binaryStore = binaryStore;
	}

	@Override
	public boolean apply(String className) {
		return binaryStore.contains(NameUtils.toBinaryName(className));
	}

}
