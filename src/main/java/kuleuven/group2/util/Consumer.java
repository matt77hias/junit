package kuleuven.group2.util;

/**
 * An interface implemented by classes that consume objects of type T.
 * 
 * @param <T>
 * 
 * @author Group2
 * @version 11 November 2013
 */
public interface Consumer<T> extends UnsafeConsumer<T>{

	public void consume(T item);

}
