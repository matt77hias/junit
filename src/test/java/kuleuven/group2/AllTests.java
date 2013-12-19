package kuleuven.group2;

import kuleuven.group2.classloader.ClassLoaderTests;
import kuleuven.group2.classloader.ReloadingStoreClassLoaderTest;
import kuleuven.group2.classloader.StoreClassLoaderTest;
import kuleuven.group2.compile.EclipseCompilerTest;
import kuleuven.group2.data.TestDatabaseTest;
import kuleuven.group2.data.TestTest;
import kuleuven.group2.data.hash.MethodHasherTest;
import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.data.updating.MethodChangeUpdaterTest;
import kuleuven.group2.data.updating.MethodTestLinkUpdaterTest;
import kuleuven.group2.data.updating.TestChangeUpdaterTest;
import kuleuven.group2.data.updating.TestResultUpdaterTest;
import kuleuven.group2.defer.DeferredConsumerTest;
import kuleuven.group2.defer.DeferredTaskRunnerTest;
import kuleuven.group2.defer.DeferredTests;
import kuleuven.group2.filewatch.FolderWatcherTest;
import kuleuven.group2.policy.PolicyTests;
import kuleuven.group2.rewrite.OssRewriterLoaderTest;
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
		ReloadingStoreClassLoaderTest.class,
		StoreClassLoaderTest.class,
		
		EclipseCompilerTest.class,
		
		MethodHasherTest.class,
		
		JavaSignatureParserTest.class,
		
		MethodChangeUpdaterTest.class,
		MethodTestLinkUpdaterTest.class,
		// OssRewriterTest.class,
		TestChangeUpdaterTest.class,
		TestResultUpdaterTest.class,

		TestDatabaseTest.class,
		TestTest.class,
		
		DeferredConsumerTest.class,
		DeferredTaskRunnerTest.class,
		DeferredTests.class,

		FolderWatcherTest.class,
		
		PolicyTests.class,

		OssRewriterLoaderTest.class,

		SourceEventHandlerTests.class,

		DirectoryStoreTest.class,
		MemoryStoreTest.class,

		TestRunnerTest.class
})
public class AllTests {

}
