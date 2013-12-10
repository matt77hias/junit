package kuleuven.group2.util;

/**
 * A consumer of items.
 * 
 * @param <T>
 * 
 * @author Group2
 * @version 11 November 2013
 */
public interface Consumer<T> extends UnsafeConsumer<T> {

	@Override
	public void consume(T item);

}
