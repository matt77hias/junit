package kuleuven.group2;

import kuleuven.group2.classloader.ClassLoaderTests;
import kuleuven.group2.compile.EclipseCompilerTest;
import kuleuven.group2.data.DatabaseTests;
import kuleuven.group2.defer.DeferredTests;
import kuleuven.group2.filewatch.FolderWatcherTest;
import kuleuven.group2.policy.PolicyTests;
import kuleuven.group2.rewrite.RewriteTests;
import kuleuven.group2.sourcehandler.SourceEventHandlerTests;
import kuleuven.group2.store.DirectoryStoreTest;
import kuleuven.group2.store.MemoryStoreTest;
import kuleuven.group2.testrunner.TestRunnerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ClassLoaderTests.class,
		
		EclipseCompilerTest.class,
		
		DatabaseTests.class,
		
		DeferredTests.class,

		FolderWatcherTest.class,
		
		PolicyTests.class,

		RewriteTests.class,

		SourceEventHandlerTests.class,

		DirectoryStoreTest.class,
		MemoryStoreTest.class,

		TestRunnerTest.class
})
public class AllTests {

}
