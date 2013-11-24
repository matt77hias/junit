package kuleuven.group2.policy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FrequentFailureTest extends PolicyTest {

	@Test
	public void correct_order_test() {
		TestSortingPolicy policy = new FrequentFailureFirst();
		kuleuven.group2.data.Test[] result = policy.getSortedTests(testDatabase);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test1);
		assertTrue(result[2] == test2);
		assertTrue(result[3] == test4);
		
		result = policy.getSortedTests(testDatabase, new kuleuven.group2.data.Test[] {test2, test3, test4});
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
		
		List<kuleuven.group2.data.Test> list = new ArrayList<kuleuven.group2.data.Test>();
		list.add(test2);
		list.add(test3);
		list.add(test4);
		result = policy.getSortedTests(testDatabase, list);
		
		assertTrue(result[0] == test3);
		assertTrue(result[1] == test2);
		assertTrue(result[2] == test4);
	}
	
	@Test
	public void immutable_input_test() {
		TestSortingPolicy policy = new FrequentFailureFirst();
		
		kuleuven.group2.data.Test[] input = new kuleuven.group2.data.Test[] {test2, test3, test4};
		policy.getSortedTests(testDatabase, input);
		assertTrue(input[0] == test2);
		assertTrue(input[1] == test3);
		assertTrue(input[2] == test4);
		assertEquals(input.length, 3);
		
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
