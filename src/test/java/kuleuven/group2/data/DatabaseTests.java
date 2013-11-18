package kuleuven.group2.data;

import kuleuven.group2.data.methodlink.MethodLinkerTest;
import kuleuven.group2.data.methodlink.OssRewriterTest;
import kuleuven.group2.data.signature.JavaSignatureParserTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		TestDatabaseTest.class, TestResultUpdaterTest.class, TestTest.class, JavaSignatureParserTest.class, MethodLinkerTest.class, OssRewriterTest.class
})
public class DatabaseTests {

}
