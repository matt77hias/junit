package kuleuven.group2.sourcehandler;

import java.util.List;

import kuleuven.group2.Project;
import kuleuven.group2.compile.CompilationResult;
import kuleuven.group2.compile.JavaCompiler;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.updating.MethodChangeUpdater;
import kuleuven.group2.store.StoreEvent;

/**
 * Handles events in the (non test) source code, such as deletions, adds and
 * changes.
 * 
 * @author Group2
 * @version 18 November 2013
 */
public class ClassSourceEventHandler extends SourceEventHandler {

	protected final MethodChangeUpdater methodChangeUpdater;
	
	public ClassSourceEventHandler(Project project, TestDatabase testDatabase) {
		super(project, testDatabase);
		this.methodChangeUpdater = new MethodChangeUpdater(testDatabase);
	}

	@Override
	public void setup() throws Exception {
		// Compile all class sources
		JavaCompiler classCompiler = getProject().createClassCompiler();
		CompilationResult result = classCompiler.compileAll();

		handleCompilation(result);
	}

	@Override
	public void consume(List<StoreEvent> events) throws Exception {
		// Collect changes in class sources
		Changes changes = collectChanges(events, getProject().getClassSourceStore());

		// Remove methods in old classes
		methodChangeUpdater.removeClasses(NameUtils.toClassNames(changes.getRemovedResources()));

		// Compile changed class sources
		JavaCompiler classCompiler = getProject().createClassCompiler();
		CompilationResult result = classCompiler.compile(changes.getAddedOrChangedResources());

		handleCompilation(result);
	}

	protected void handleCompilation(CompilationResult result) throws Exception {
		// Detect changes in compiled classes
		methodChangeUpdater.detectChanges(result.getCompiledClasses());

		if (!result.isSuccess()) {
			// TODO What exception should be thrown when EclipseCompiler.compile returns false?
			throw new Exception("Compilation of class sources failed: " + result.getErrors());
		}
	}
}
