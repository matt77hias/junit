package kuleuven.group2.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


public class CompositeTestSortingPolicyTest {

	@Test
	public void addLastPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addLastTestSortingPolicy(p1);
		rrp.addLastTestSortingPolicy(p2);
		rrp.addLastTestSortingPolicy(p3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0).getTestSortingPolicy() == p1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2).getTestSortingPolicy() == p3);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(0).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(1).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(2).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}
	
	@Test
	public void addLastWeightedTestSortingPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		rrp.addLastWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2) == wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}
	
	@Test
	public void addFirstPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addFirstTestSortingPolicy(p1);
		rrp.addFirstTestSortingPolicy(p2);
		rrp.addFirstTestSortingPolicy(p3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0).getTestSortingPolicy() == p3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2).getTestSortingPolicy() == p1);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(0).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(1).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(2).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}
	
	@Test
	public void addFirstWeightedTestSortingPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		rrp.addFirstWeightedTestSortingPolicy(wp1);
		rrp.addFirstWeightedTestSortingPolicy(wp2);
		rrp.addFirstWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2) == wp1);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}
	
	@Test
	public void addPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		rrp.addTestSortingPolicyAt(0, p1);
		rrp.addTestSortingPolicyAt(0, p2);
		rrp.addTestSortingPolicyAt(0, p3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0).getTestSortingPolicy() == p3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1).getTestSortingPolicy() == p2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2).getTestSortingPolicy() == p1);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(0).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(1).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getWeightedTestSortingPolicyAt(2).getWeight(), WeightedTestSortingPolicy.DEFAULT_WEIGHT);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}
	
	@Test
	public void addWeightedTestSortingPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		rrp.addWeightedTestSortingPolicyAt(0, wp1);
		rrp.addWeightedTestSortingPolicyAt(0, wp2);
		rrp.addWeightedTestSortingPolicyAt(0, wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2) == wp1);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
	}

	@Test
	public void setWeightedTestSortingPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.setWeightedTestSortingPolicyAt(0, wp2);
		rrp.setWeightedTestSortingPolicyAt(0, wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 1);
	}
	
	@Test
	public void removeWeightedTestSortingPolicy_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		rrp.removeWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 2);
		rrp.removeWeightedTestSortingPolicy(wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 1);
		rrp.removeWeightedTestSortingPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
	}
	
	@Test
	public void removeWeightedTestSortingPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		rrp.addLastWeightedTestSortingPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2) == wp3);
		rrp.removeWeightedTestSortingPolicyAt(1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 2);
		rrp.removeWeightedTestSortingPolicyAt(0);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 1);
		rrp.removeWeightedTestSortingPolicyAt(0);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
	}
	
	@Test
	public void contains_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		assertFalse(rrp.containsDirectly(wp1));
		assertFalse(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
		rrp.addLastWeightedTestSortingPolicy(wp1);
		assertTrue(rrp.containsDirectly(wp1));
		assertFalse(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
		rrp.addLastWeightedTestSortingPolicy(wp2);
		assertTrue(rrp.containsDirectly(wp1));
		assertTrue(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
		rrp.addLastWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.containsDirectly(wp1));
		assertTrue(rrp.containsDirectly(wp2));
		assertTrue(rrp.containsDirectly(wp3));
		rrp.removeWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.containsDirectly(wp1));
		assertTrue(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
		rrp.removeWeightedTestSortingPolicy(wp2);
		assertTrue(rrp.containsDirectly(wp1));
		assertFalse(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
		rrp.removeWeightedTestSortingPolicy(wp1);
		assertFalse(rrp.containsDirectly(wp1));
		assertFalse(rrp.containsDirectly(wp2));
		assertFalse(rrp.containsDirectly(wp3));
	}
	
	@Test
	public void getWeightedTestSortingPolicyAt_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		rrp.addLastWeightedTestSortingPolicy(wp3);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(0) == wp1);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(1) == wp2);
		assertTrue(rrp.getWeightedTestSortingPolicyAt(2) == wp3);
	}
	
	@Test
	public void getNbOfWeightedTestSortingPolicies_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		
		rrp.addLastWeightedTestSortingPolicy(wp1);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 2);
		rrp.addLastWeightedTestSortingPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 3);
		rrp.removeWeightedTestSortingPolicy(wp3);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 2);
		rrp.removeWeightedTestSortingPolicy(wp2);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 1);
		rrp.removeWeightedTestSortingPolicy(wp1);
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
	}
	
	@Test
	public void getWeightedTestSortingPolicies_test() {
		RoundRobinTestSortingPolicy rrp = new RoundRobinTestSortingPolicy();
		assertEquals(rrp.getNbOfWeightedTestSortingPolicies(), 0);
		
		TestSortingPolicy p1 = new LastFailureFirst();
		TestSortingPolicy p2 = new FrequentFailureFirst();
		TestSortingPolicy p3 = new DistinctFailureFirst();
		WeightedTestSortingPolicy wp1 = new WeightedTestSortingPolicy(p1);
		WeightedTestSortingPolicy wp2 = new WeightedTestSortingPolicy(p2);
		WeightedTestSortingPolicy wp3 = new WeightedTestSortingPolicy(p3);
		rrp.addLastWeightedTestSortingPolicy(wp1);
		rrp.addLastWeightedTestSortingPolicy(wp2);
		rrp.addLastWeightedTestSortingPolicy(wp3);
		List<WeightedTestSortingPolicy> result = rrp.getWeightedTestSortingPolicies();
		assertTrue(result.get(0) == wp1);
		assertTrue(result.get(1) == wp2);
		assertTrue(result.get(2) == wp3);
	}

}
