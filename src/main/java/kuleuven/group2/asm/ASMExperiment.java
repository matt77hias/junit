package kuleuven.group2.asm;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Adapted from: Pretty printing a method in ASM Bytecode - Stack Overflow
 * 
 * http://stackoverflow.com/a/7995857/1321716
 */
public class ASMExperiment {

	public static void main(String[] args) throws Exception {
		// Get the method bytecode
		byte[] methodBytes= getMethod("kuleuven.group2.HelloWorld", "a");
		// Create a SHA-1 hash
		MessageDigest md= MessageDigest.getInstance("SHA-1");
		String methodHash= formatHex(md.digest(methodBytes));
		System.out.println("Hash: " + methodHash);
	}

	private static String formatHex(final byte[] hash) {
		Formatter formatter= new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result= formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * Gets the bytecode method body of a given method.
	 * 
	 * @param className
	 *            The class name to search for.
	 * @param methodName
	 *            The method name.
	 * @param methodDescriptor
	 *            The method's descriptor. Can be null if one wishes to just get
	 *            the first method with the given name.
	 * @throws IOException
	 */
	public static byte[] getMethod(String className, String methodName,
			String methodDescriptor) throws IOException {
		ClassReader classReader= new ClassReader(className);
		ClassWriter classWriter= new ClassWriter(0);
		MethodSelectorVisitor methodSelectorVisitor= new MethodSelectorVisitor(
				classWriter, methodName, methodDescriptor);
		classReader.accept(methodSelectorVisitor, ClassReader.SKIP_DEBUG);
		return classWriter.toByteArray();
	}

	/**
	 * Gets the bytecode method body of a given method.
	 * 
	 * @param className
	 *            The class name to search for.
	 * @param methodName
	 *            The method name.
	 * @throws IOException
	 */
	public static byte[] getMethod(String className, String methodName)
			throws IOException {
		return getMethod(className, methodName, null);
	}

	/**
	 * Class visitor which only visits the given method.
	 */
	private static class MethodSelectorVisitor extends ClassVisitor {

		private final String methodName;

		private final String methodDescriptor;

		public MethodSelectorVisitor(ClassVisitor cv, String methodName,
				String methodDescriptor) {
			super(Opcodes.ASM4, cv);
			this.methodName= methodName;
			this.methodDescriptor= methodDescriptor;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {

			if (methodName.equals(name)) {
				if (methodDescriptor == null)
					return super.visitMethod(access, name, desc, signature,
							exceptions);

				if (methodDescriptor.equals(desc))
					return super.visitMethod(access, name, desc, signature,
							exceptions);
			}

			return null;
		}

	}

}
