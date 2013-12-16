package kuleuven.group2.policy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		DistinctFailureFirstTest.class,
		FrequentFailureTest.class,
		LastFailureFirstTest.class,
		FixedOrderTestSortingPolicyTest.class,
		RoundRobinTestSortingPolicyTest.class,
		CompositeTestSortingPolicyTest.class
})
public class PolicyTests {

}
