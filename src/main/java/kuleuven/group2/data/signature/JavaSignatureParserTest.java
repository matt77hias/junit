package kuleuven.group2.data.signature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JavaSignatureParserTest {

	private final static String testMethod2Arg_Package = "org.eclipse.jdt.internal.junit.runner";
	private final static String testMethod2Arg_ClassName = "FirstRunExecutionListener";
	private final static String testMethod2Arg_MethodName = "sendMessage";
	private final static String testMethod2Arg_Arg1 = "Lorg/eclipse/jdt/internal/junit/runner/ITestIdentifier";
	private final static String testMethod2Arg_Arg2 = "Ljava/lang/String";
	private final static String testMethod2Arg_ReturnType = "V";

	/**
	 * Signature with two arguments.
	 */
	private final static String testMethod2Arg = testMethod2Arg_Package + "." + testMethod2Arg_ClassName + "."
			+ testMethod2Arg_MethodName + "(" + testMethod2Arg_Arg1 + ";" + testMethod2Arg_Arg2 + ";" + ")"
			+ testMethod2Arg_ReturnType;

	/**
	 * Signature with no arguments.
	 */
	private final static String testMethod0Arg = testMethod2Arg_Package + "." + testMethod2Arg_ClassName + "."
			+ testMethod2Arg_MethodName + "(" + ")" + testMethod2Arg_ReturnType;

	/**
	 * Signature with default package.
	 */
	private final static String testMethodDefault = testMethod2Arg_ClassName + "." + testMethod2Arg_MethodName + "("
			+ ")" + testMethod2Arg_ReturnType;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMethod2Arg() {
		JavaSignature signature = new JavaSignatureParser(testMethod2Arg).parseSignature();

		assertEquals(testMethod2Arg_MethodName, signature.getName());
		assertEquals(testMethod2Arg_ClassName, signature.getClassName());
		assertEquals(testMethod2Arg_Package, signature.getPackageName());
		assertEquals(2, signature.getArguments().size());
		assertTrue(signature.getArguments().contains(testMethod2Arg_Arg1));
		assertTrue(signature.getArguments().contains(testMethod2Arg_Arg2));
		assertEquals(testMethod2Arg_ReturnType, signature.getReturnType());
	}

	@Test
	public void testMethod0Arg_Arguments() {
		JavaSignature signature = new JavaSignatureParser(testMethod0Arg).parseSignature();

		assertTrue(signature.getArguments().isEmpty());
	}

	@Test
	public void testMethodDefault_Package() {
		JavaSignature signature = new JavaSignatureParser(testMethodDefault).parseSignature();

		assertTrue(signature.getPackageName().isEmpty());
	}

}
