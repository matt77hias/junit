package kuleuven.group2;

public class HelloWorld {

	public void a() {
		b(5);
	}

	public void b(int foo) {
		System.out.println("Hallo " + foo);
	}

	public static class Inner {

		public String c(Object o) throws Exception {
			return o.toString();
		}

	}

}
