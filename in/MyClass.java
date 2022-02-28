public class MyClass {
	public static void main(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < i; ++j) {
				stringBuilder.append('*');
			}
			stringBuilder.append('\n');
		}
		System.out.println(stringBuilder);
	}
}