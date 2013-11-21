package kuleuven.group2.data;

import kuleuven.group2.data.hash.MethodHasherTest;
import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.data.updating.MethodChangeUpdaterTest;
import kuleuven.group2.data.updating.MethodTestLinkUpdaterTest;
import kuleuven.group2.data.updating.OssRewriterTest;
import kuleuven.group2.data.updating.TestChangeUpdaterTest;
import kuleuven.group2.data.updating.TestResultUpdaterTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		MethodHasherTest.class,
		JavaSignatureParserTest.class,
		MethodChangeUpdaterTest.class,
		MethodTestLinkUpdaterTest.class,
		OssRewriterTest.class,
		TestChangeUpdaterTest.class,
		TestResultUpdaterTest.class,
		TestDatabaseTest.class,
		TestTest.class
})
public class DatabaseTests {

}
