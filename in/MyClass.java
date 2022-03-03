public class MyClass {
	public static void main(String[] args) {
		for (int i = 0; i < 10; ++i) {
			if (i < 5) {
				System.out.println(i + " < 5");
			} else if (i == 5) {
				System.out.println(i + " = 5");
			} else {
				System.out.println(i + " > 5");
			}
		}
	}
}
