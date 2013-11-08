package kuleuven.group2.store;

public interface Store {

	public boolean contains(String resourceName);

	public byte[] read(String resourceName);

	public void write(String resourceName, byte[] contents);

}
