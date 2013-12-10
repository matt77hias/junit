package kuleuven.group2.util;

/**
 * An consumer of items which may throw exceptions while consuming.
 * 
 * @param <T>
 * 
 * @author Group2
 * @version 10 December 2013
 */
public interface UnsafeConsumer<T> {

	public void consume(T item) throws Exception;

}
