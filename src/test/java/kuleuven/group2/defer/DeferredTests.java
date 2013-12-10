package kuleuven.group2.defer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		DeferredConsumerTest.class,
		DeferredTaskRunnerTest.class
})
public class DeferredTests {

}
