package kuleuven.group2.policy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		DistinctFailureFirstTest.class,
		FrequentFailureTest.class,
		LastFailureFirstTest.class,
		FixedOrderPolicyTest.class,
		RoundRobinPolicyTest.class,
		CompositePolicyTest.class
})
public class PolicyTests {

}
