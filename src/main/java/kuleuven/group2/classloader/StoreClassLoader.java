package kuleuven.group2.classloader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.SecureClassLoader;

import kuleuven.group2.compile.NameUtils;
import kuleuven.group2.store.Store;

/**
 * A ClassLoader which uses the resources in its store to find the bytes of
 * classes to load.
 * 
 * @author Group2
 * @version 19 November 2013
 */
public class StoreClassLoader extends SecureClassLoader {

	protected final Store classStore;

	public StoreClassLoader(Store classStore) {
		super();
		this.classStore = classStore;
	}

	public StoreClassLoader(Store classStore, ClassLoader parent) {
		super(parent);
		this.classStore = classStore;
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		String resourceName = NameUtils.toBinaryName(className);
		if (classStore.contains(resourceName)) {
			byte[] classBytes = classStore.read(resourceName);
			return defineClass(className, classBytes, 0, classBytes.length);
		}
		return super.findClass(className);
	}

	@Override
	protected URL findResource(String name) {
		if (classStore.contains(name)) {
			try {
				return new URL(null, "res://store/" + name, new StoreURLStreamHandler(name));
			} catch (MalformedURLException e) {
			}
		}
		return super.findResource(name);
	}

	protected class StoreURLStreamHandler extends URLStreamHandler {
		private final String resourceName;

		public StoreURLStreamHandler(String resourceName) {
			this.resourceName = resourceName;
		}

		@Override
		protected URLConnection openConnection(final URL u) throws IOException {
			return new URLConnection(u) {
				@Override
				public void connect() throws IOException {
					// No need to connect
				}

				@Override
				public InputStream getInputStream() throws IOException {
					// Read from store
					return new ByteArrayInputStream(classStore.read(resourceName));
				}
			};
		}

	}

}
