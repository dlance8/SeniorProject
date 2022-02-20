package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
//		Scanner file = new Scanner(new File("src/main/ParserV1.java"));
//
//		StringBuilder stringBuilder = new StringBuilder();
//		while (file.hasNextLine()) {
//			String line = file.nextLine().substring(2).trim();
//			if (line.startsWith("p(NonTerminal.")) {
//				stringBuilder.append(line.substring(14, line.indexOf(')'))).append(", ");
//			}
//		}
//		System.out.println(stringBuilder);

		new Test().parse("in/in.txt");

//		java.util.Random r = new java.util.Random();
//		double k = 2.0;
//		double e0 = 8.854E-12;
//		{
//			double A = 2.32E-2;
//			double[] d_ = {1E-3 * 1/3, 1E-3 * 2/3, 1E-3 * 3/3, 1E-3 * 4/3, 1E-3 * 5/3};
//			for (double d : d_) {
//				double C = k * e0 * A / d * (1 + 0.1 * r.nextGaussian());
//				System.out.println((1./d) + "\n\t" + C);
//			}
//			System.out.println("\n");
//		}
//		{
//			double[] A_ = {1.16E-2, 2.32E-2, 4.65E-2, 6.97E-2, 9.29E-2};
//			double d = 1E-3 * 1 / 3;
//			for (double A : A_) {
//				double C = k * A / d * (1 + 0.1 * r.nextGaussian());
//				System.out.println(A + "\n\t" + C);
//			}
//		}
	}

	private static final Collection<HashMap<String, Terminal>> ALL_MAPS = Arrays.asList(TextConstants.KEYWORDS, TextConstants.BOOLEAN_LITERALS, TextConstants.NULL_LITERAL, TextConstants.SEPARATORS, TextConstants.OPERATORS);
	private Scanner file;
	private String line;
	public void parse(String dir) throws FileNotFoundException {
		file = new Scanner(new File(dir));
		StringBuilder stringBuilder = new StringBuilder();
		line = file.nextLine();
		while (file.hasNextLine()) {
			if (line.trim().length() == 0) {
				if (file.hasNextLine())
					line = file.nextLine();
				continue;
			}


			line = line.substring(0, line.length() - 1);

			stringBuilder.append("private boolean ").append(camelCase(line)).append("() {\n\treturn accept(NonTerminal.").append(allCaps(line)).append(",\n");

			StringBuilder statementBuilder = new StringBuilder();
			boolean isFirst = true;
			while (nextLine() && !isNewStatement()) {
				if (isFirst && line.equals("(one of)")) {
					while (nextLine() && !isNewStatement())
						statementBuilder.append(line).append(' ');
					String[] lineSplit = statementBuilder.toString().split(" ");
					statementBuilder.setLength(0);
					for (String string : lineSplit)
						statementBuilder.append(string).append('\n');
					break;
				} else {
					statementBuilder.append(line).append('\n');
				}
				isFirst = false;
			}
			for (String subStatement : statementBuilder.toString().split("\n")) {
				stringBuilder.append("\t\t() -> ");
				String[] lineSplit = subStatement.split(" ");
				for (String string : lineSplit) {
					if (string.startsWith("{") && string.endsWith("}")) {
						x(string.substring(1, string.length() - 1), stringBuilder, "Repeating");
					} else if (string.startsWith("[") && string.endsWith("]")) {
						x(string.substring(1, string.length() - 1), stringBuilder, "Optional");
					} else {
						int closingParentheses = 0;
						while (string.length() > 1) {
							boolean acceptedAny = false;
							if (string.startsWith("{")) {
								acceptedAny = true;
								stringBuilder.append("acceptRepeating(() -> ");
								string = string.substring(1);
							} else if (string.startsWith("[")) {
								acceptedAny = true;
								stringBuilder.append("acceptOptional(() -> ");
								string = string.substring(1);
							}
							if (!acceptedAny) {
								break;
							}
						}
						while (string.length() > 1) {
							boolean acceptedAny = false;
							if (string.endsWith("}")) {
								acceptedAny = true;
								++closingParentheses;
								string = string.substring(0, string.length() - 1);
							} else if (string.endsWith("]")) {
								acceptedAny = true;
								++closingParentheses;
								string = string.substring(0, string.length() - 1);
							}
							if (!acceptedAny) {
								break;
							}
						}
						x(string, stringBuilder, "");
						for (int i = 0; i < closingParentheses; ++i) {
							stringBuilder.append(')');
						}
					}
					stringBuilder.append(" && ");
				}
				stringBuilder.setLength(stringBuilder.length() - 4);
				stringBuilder.append(",\n");
			}
			stringBuilder.setLength(stringBuilder.length() - 2);
			stringBuilder.append(");\n}\n");
		}
		System.out.println(stringBuilder);
	}
	private boolean nextLine() {
		do {
			if (!file.hasNextLine()) return false;
		} while ((line = file.nextLine().trim()).length() == 0);
		return true;
	}
	private void x(String in, StringBuilder out, String add) {
		if (65 <= in.charAt(0) && in.charAt(0) <= 90) {
			out.append("accept").append(add).append("(this::").append(camelCase(in)).append(")");
		} else if (in.equals("<>")) {
			out.append("accept").append(add).append("(Terminal.").append(Terminal.LESS_THAN).append(") && accept").append(add).append("(Terminal.").append(Terminal.GREATER_THAN).append(")");
		} else {
			out.append("accept").append(add).append("(Terminal.");
			for (HashMap<String, Terminal> map : ALL_MAPS) {
				Terminal terminal = map.get(in);
				if (terminal != null) {
					out.append(terminal.toString());
					break;
				}
			}
			out.append(")");
		}
	}
	private boolean isNewStatement() {
		return line.charAt(line.length() - 1) == ':' && (line.length() == 1 || line.charAt(line.length() - 2) != ' ');
	}




	private static String camelCase(String in) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < in.length(); ++i) {
			char c = in.charAt(i);
			if (c == Character.toUpperCase(c) && i != 0)
				out.append(c);
			else
				out.append(Character.toLowerCase(c));
		}
		if (TextConstants.KEYWORDS.containsKey(out.toString())) {
			out.append('_');
		}
		return out.toString();
	}
	private static String allCaps(String in) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < in.length(); ++i) {
			char c = in.charAt(i);
			if (c == Character.toUpperCase(c) && i != 0)
				out.append('_');
			out.append(Character.toUpperCase(c));
		}
		return out.toString();
	}

}