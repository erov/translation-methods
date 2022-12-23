
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/*
"tmp = 42\n\n" +
                "a = 'c'      \nb=True \r\nd=False \n\ne=\"string\" \nf = tmp\nf = 76\nd = b\n" +
                "z = input()\nprint(z)\nprint(47, \"kek\", 'c', False, tmp)\nl = int( input (  ) )\n" +
                "tmp = (tmp * 47 + 33) // f - tmp\n" +
                "b = not (b or d) and (b or d)\n" +
                "e = e + 'a' + 'd' + a\nprint(e)" +
 */
public class Main {
    public static void main(String[] args) {
        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString(
                "if True :\n" +
                "    print(1)\n" +
                "    if False :\n" +
                "        print(\"Hello\")\n" +
                "        print(1)\n" +
                "    else :\n" +
                "        x = input()\n" +
                "        if 3 * 3 == 9:\n" +
                "            tmp = 1 + 2 * 3 // (55 // 8)\n" +
                "            print(tmp, \"esskeetit\")\n"+
                "        y = input()\n" +
                "else :\n" +
                "    z = input()\n" +
                "    l = input()\n" +
                "    if \"z\" == \"l\":\n" +
                "        print(\"ok\")\n" +
                "c = int(input())\n" +
                "\n\n"
                ));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);
        ProgramParser.ProgramContext programContext = parser.program();
        ProgramParserListener listener = new ProgramParserListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, programContext);
    }
}
