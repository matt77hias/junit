package kuleuven.group2.data.updating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.data.TestDatabase;
import kuleuven.group2.data.TestedMethod;
import kuleuven.group2.data.signature.JavaSignature;
import kuleuven.group2.data.signature.JavaSignatureParser;
import kuleuven.group2.data.signature.JavaSignatureParserTest;
import kuleuven.group2.rewrite.OssRewriterLoader;
import kuleuven.group2.testrunner.TestRunner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Predicate;

public class MethodTestLinkUpdaterTest {

	private static OssRewriterLoader ossRewriterLoader = OssRewriterLoader.getInstance();
	
	private TestDatabase database;
	private MethodTestLinkUpdater updater;
	private CurrentTestHolder currentTestHolder;
	
	private static String testClassName = NameUtils.toInternalName(TestedClass.class.getName()); 
	private static String getFalsePath = testClassName+".getFalse()Z";
	private static TestedMethod getFalse = new TestedMethod(getFalsePath);
	private static String getTruePath = testClassName+".getTrue()Z";
	private static TestedMethod getTrue = new TestedMethod(getTruePath);
	
	private static kuleuven.group2.data.Test testMethod1S = new kuleuven.group2.data.Test("TestClass", "testMethod1S");
	private static kuleuven.group2.data.Test testMethod2F = new kuleuven.group2.data.Test("TestClass", "testMethod2F");
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		database = new TestDatabase();
		database.addMethod(getTrue);
		currentTestHolder = new CurrentTestHolder();
		updater = new MethodTestLinkUpdater(database, ossRewriterLoader);
		updater.registerTestHolder(currentTestHolder);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLinks() throws Exception {
		assertEquals(0, database.getNbLinks());
		
		currentTestHolder.setCurrentTest(testMethod2F);
		updater.enterMethod(getFalsePath);
		
		// getFalse was not added to the database, and will not be linked
		assertFalse(database.containsMethod(getFalse.getSignature()));
		assertEquals(0, database.getNbLinks());
		assertFalse(database.containsMethodTestLink(getFalse, testMethod2F));
		
		currentTestHolder.setCurrentTest(testMethod1S);
		updater.enterMethod(getTruePath);
		
		// getTrue was added to the database, and now has a link with testMethod1S
		assertTrue(database.containsMethod(getTrue.getSignature()));
		assertEquals(1, database.getNbLinks());
		assertTrue(database.containsMethodTestLink(getTrue, testMethod1S));
	}
	
	@Test
	public void testWithOssRewriter() throws Exception {
		TestRunner testRunner = new TestRunner(getClass().getClassLoader());
		updater.registerTestHolder(testRunner);

		String testClassName = JavaSignatureParserTest.class.getName();
		String testMethodName = "testMethod2Arg";
		kuleuven.group2.data.Test test = new kuleuven.group2.data.Test(testClassName, testMethodName);

		String testedSignaturePath = NameUtils.toInternalName(JavaSignatureParser.class.getName())
				+ ".parseSignature()L" + NameUtils.toInternalName(JavaSignature.class.getName()) + ";";
		JavaSignature testedSignature = new JavaSignatureParser(testedSignaturePath).parseSignature();

		database.addMethod(new TestedMethod(testedSignature));

		ossRewriterLoader.setClassTransformFilter(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains(NameUtils.toInternalName("kuleuven.group2.data.signature.JavaSignatureParser"));
			}
		});
		
		ossRewriterLoader.registerMonitor(updater);
		ossRewriterLoader.enable();

		testRunner.runTestMethods(test);

		ossRewriterLoader.disable();
		ossRewriterLoader.unregisterMonitor(updater);

		System.out.println(database.getLinkedMethods(new kuleuven.group2.data.Test(testClassName, testMethodName)));
		assertTrue(database.containsMethodTestLink(database.getMethod(testedSignature), test));
	}
	
	protected static class TestedClass {

		public boolean getFalse() {
			return false;
		}

		public boolean getTrue() {
			return true;
		}
		
	}
	
	private class CurrentTestHolder implements CurrentRunningTestHolder {
		
		private kuleuven.group2.data.Test currentTest;
		
		public CurrentTestHolder() {
			// do nothing
		}

		@Override
		public kuleuven.group2.data.Test getCurrentRunningTest() {
			return currentTest;
		}
		
		public void setCurrentTest(kuleuven.group2.data.Test test) {
			currentTest = test;
		}
		
	}

}
