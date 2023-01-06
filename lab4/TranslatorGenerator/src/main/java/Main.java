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

        GrammarLexer lexer = new GrammarLexer(CharStreams.fromString(
                stringBuilder.toString()
        ));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        GrammarParser grammarParser = new GrammarParser(tokens);
        GrammarParser.FileContext GrammarContext = grammarParser.file();

        GrammarSimpleListener listener = new GrammarSimpleListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, GrammarContext);

//        System.err.println(listener);

        List<Rule> rules = listener.getRules();
        Set<Terminal> terminals = listener.getTerminals();

        for (Rule rule : rules) {
            System.out.printf("%s: %s%n", rule.getLhs(), rule.getRhs());
        }

        System.out.println("\nTerminals:");
        for (Terminal terminal : terminals) {
            System.out.println(terminal);
        }

        System.out.println("\nSample:");
        String sample = "(132 + 131) / 200 * 9993 - 56 * 7 / 1 * (0 - 1)";

        try {
            LexicalAnalyzer.checkLexicalAnalyzer(terminals, sample);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println();

        NonTerminal start = null;
        for (NonTerminal nonTerminal : listener.getNonTerminals()) {
            if (nonTerminal.getValue().equals("e")) {
                start = nonTerminal;
                break;
            }
        }

        LL1Helper ll1Helper = new LL1Helper(rules, start);
        System.out.println(ll1Helper);

        final Tree tree;
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(terminals, sample);
            Parser parser = new Parser(lexicalAnalyzer, ll1Helper);
            tree = parser.parse(start, Map.of());
            tree.walkthroughGraphviz("visualizer/test.dot");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        System.err.println(tree.synthesizedAttr.get("e_0.result"));


//        if (args.length == 2) {
//            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
//                writer.write(listener.getResult());
//            } catch (IOException e) {
//                System.err.println("Error occurred while writing result into file");
//            }
//        } else {
//            System.out.println(listener.getResult());
//        }
    }
}