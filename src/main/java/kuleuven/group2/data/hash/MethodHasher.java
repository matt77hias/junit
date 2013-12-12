package kuleuven.group2.data.hash;

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

/**
 * Calculates hashes for methods using their byte codes.
 * 
 * @author Group2
 * @version 8 November 2013
 */
public class MethodHasher {

	protected final ClassReader classReader;

	/**
	 * Create a method hasher which reads the given bytecode.
	 * 
	 * @pre classBytes must be valid Java byte code
	 * 
	 * @param classBytes
	 *            The class bytecode to read.
	 * @throws RuntimeException
	 * 			  When the class bytes are invalid
	 */
	public MethodHasher(byte[] classBytes) 
		throws RuntimeException{
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
			String fullMethodName = getFullMethodName(name, desc);
			
			return visitSingleMethod(access, name, desc, signature, exceptions, fullMethodName);
		}
		
		private String getFullMethodName(String name, String desc) {
			return name + desc;
		}

		private MethodVisitor visitSingleMethod(int access, String name, String desc, String signature,
				String[] exceptions, String fullMethodName) {
			ClassWriter visitor = new ClassWriter(0);
			writers.put(fullMethodName, visitor);
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
