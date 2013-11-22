package kuleuven.group2;

import kuleuven.group2.classloader.ReloadingStoreClassLoaderTest;
import kuleuven.group2.compile.EclipseCompilerTest;
import kuleuven.group2.data.DatabaseTests;
import kuleuven.group2.filewatch.FolderWatcherTest;
import kuleuven.group2.policy.PolicyTest;
import kuleuven.group2.store.DirectoryStoreTest;
import kuleuven.group2.testrunner.TestRunnerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ReloadingStoreClassLoaderTest.class,
	EclipseCompilerTest.class,
	DatabaseTests.class,
	FolderWatcherTest.class,
	PolicyTest.class,
	DirectoryStoreTest.class,
	TestRunnerTest.class
	})
public class AllTests {
	
}
