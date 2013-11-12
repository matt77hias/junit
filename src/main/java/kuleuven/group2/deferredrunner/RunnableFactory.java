package kuleuven.group2.deferredrunner;

/**
 * An interface for runnable factories. Runnable factories
 * provide a method for creating new runnable objects.
 * 
 * @author	Group 2
 * @version 9 November 2013
 *
 */
public interface RunnableFactory {
	
	/**
	 * Creates a new runnable.
	 * 
	 * @return	The new runnable.
	 */
	public Runnable createRunnable();
}
