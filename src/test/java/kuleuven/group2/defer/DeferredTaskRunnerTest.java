package kuleuven.group2.defer;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeferredTaskRunnerTest {
	
	protected DeferredTaskRunner deferredTaskRunner200msDelay;
	protected boolean taskDone = false;
	protected int taskDoneCounter = 0;
	protected Runnable task100ms = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DeferredTaskRunnerTest.this.taskDone = true;
			DeferredTaskRunnerTest.this.taskDoneCounter++;
		}
	};
	
	protected Runnable longNap = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(50000000);
			} catch (InterruptedException e) {
				
			}
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
		taskDoneCounter = 0;
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
		
		Thread.sleep(220);

		// task should be running after 200 ms
		assertTrue(deferredTaskRunner200msDelay.isRunning());
		
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(120);
		
		// task should be done after 200 + 100 ms, even though start was requested
		assertTrue(taskDone);
	}
	
	@Test
	public void test100msTaskStoppedDuringExecution() throws InterruptedException {
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(220);

		// task should be running after 200 ms
		assertTrue(deferredTaskRunner200msDelay.isRunning());
		
		deferredTaskRunner200msDelay.stop();
		
		assertFalse(taskDone);
		
		Thread.sleep(120);
		
		// task should finish, stop does not abort a currently running task
		assertTrue(taskDone);
	}
	
	@Test
	public void test100msTaskRequestedDuringExecution() throws InterruptedException {
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(220);

		// task should be running after 200 ms
		assertTrue(deferredTaskRunner200msDelay.isRunning());
		
		deferredTaskRunner200msDelay.start();
		
		Thread.sleep(120);
		
		// busy with deferring second requested task
		assertFalse(deferredTaskRunner200msDelay.isRunning());
		
		Thread.sleep(320);
		
		// task has been executed twice
		assertEquals(2, taskDoneCounter);
	}
	
	@Test
	public void test100msStopServiceDuringExecution() throws InterruptedException {
		DeferredTaskRunner infiniteLoopTaskRunner = new DeferredTaskRunner(longNap, 20, TimeUnit.MILLISECONDS);
		
		infiniteLoopTaskRunner.start();
		
		Thread.sleep(30);

		// task should be running after 20 ms
		assertTrue(infiniteLoopTaskRunner.isRunning());
		
		infiniteLoopTaskRunner.stopService();
		
		Thread.sleep(10);
		
		// long running task responding to interrupts has been stopped
		assertFalse(infiniteLoopTaskRunner.isRunning());
	}
	
}
