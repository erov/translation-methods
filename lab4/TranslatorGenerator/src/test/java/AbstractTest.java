import grammar.NonTerminal;
import grammar.Rule;
import grammar.Terminal;
import helpers.LL1Helper;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.ParseException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import parser.Parser;
import parser.Tree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class AbstractTest {
    protected Tree parse(final String grammarPath, final String startNonTerminal, final String inputString) {
        final StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(grammarPath), StandardCharsets.UTF_8)))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append('\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }

        GrammarLexer lexer = new GrammarLexer(CharStreams.fromString(
                stringBuilder.toString()
        ));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        GrammarParser grammarParser = new GrammarParser(tokens);
        GrammarParser.FileContext GrammarContext = grammarParser.file();

        GrammarSimpleListener listener = new GrammarSimpleListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, GrammarContext);

        List<Rule> rules = listener.getRules();
        Set<Terminal> terminals = listener.getTerminals();

        NonTerminal start = new NonTerminal(startNonTerminal);

        LL1Helper ll1Helper = new LL1Helper(rules, start);

        final Tree tree;
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(terminals, inputString);
            Parser parser = new Parser(lexicalAnalyzer, ll1Helper);
            tree = parser.parse(start, Map.of());
            tree.walkthroughGraphviz("visualizer/test.dot");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tree;
    }

}
