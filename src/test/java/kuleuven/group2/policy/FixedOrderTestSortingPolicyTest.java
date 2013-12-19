package kuleuven.group2.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class FixedOrderTestSortingPolicyTest extends TestSortingPolicyTest {

	@Test
	public void correct_order_test() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test4, test2, test3, test1);
		List<kuleuven.group2.data.Test> expected = list;

		TestSortingPolicy policy = new FixedOrderTestSortingPolicy(list);
		List<kuleuven.group2.data.Test> result = policy.getSortedTests(testDatabase, list);
		assertEquals(expected, result);

		list = ImmutableList.of(test2, test3, test1);
		expected = list;
		result = policy.getSortedTests(testDatabase, list);
		assertEquals(expected, result);
	}

	@Test
	public void immutable_input_test() {
		List<kuleuven.group2.data.Test> list = new ArrayList<>();
		list.add(test2);
		list.add(test3);
		list.add(test4);

		TestSortingPolicy policy = new FixedOrderTestSortingPolicy(list);
		policy.getSortedTests(testDatabase, list);

		assertTrue(list.get(0) == test2);
		assertTrue(list.get(1) == test3);
		assertTrue(list.get(2) == test4);
		assertEquals(list.size(), 3);
	}

}
