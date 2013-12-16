package kuleuven.group2.policy;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class CompositeTestSortingPolicyTest {

	@Test
	public void addLastPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addLastPolicy(p1);
		rrp.addLastPolicy(p2);
		rrp.addLastPolicy(p3);
		assertTrue(rrp.getWeightedPolicyAt(0).getTestSortingPolicy() == p1);
		assertTrue(rrp.getWeightedPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedPolicyAt(2).getTestSortingPolicy() == p3);
		assertEquals(rrp.getWeightedPolicyAt(0).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(1).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(2).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}
	
	@Test
	public void addLastWeightedPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		rrp.addLastWeightedPolicy(wp1);
		rrp.addLastWeightedPolicy(wp2);
		rrp.addLastWeightedPolicy(wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedPolicyAt(2) == wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}
	
	@Test
	public void addFirstPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addFirstPolicy(p1);
		rrp.addFirstPolicy(p2);
		rrp.addFirstPolicy(p3);
		assertTrue(rrp.getWeightedPolicyAt(0).getTestSortingPolicy() == p3);
		assertTrue(rrp.getWeightedPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedPolicyAt(2).getTestSortingPolicy() == p1);
		assertEquals(rrp.getWeightedPolicyAt(0).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(1).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(2).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}
	
	@Test
	public void addFirstWeightedPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		rrp.addFirstWeightedPolicy(wp1);
		rrp.addFirstWeightedPolicy(wp2);
		rrp.addFirstWeightedPolicy(wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp3);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedPolicyAt(2) == wp1);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}
	
	@Test
	public void addPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addPolicyAt(0, p1);
		rrp.addPolicyAt(0, p2);
		rrp.addPolicyAt(0, p3);
		assertTrue(rrp.getWeightedPolicyAt(0).getTestSortingPolicy() == p3);
		assertTrue(rrp.getWeightedPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedPolicyAt(2).getTestSortingPolicy() == p1);
		assertEquals(rrp.getWeightedPolicyAt(0).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(1).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedPolicyAt(2).getWeight(), WeightedPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}
	
	@Test
	public void addWeightedPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		rrp.addWeightedPolicyAt(0, wp1);
		rrp.addWeightedPolicyAt(0, wp2);
		rrp.addWeightedPolicyAt(0, wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp3);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedPolicyAt(2) == wp1);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
	}

	@Test
	public void setWeightedPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		rrp.addLastWeightedPolicy(wp1);
		rrp.setWeightedPolicyAt(0, wp2);
		rrp.setWeightedPolicyAt(0, wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 1);
	}
	
	@Test
	public void removeWeightedPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		rrp.addLastWeightedPolicy(wp1);
		rrp.addLastWeightedPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 2);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		rrp.removeWeightedPolicy(wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 2);
		rrp.removeWeightedPolicy(wp1);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 1);
		rrp.removeWeightedPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
	}
	
	@Test
	public void removeWeightedPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		rrp.addLastWeightedPolicy(wp1);
		rrp.addLastWeightedPolicy(wp2);
		rrp.addLastWeightedPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedPolicyAt(2) == wp3);
		rrp.removeWeightedPolicyAt(1);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 2);
		rrp.removeWeightedPolicyAt(0);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 1);
		rrp.removeWeightedPolicyAt(0);
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
	}
	
	@Test
	public void contains_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		assertFalse(rrp.contains(wp1));
		assertFalse(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
		rrp.addLastWeightedPolicy(wp1);
		assertTrue(rrp.contains(wp1));
		assertFalse(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
		rrp.addLastWeightedPolicy(wp2);
		assertTrue(rrp.contains(wp1));
		assertTrue(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
		rrp.addLastWeightedPolicy(wp3);
		assertTrue(rrp.contains(wp1));
		assertTrue(rrp.contains(wp2));
		assertTrue(rrp.contains(wp3));
		rrp.removeWeightedPolicy(wp3);
		assertTrue(rrp.contains(wp1));
		assertTrue(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
		rrp.removeWeightedPolicy(wp2);
		assertTrue(rrp.contains(wp1));
		assertFalse(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
		rrp.removeWeightedPolicy(wp1);
		assertFalse(rrp.contains(wp1));
		assertFalse(rrp.contains(wp2));
		assertFalse(rrp.contains(wp3));
	}
	
	@Test
	public void getWeightedPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		rrp.addLastWeightedPolicy(wp1);
		rrp.addLastWeightedPolicy(wp2);
		rrp.addLastWeightedPolicy(wp3);
		assertTrue(rrp.getWeightedPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedPolicyAt(2) == wp3);
	}
	
	@Test
	public void getNbOfWeightedPolicies_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		
		rrp.addLastWeightedPolicy(wp1);
		assertEquals(rrp.getNbOfWeightedPolicies(), 1);
		rrp.addLastWeightedPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 2);
		rrp.addLastWeightedPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 3);
		rrp.removeWeightedPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedPolicies(), 2);
		rrp.removeWeightedPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedPolicies(), 1);
		rrp.removeWeightedPolicy(wp1);
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
	}
	
	@Test
	public void getWeightedPolicies_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedPolicy wp1 = new WeightedPolicy(p1);
		WeightedPolicy wp2 = new WeightedPolicy(p2);
		WeightedPolicy wp3 = new WeightedPolicy(p3);
		rrp.addLastWeightedPolicy(wp1);
		rrp.addLastWeightedPolicy(wp2);
		rrp.addLastWeightedPolicy(wp3);
		List<WeightedPolicy> result = rrp.getWeightedPolicies();
		assertTrue(result.get(0) == wp1);
		assertTrue(result.get(1) == wp2);
		assertTrue(result.get(2) == wp3);
	}

}
