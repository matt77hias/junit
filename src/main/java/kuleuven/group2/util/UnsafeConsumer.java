package kuleuven.group2.util;

/**
 * An interface implemented by classes that consume objects of type T, but
 * an throw exceptions while doing that.
 * 
 * @param <T>
 * 
 * @author Group2
 * @version 10 December 2013
 */
public interface UnsafeConsumer<T> {

	public void consume(T item) throws Exception;
	
}
