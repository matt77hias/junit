package kuleuven.group2.store;

/**
 * A filter that separates source code (.java) from binaries (.class).
 * 
 * @author Group2
 * @version 11 November 2013
 */
public interface StoreFilter {

	public boolean accept(String resourceName);

	public static final StoreFilter ALL = new StoreFilter() {
		@Override
		public boolean accept(String resourceName) {
			return true;
		}
	};

	public static final StoreFilter NONE = new StoreFilter() {
		@Override
		public boolean accept(String resourceName) {
			return false;
		}
	};

	public static final StoreFilter SOURCE = new StoreFilter() {
		@Override
		public boolean accept(String resourceName) {
			return resourceName.toLowerCase().endsWith(".java");
		}
	};

	public static final StoreFilter CLASS = new StoreFilter() {
		@Override
		public boolean accept(String resourceName) {
			return resourceName.toLowerCase().endsWith(".class");
		}
	};

}
