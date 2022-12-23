
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String input = "integer = 47\n" +
                "boolean = True\n" +
                "character = 'c'\n" +
                "string = \"string\"\n" +
                "\n" +
                "integer_copy = integer\n" +
                "boolean_copy = boolean\n" +
                "character_copy = character\n" +
                "string_copy = string\n" +
                "\n" +
                "input_int = int(input())\n" +
                "input_string = input()\n" +
                "\n" +
                "print(integer, boolean, character_copy, string_copy)\n" +
                "print(input_int)\n" +
                "\n" +
                "a = 10\n" +
                "b = a * 100 // 44 - 78 + (5 + 10)\n" +
                "\n" +
                "if a != b:\n" +
                "    c = True\n" +
                "    d = False\n" +
                "    e = not c and (c or d) or not (c and d)\n" +
                "    if e == True:\n" +
                "        my_string = \"str\"\n" +
                "        if my_string == string:\n" +
                "            print(\"Wow!\")\n" +
                "        else:\n" +
                "            print(\"Strings\", my_string, \"and\",   string, \"are not equals\")\n" +
                "            if a <= b:\n" +
                "                f = b\n" +
                "                b = a\n" +
                "                a = f \n" +
                "    else :\n" +
                "        print(\"Ooops..\")\n" +
                "\n" +
                "string = \"Is there else-branch?\"\n" +
                "\n";


        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString(
                input
        ));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ProgramParser parser = new ProgramParser(tokens);
        ProgramParser.ProgramContext programContext = parser.program();

        ProgramParserListener listener = new ProgramParserListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, programContext);
    }
}
