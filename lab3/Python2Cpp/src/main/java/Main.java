
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString("d = True "));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);
        parser.program();
    }
}
