package kuleuven.group2.policy;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;


public class RoundRobinTestSortingPolicyTest extends TestSortingPolicyTest {

	@Override
	public void correct_order_test() {
	}
	
	@Test
	public void correct_order_test_weight_readdedFixed() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastWeightedPolicy(new WeightedPolicy(policy, 1));
		rrp.addLastPolicy(fixed1);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test1);
		assertTrue(result.get(2) == test2);
		assertTrue(result.get(3) == test4);
	}
	
	@Test
	public void correct_order_test_weight1() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastWeightedPolicy(new WeightedPolicy(policy, 1));
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test1);
		assertTrue(result.get(2) == test2);
		assertTrue(result.get(3) == test4);
	}
	
	@Test
	public void correct_order_test_readded2() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test4, test2, test3, test1);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastPolicy(policy);
		rrp.addLastPolicy(policy);
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test2);
		assertTrue(result.get(2) == test4);
		assertTrue(result.get(3) == test1);
	}
	
	@Test
	public void correct_order_test_weight2() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test4, test2, test3, test1);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastWeightedPolicy(new WeightedPolicy(policy, 2));
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test2);
		assertTrue(result.get(2) == test4);
		assertTrue(result.get(3) == test1);
	}
	
	@Test
	public void correct_order_test_readded3() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test4, test2, test3, test1);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastPolicy(policy);
		rrp.addLastPolicy(policy);
		rrp.addLastPolicy(policy);
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test2);
		assertTrue(result.get(2) == test1);
		assertTrue(result.get(3) == test4);
	}
	
	@Test
	public void correct_order_test_weight3() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test4, test2, test3, test1);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastWeightedPolicy(new WeightedPolicy(policy, 3));
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test2);
		assertTrue(result.get(2) == test1);
		assertTrue(result.get(3) == test4);
	}
	
	@Test
	public void correct_order_test_selfDirectContaining() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);
		
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp_high = new RoundRobinTestSortingPolicy();
		rrp_high.addLastPolicy(fixed1);
		rrp_high.addLastPolicy(rrp_high);
		
		rrp_high.getSortedTests(super.testDatabase);
	}
	
	@Test
	public void correct_order_test_selfIndirectContaining() {
		List<kuleuven.group2.data.Test> list1 = ImmutableList.of(test1, test2, test3, test4);
		List<kuleuven.group2.data.Test> list2 = ImmutableList.of(test2, test3, test4, test1);
		
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list1);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list2);
		
		RoundRobinTestSortingPolicy rrp_high = new RoundRobinTestSortingPolicy();
		RoundRobinTestSortingPolicy rrp_low = new RoundRobinTestSortingPolicy();
		rrp_high.addLastPolicy(fixed1);
		rrp_low.addLastPolicy(fixed2);
		
		rrp_high.addLastPolicy(rrp_low);
		rrp_low.addLastPolicy(rrp_high);
		
		rrp_high.getSortedTests(super.testDatabase);
	}
	
	@Test
	public void correct_order_test_multipleLevels() {
		List<kuleuven.group2.data.Test> list1 = ImmutableList.of(test1, test2, test3, test4);
		List<kuleuven.group2.data.Test> list2 = ImmutableList.of(test2, test3, test4, test1);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list1);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list2);
		
		RoundRobinTestSortingPolicy rrp_low = new RoundRobinTestSortingPolicy();
		rrp_low.addLastPolicy(fixed1);
		rrp_low.addLastPolicy(fixed2);
		
		RoundRobinTestSortingPolicy rrp_high = new RoundRobinTestSortingPolicy();
		rrp_high.addLastPolicy(policy);
		rrp_high.addLastPolicy(rrp_low);
		
		List<kuleuven.group2.data.Test> result =  rrp_high.getSortedTests(super.testDatabase);
		assertTrue(result.get(0) == test3);
		assertTrue(result.get(1) == test1);
		assertTrue(result.get(2) == test2);
		assertTrue(result.get(3) == test4);
	}
	
	@Test
	public void correct_order_test_noWeightedPolicies() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		List<kuleuven.group2.data.Test> result =  rrp.getSortedTests(super.testDatabase, list);
		assertTrue(result.get(0) == test1);
		assertTrue(result.get(1) == test2);
		assertTrue(result.get(2) == test3);
		assertTrue(result.get(3) == test4);
	}

	@Override @Test
	public void immutable_input_test() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);
		
		TestSortingPolicy policy = new LastFailureFirst();
		FixedOrderTestSortingPolicy fixed1 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed2 = new FixedOrderTestSortingPolicy(list);
		FixedOrderTestSortingPolicy fixed3 = new FixedOrderTestSortingPolicy(list);
		
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		rrp.addLastWeightedPolicy(new WeightedPolicy(policy, 1));
		rrp.addLastPolicy(fixed1);
		rrp.addLastPolicy(fixed2);
		rrp.addLastPolicy(fixed3);
		
		List<kuleuven.group2.data.Test> input = new ArrayList<kuleuven.group2.data.Test>();
		input.add(test1);
		input.add(test2);
		input.add(test3);
		input.add(test4);
		rrp.getSortedTests(testDatabase, input);
		assertTrue(input.get(0) == test1);
		assertTrue(input.get(1) == test2);
		assertTrue(input.get(2) == test3);
		assertTrue(input.get(3) == test4);
	}

}
