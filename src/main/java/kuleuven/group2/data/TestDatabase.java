package kuleuven.group2.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that has access to all tests (Test) and tested methods (TestedMethod)
 * for access by updaters and users.
 * 
 * @author Vital D'haveloose
 */
public class TestDatabase {
	
	protected Set<Test> tests = Collections.synchronizedSet(new HashSet<Test>());
	protected Set<TestedMethod> methods = Collections.synchronizedSet(new HashSet<TestedMethod>());
	
	//TODO: implementatie van verschillende access-interfaces voor users
	
	//TODO implementeren, daarbij rekening houden met concurrency (synchronized sets) en uitbreidbaarheid

	
}
