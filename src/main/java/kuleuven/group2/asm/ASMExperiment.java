package kuleuven.group2.asm;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

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
		// Get the method bytecodes
		String className= "kuleuven.group2.HelloWorld";
		// String className= "kuleuven.group2.HelloWorld$Inner";
		Map<String, byte[]> methods= getMethods(className);
		// Create SHA-1 hashes
		MessageDigest md= MessageDigest.getInstance("SHA-1");
		for (Map.Entry<String, byte[]> entry : methods.entrySet()) {
			String methodName= entry.getKey();
			byte[] methodBytes= entry.getValue();
			String methodHash= formatHex(md.digest(methodBytes));
			System.out.println("Method: " + methodName);
			System.out.println("Hash: " + methodHash);
		}
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
	 * Gets the bytecode of all methods in a class.
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
	public static Map<String, byte[]> getMethods(String className)
			throws IOException {
		String classDir= className.replaceAll("\\.", "/");
		ClassReader classReader= new ClassReader(className);
		MethodBytecodeCollector collector= new MethodBytecodeCollector(classDir);
		classReader.accept(collector, ClassReader.SKIP_DEBUG);
		return collector.getCollected();
	}

	/**
	 * Visits each method with a different class visitor.
	 */
	private static abstract class MethodCollector<T extends ClassVisitor, V>
			extends ClassVisitor {

		private final String classDir;

		private final Map<String, T> writers= new HashMap<String, T>();

		public MethodCollector(String classDir) {
			super(Opcodes.ASM4, null);
			this.classDir= classDir;
		}

		public Map<String, V> getCollected() {
			Map<String, V> values= new HashMap<String, V>();
			for (Map.Entry<String, T> entry : writers.entrySet()) {
				values.put(entry.getKey(), getValue(entry.getValue()));
			}
			return values;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			// Get full method name
			String methodName= classDir + "." + name + desc;
			// Visit single method
			T visitor= createVisitor();
			writers.put(methodName, visitor);
			return visitor.visitMethod(access, name, desc, signature,
					exceptions);
		}

		protected abstract T createVisitor();

		protected abstract V getValue(T visitor);

	}

	/**
	 * Collects bytecode of each class method.
	 */
	private static class MethodBytecodeCollector extends
			MethodCollector<ClassWriter, byte[]> {

		public MethodBytecodeCollector(String classDir) {
			super(classDir);
		}

		@Override
		protected ClassWriter createVisitor() {
			return new ClassWriter(0);
		}

		@Override
		protected byte[] getValue(ClassWriter classWriter) {
			return classWriter.toByteArray();
		}

	}

}
