package kuleuven.group2;

import kuleuven.group2.compile.EclipseCompilerTest;
import kuleuven.group2.data.DatabaseTests;
import kuleuven.group2.filewatch.FolderWatcherTest;
import kuleuven.group2.store.DirectoryStoreTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DatabaseTests.class, EclipseCompilerTest.class, FolderWatcherTest.class, DirectoryStoreTest.class})
public class AllTests {
	
}
