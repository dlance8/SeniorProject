package GUI;
import main.Lexer;
import main.Parser;
import main.Token;
import main.Translator;
import tree.NonterminalNode;
import java.util.ArrayList;
public class ProcessManager {
	private boolean inProgress;

	private ArrayList<Token> lexerResult;
	private NonterminalNode parserResult;
	private String translatorResult;

	public synchronized String run(String input) {
		Lexer lexer = new Lexer();
		Parser parser = new Parser();
		Translator translator = new Translator();


		new Thread(() -> {
			inProgress = true;
			lexerResult = lexer.lexFromFile(input);
			inProgress = false;
			synchronized (this) {
				notify();
			}
		}).start();
		try {
			while (inProgress) {
				wait();
			}
		} catch (InterruptedException e) {
			return lexer.getErrorMessage();
		}

		new Thread(() -> {
			inProgress = true;
			parserResult = parser.parse(lexerResult);
			inProgress = false;
			synchronized (this) {
				notify();
			}
		}).start();
		try {
			while (inProgress) {
				wait();
			}
		} catch (InterruptedException e) {
			return parser.getErrorMessage();
		}

		new Thread(() -> {
			inProgress = true;
			translatorResult = translator.translate(parserResult);
			inProgress = false;
			synchronized (this) {
				notify();
			}
		}).start();
		try {
			while (inProgress) {
				wait();
			}
		} catch (InterruptedException e) {
			return translator.getErrorMessage();
		}

		return translatorResult;
	}
}