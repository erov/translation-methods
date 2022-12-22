
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
    public static void main(String[] args) {
        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString("tmp = 42\n\n a = 'c'      \nb=True \r\nd=False \n\ne=\"string\" \nf = tmp\nf = 76\nd = b\n" +
                "z = input()\nprint(z)\nprint(47, \"kek\", 'c', False, tmp)"
                ));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);
        ProgramParser.ProgramContext programContext = parser.program();
        ProgramParserListener listener = new ProgramParserListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, programContext);
    }
}
