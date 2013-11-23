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
	
	protected DeferredConsumer<Float> deferredConsumer;
	protected List<Float> floatList;
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
		floatList = new ArrayList<>();
		deferredConsumer = new DeferredConsumer<>(testConsumer, 200, TimeUnit.MILLISECONDS);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public class TestConsumer implements Consumer<List<Float>> {

		@Override
		public void consume(List<Float> item) {
			DeferredConsumerTest.this.floatList.addAll(item);
		}
		
	}

	@Test
	public void test() {
		deferredConsumer.consume(0.0f);
		floatList.contains(new Float(0.0f));
	}

}
