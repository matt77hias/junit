package kuleuven.group2.defer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import kuleuven.group2.util.Consumer;

public class DeferredConsumer<T> implements Consumer<T> {

	protected final DeferredTaskRunner runner;
	protected final Deque<T> queue = new ConcurrentLinkedDeque<T>();

	public DeferredConsumer(Consumer<? super List<T>> batchConsumer) throws IllegalArgumentException {
		checkNotNull(batchConsumer);
		this.runner = new DeferredTaskRunner(createRunnable(batchConsumer));
	}

	public DeferredConsumer(Consumer<? super List<T>> batchConsumer, long delay, TimeUnit timeUnit)
			throws IllegalArgumentException {
		checkNotNull(batchConsumer);
		this.runner = new DeferredTaskRunner(createRunnable(batchConsumer), delay, timeUnit);
	}

	/**
	 * Create a runnable for the given batch consumer.
	 * 
	 * <p>
	 * Implementation note: This method is called in the
	 * {@link DeferredConsumer} constructor. As such, fields of subclasses may
	 * not be initialized yet. It is recommended to treat this method as if it
	 * were static.
	 * </p>
	 * 
	 * @param batchConsumer
	 * @return
	 */
	protected Runnable createRunnable(Consumer<? super List<T>> batchConsumer) {
		return new BatchRunnable(batchConsumer);
	}

	@Override
	public void consume(T item) {
		queue.addLast(item);
		runner.start();
	}

	public void stop() {
		runner.stop();
	}

	public void stopService() {
		runner.stopService();
	}

	public class BatchRunnable implements Runnable {

		protected final Consumer<? super List<T>> batchConsumer;

		public BatchRunnable(Consumer<? super List<T>> batchConsumer) {
			this.batchConsumer = batchConsumer;
		}

		@Override
		public void run() {
			// Collect items
			List<T> items = new LinkedList<T>();
			for (Iterator<T> it = queue.iterator(); it.hasNext();) {
				T item = it.next();
				items.add(item);
				it.remove();
			}
			if (!items.isEmpty()) {
				try {
					// Call batch consumer
					batchConsumer.consume(items);
				} catch (Throwable e) {
					// Consumer failed, re-insert
					queue.addAll(items);
					throw e;
				}
			}
		}

	}

}
