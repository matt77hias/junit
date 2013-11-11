package kuleuven.group2.signature;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JavaSignatureParserTest {

	private final static String testMethod2Arg_Package
		= "org/eclipse/jdt/internal/junit/runner/";
	private final static String testMethod2Arg_MethodName
		= "FirstRunExecutionListener.sendMessage";
	private final static String testMethod2Arg_Arg1
		= "Lorg/eclipse/jdt/internal/junit/runner/ITestIdentifier";
	private final static String testMethod2Arg_Arg2
		= "Ljava/lang/String";
	private final static String testMethod2Arg_ReturnType
		= "V";
	private final static String testMethod2Arg
		= testMethod2Arg_Package
				+ testMethod2Arg_MethodName + "("
				+ testMethod2Arg_Arg1 + ";"
				+ testMethod2Arg_Arg2 + ";"
				+ ")"
				+ testMethod2Arg_ReturnType;
	
	private final static String testMethod0Arg
	= testMethod2Arg_Package
			+ testMethod2Arg_MethodName + "("
			+ ")"
			+ testMethod2Arg_ReturnType;

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
	public void testMethod2ArgMethodName() {
		JavaSignatureParser parser = new JavaSignatureParser(testMethod2Arg);
		
		String methodName = parser.parseMethodName();
		
		assertEquals(testMethod2Arg_MethodName, methodName);
	}

	@Test
	public void testMethod2ArgPackageName() {
		JavaSignatureParser parser = new JavaSignatureParser(testMethod2Arg);
		
		String packageName = parser.parsePackageName();
		
		assertEquals(testMethod2Arg_Package, packageName);
	}

	@Test
	public void testMethod2ArgReturnType() {
		JavaSignatureParser parser = new JavaSignatureParser(testMethod2Arg);
		
		String returnType = parser.parseReturnType();
		
		assertEquals(testMethod2Arg_ReturnType, returnType);
	}

	@Test
	public void testMethod2ArgArguments() {
		JavaSignatureParser parser = new JavaSignatureParser(testMethod2Arg);
		
		List<String> arguments = parser.parseArguments();
		
		assertTrue(arguments.contains(testMethod2Arg_Arg1));
		assertTrue(arguments.contains(testMethod2Arg_Arg2));
		assertEquals(arguments.size(), 2);
	}

	@Test
	public void testMethod0ArgArguments() {
		JavaSignatureParser parser = new JavaSignatureParser(testMethod0Arg);
		
		List<String> arguments = parser.parseArguments();
		
		assertTrue(arguments.isEmpty());
	}

}
