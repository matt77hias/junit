package kuleuven.group2.defer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kuleuven.group2.util.Consumer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeferredConsumerTest {
	
	protected DeferredConsumer<Integer> deferredConsumer;
	protected List<Integer> intList;
	protected TestConsumer testConsumer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testConsumer = new TestConsumer();
		intList = new ArrayList<>();
		deferredConsumer = new DeferredConsumer<>(testConsumer, 50, TimeUnit.MILLISECONDS);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public class TestConsumer implements Consumer<List<Integer>> {

		@Override
		public void consume(List<Integer> item) {
			DeferredConsumerTest.this.intList.addAll(item);
		}
		
	}

	@Test
	public void testSingleConsume() throws InterruptedException {
		deferredConsumer.consume(0);
		// integer is not consumed instantly
		assertFalse(intList.contains(0));
		
		Thread.sleep(60);
		
		// integer is consumed after 50 ms
		assertTrue(intList.contains(0));
	}

	@Test
	public void testMultipleConsume() throws InterruptedException {
		deferredConsumer.consume(0);
		deferredConsumer.consume(1);
		deferredConsumer.consume(2);
		deferredConsumer.consume(3);
		
		Thread.sleep(60);
		
		// integers are consumed after 50 ms
		assertTrue(intList.contains(0));
		assertTrue(intList.contains(1));
		assertTrue(intList.contains(2));
		assertTrue(intList.contains(3));
	}

}
