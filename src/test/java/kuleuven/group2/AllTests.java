package kuleuven.group2;

import kuleuven.group2.classloader.ReloadingStoreClassLoaderTest;
import kuleuven.group2.classloader.StoreClassLoaderTest;
import kuleuven.group2.compile.EclipseCompilerTest;
import kuleuven.group2.data.TestDatabaseTest;
import kuleuven.group2.data.TestTest;
import kuleuven.group2.data.hash.MethodHasherTest;
import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.data.updating.MethodChangeUpdaterTest;
import kuleuven.group2.data.updating.MethodTestLinkUpdaterTest;
import kuleuven.group2.data.updating.OssRewriterLoaderTest;
import kuleuven.group2.data.updating.OssRewriterTest;
import kuleuven.group2.data.updating.TestChangeUpdaterTest;
import kuleuven.group2.data.updating.TestResultUpdaterTest;
import kuleuven.group2.defer.DeferredConsumerTest;
import kuleuven.group2.defer.DeferredTaskRunnerTest;
import kuleuven.group2.filewatch.FolderWatcherTest;
import kuleuven.group2.policy.DistinctFailureFirstTest;
import kuleuven.group2.policy.FrequentFailureTest;
import kuleuven.group2.policy.LastFailureFirstTest;
import kuleuven.group2.sourcehandler.ClassSourceEventHandlerTest;
import kuleuven.group2.sourcehandler.SourceEventHandlerTest;
import kuleuven.group2.sourcehandler.TestSourceEventHandlerTest;
import kuleuven.group2.store.DirectoryStoreTest;
import kuleuven.group2.testrunner.TestRunnerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ReloadingStoreClassLoaderTest.class,
	StoreClassLoaderTest.class,
	
	EclipseCompilerTest.class,
	
	MethodHasherTest.class,
	
	JavaSignatureParserTest.class,
	
	MethodChangeUpdaterTest.class,
	MethodTestLinkUpdaterTest.class,
	OssRewriterLoaderTest.class,
	OssRewriterTest.class,
	TestChangeUpdaterTest.class,
	TestResultUpdaterTest.class,
	
	TestDatabaseTest.class,
	TestTest.class,
	
	DeferredConsumerTest.class,
	DeferredTaskRunnerTest.class,
	
	FolderWatcherTest.class,
	
	DistinctFailureFirstTest.class,
	FrequentFailureTest.class,
	LastFailureFirstTest.class,
	
	ClassSourceEventHandlerTest.class,
	SourceEventHandlerTest.class,
	TestSourceEventHandlerTest.class,
	
	DirectoryStoreTest.class,
	
	TestRunnerTest.class
	})
public class AllTests {
	
}
