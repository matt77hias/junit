package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.Project;
import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.TestChangeUpdater;
import kuleuven.group2.store.StoreEvent;

/**
 * A handler for events that happen with test source code.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public class TestSourceEventHandler extends SourceEventHandler {

	protected final TestChangeUpdater testChangeUpdater;
	
	public TestSourceEventHandler(Project project, TestDatabase testDatabase) {
		super(project, testDatabase);
		this.testChangeUpdater = new TestChangeUpdater(testDatabase, getProject().getClassLoader());
	}

	@Override
	public void setup() throws Exception {
		// Compile all test sources
		JavaCompiler classCompiler = getProject().createTestCompiler();
		CompilationResult result = classCompiler.compileAll();

		handleCompilation(result);
	}

	@Override
	public void consume(List<StoreEvent> events) throws Exception{
		// Collect changes in test sources
		Changes changes = collectChanges(events, getProject().getTestSourceStore());

		// Remove test methods in old test classes
		testChangeUpdater.removeTests(NameUtils.toClassNames(changes.getRemovedResources()));

		// Compile changed test sources
		JavaCompiler classCompiler = getProject().createTestCompiler();
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());

		handleCompilation(result);
	}

	protected void handleCompilation(CompilationResult result) throws Exception {
		// Update test methods in compiled test classes
		testChangeUpdater.updateTestClasses(result.getCompiledClassNames());

		if (!result.isSuccess()) {
			// TODO What exception should be thrown when EclipseCompiler.compile returns false?
			throw new Exception("Compilation of test sources failed: " + result.getErrors());
		}
	}
}
