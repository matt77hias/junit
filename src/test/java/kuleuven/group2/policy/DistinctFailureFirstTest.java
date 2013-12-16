package kuleuven.group2.policy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class DistinctFailureFirstTest extends TestSortingPolicyTest {

	@Test
	public void correct_order_test() {
		List<kuleuven.group2.data.Test> list = ImmutableList.of(test1, test2, test3, test4);

		TestSortingPolicy policy = new DistinctFailureFirst();
		List<kuleuven.group2.data.Test> result = policy.getSortedTests(testDatabase, list);

		assertEquals(test1, result.get(0));
		assertEquals(test2, result.get(1));
		assertEquals(test3, result.get(2));
		assertEquals(test4, result.get(3));

		list = ImmutableList.of(test2, test3, test4);
		result = policy.getSortedTests(testDatabase, list);

		assertEquals(test2, result.get(0));
		assertEquals(test3, result.get(1));
		assertEquals(test4, result.get(2));
	}

	@Test
	public void immutable_input_test() {
		TestSortingPolicy policy = new DistinctFailureFirst();

		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		policy.getSortedTests(testDatabase, list);
		assertTrue(list.get(0) == test2);
		assertTrue(list.get(1) == test3);
		assertTrue(list.get(2) == test4);
		assertEquals(list.size(), 3);
	}
}
