package kuleuven.group2.store;

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
