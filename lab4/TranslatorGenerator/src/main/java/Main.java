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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.exit;

public class Main {
    private static void printErrorMsg() {
        System.err.println("Usage: java TranslatorGenerator <grammar file> <input string> <starting non-terminal name> [--tree=<graphviz file>] [--result-attr=<result attribute name>]");
        exit(1);
    }
    public static void main(String[] args) {
        if (args.length > 5) {
            printErrorMsg();
        }

        String graphvizFile = null;
        String resultAttribute = null;
        for (int i = 3; i != 5; ++i) {
            if (args.length > i) {
                boolean found = false;
                if (args[i].startsWith("--tree=")) {
                    graphvizFile = args[i].substring(args[i].indexOf('=') + 1);
                    found = true;
                } else if (args[i].startsWith("--result-attr=")) {
                    resultAttribute = args[i].substring(args[i].indexOf('=') + 1);
                    found = true;
                }

                if (!found) {
                    printErrorMsg();
                }
            } else {
                break;
            }
        }

        final StringBuilder stringBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8)))) {
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

        System.out.println("----- Parsed grammar rules -----");
        for (Rule rule : rules) {
            System.out.printf("%s: %s%n", rule.getLhs(), rule.getRhs());
        }

        System.out.println("\n----- Grammar terminals -----");
        for (Terminal terminal : terminals) {
            System.out.println(terminal);
        }

        String sample = args[1];
        System.out.printf("\n----- Input String -----\n%s", sample);

        NonTerminal start = new NonTerminal(args[2]);
        LL1Helper ll1Helper = new LL1Helper(rules, start);
        System.out.println(ll1Helper);

        final Tree tree;
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(terminals, sample);
            Parser parser = new Parser(lexicalAnalyzer, ll1Helper);
            tree = parser.parse(start, Map.of());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (graphvizFile != null) {
            tree.walkthroughGraphviz(graphvizFile);
        }
        if (resultAttribute != null) {
            System.err.println(tree.synthesizedAttr.get(resultAttribute));
        }
    }
}