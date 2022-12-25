
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final StringBuilder stringBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8)))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append('\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }

        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString(
                stringBuilder.toString()
        ));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ProgramParser parser = new ProgramParser(tokens);
        ProgramParser.ProgramContext programContext = parser.program();

        ProgramParserListener listener = new ProgramParserListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, programContext);
    }
}
