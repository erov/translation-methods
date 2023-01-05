package parser;

import grammar.*;
import helpers.LL1Helper;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Parser {
    private final LexicalAnalyzer lexicalAnalyzer;
    private final LL1Helper ll1Helper;


    public Parser(final LexicalAnalyzer lexicalAnalyzer, final LL1Helper ll1Helper) throws ParseException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.ll1Helper = ll1Helper;
        lexicalAnalyzer.nextToken();
    }

    public Tree parse(NonTerminal node) throws ParseException {
        Tree tree = new Tree(node);
        boolean found = false;

        List<Terminal> expectedTerminals = new ArrayList<>();
        for (Rule rule : ll1Helper.getRules()) {
            if (!rule.getLhs().equals(node)) {
                continue;
            }
            Set<Terminal> first = ll1Helper.firstOf(rule);
            if (first.contains(lexicalAnalyzer.getToken())) {
                found = true;
                for (GrammarItem grammarItem : rule.getRhs()) {
                    if (grammarItem instanceof Terminal) {
                        tree.addChild(ensureTerminal((Terminal) grammarItem, rule.getType()));
                    } else {
                        Tree child = parse((NonTerminal) grammarItem);
                        child.setType(rule.getType());
                        tree.addChild(child);
                    }
                }
                break;
            }
            expectedTerminals.addAll(first);
        }

        if (!found) {
            throwParseException(expectedTerminals);
        }
        return tree;
    }


    private Tree ensureTerminal(final Terminal terminal, final Type type) throws ParseException {
        if (terminal.equals(LL1Helper.EPS)) {
            return new Tree(LL1Helper.EPS, type);
        }

        if (!lexicalAnalyzer.getToken().equals(terminal)) {
            throwParseException(List.of(terminal));
        }
        Tree tree = new Tree(lexicalAnalyzer.getToken(), type);
        lexicalAnalyzer.nextToken();
        return tree;
    }

    private void throwParseException(final Collection<Terminal> expectedTerminals) throws ParseException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected one of tokens: {");
        for (Terminal terminal : expectedTerminals) {
            stringBuilder
                    .append(terminal.getValue())
                    .append(' ');
        }
        stringBuilder
                .append("} , at position ")
                .append(lexicalAnalyzer.getPosition())
                .append(", but found ")
                .append(lexicalAnalyzer.getToken().getValue());

        throw new ParseException(stringBuilder.toString());
    }
}
