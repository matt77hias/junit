package kuleuven.group2.defer;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeferredTaskRunnerTest {
	
	protected boolean taskDone = false;
	
	protected DeferredTaskRunner deferredTaskRunner200msDelay;
	
	protected Runnable task100ms = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DeferredTaskRunnerTest.this.taskDone = true;
		}
	};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		taskDone = false;
		deferredTaskRunner200msDelay = new DeferredTaskRunner(task100ms, 200, TimeUnit.MILLISECONDS);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test100msTaskNormalDefer() throws InterruptedException {
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(150);
		
		// task should still be delayed for 50ms
		assertFalse(taskDone);
		
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(150);

		// task should still be delayed for 50ms
		assertFalse(taskDone);
		
		Thread.sleep(200);
		
		// task should be done after 200 + 100 ms
		assertTrue(taskDone);
	}

	@Test
	public void test100msTaskRequestDuringExecution() throws InterruptedException {
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(210);

		// task should be running after 200 ms
		assertTrue(deferredTaskRunner200msDelay.isRunning());
		
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(110);
		
		// task should be done after 200 + 100 ms, even though start was requested
		assertTrue(taskDone);
	}
	
	@Test
	public void test100msTaskStoppedDuringExecution() throws InterruptedException {
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(210);

		// task should be running after 200 ms
		assertTrue(deferredTaskRunner200msDelay.isRunning());
		
		deferredTaskRunner200msDelay.stop();
		
		assertFalse(taskDone);
		
		Thread.sleep(110);
		
		// task should be not be done, because a stop was requested
		assertFalse(taskDone);
	}
	
}
