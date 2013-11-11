package kuleuven.group2.methodchange;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class MethodHasher {

	protected final ClassReader classReader;

	/**
	 * Create a method hasher which reads the given bytecode.
	 * 
	 * @param classBytes
	 *            The class bytecode to read.
	 */
	public MethodHasher(byte[] classBytes) {
		this(new ClassReader(classBytes));
	}

	/**
	 * Create a method hasher which reads bytecode from the given
	 * {@link ClassReader}.
	 * 
	 * @param classReader
	 *            The class reader from which to read.
	 */
	protected MethodHasher(ClassReader classReader) {
		this.classReader = classReader;
	}

	/**
	 * Get the hashes of all methods.
	 */
	public Map<String, MethodHash> getHashes() {
		MethodBytecodeCollector collector = new MethodBytecodeCollector();
		classReader.accept(collector, ClassReader.SKIP_DEBUG);
		return Maps.transformValues(collector.getCollected(), new BytecodeToHash());
	}

	/**
	 * Collects bytecode of each class method.
	 */
	protected static class MethodBytecodeCollector extends ClassVisitor {

		private final Map<String, ClassWriter> writers = new HashMap<String, ClassWriter>();

		public MethodBytecodeCollector() {
			super(Opcodes.ASM4, null);
		}

		/**
		 * Get the collected bytecodes.
		 */
		public Map<String, byte[]> getCollected() {
			Map<String, byte[]> values = new HashMap<String, byte[]>();
			for (Map.Entry<String, ClassWriter> entry : writers.entrySet()) {
				values.put(entry.getKey(), entry.getValue().toByteArray());
			}
			return values;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			// Get full method name
			String methodName = name + desc;
			// Visit single method
			ClassWriter visitor = new ClassWriter(0);
			writers.put(methodName, visitor);
			return visitor.visitMethod(access, name, desc, signature, exceptions);
		}

	}

	/**
	 * Calculates the hash from the given bytecode.
	 */
	protected static class BytecodeToHash implements Function<byte[], MethodHash> {

		protected final MessageDigest md;

		public BytecodeToHash() {
			try {
				this.md = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException cannotHappen) {
				throw new RuntimeException(cannotHappen);
			}
		}

		@Override
		public MethodHash apply(byte[] bytecode) {
			return new MethodHash(md.digest(bytecode));
		}

	}

}
