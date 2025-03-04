import java.util.List;
import java.util.Scanner;

public class Lox {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Lox Interpreter. Type an expression or 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) break;

            run(line);
        }
        scanner.close();
    }

    private static void run(String source) {
        LoxScanner scanner = new LoxScanner(source);
        List<Token> tokens = scanner.scanTokens();

        Evaluator evaluator = new Evaluator(tokens);
        double result = evaluator.evaluate();
        System.out.println("Result: " + result);
    }
}
