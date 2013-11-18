package kuleuven.group2.data;

import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.data.updating.MethodTestLinkUpdaterTest;
import kuleuven.group2.data.updating.OssRewriterTest;
import kuleuven.group2.data.updating.TestResultUpdaterTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		TestDatabaseTest.class, TestResultUpdaterTest.class, TestTest.class, JavaSignatureParserTest.class, MethodTestLinkUpdaterTest.class, OssRewriterTest.class
})
public class DatabaseTests {

}
