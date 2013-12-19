package kuleuven.group2.testrunner;

import org.junit.Assert;
import org.junit.Test;

public class FailTest {

	// used as failing test
	@Test
	public void fail() {
		Assert.fail();
	}

}
