package parser;

import grammar.*;
import helpers.LL1Helper;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.ParseException;

import java.util.*;

public class Parser {
    private final LexicalAnalyzer lexicalAnalyzer;
    private final LL1Helper ll1Helper;


    public Parser(final LexicalAnalyzer lexicalAnalyzer, final LL1Helper ll1Helper) throws ParseException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.ll1Helper = ll1Helper;
        lexicalAnalyzer.nextToken();
    }

    public Tree parse(final NonTerminal node, final Map<String, String> inhAttributes) throws ParseException {
        Tree tree = new Tree(node);
        boolean found = false;

        tree.synthesizedAttr.putAll(inhAttributes);

        List<Terminal> expectedTerminals = new ArrayList<>();
        for (Rule rule : ll1Helper.getRules()) {
            if (!rule.getLhs().equals(node)) {
                continue;
            }
            Set<Terminal> first = ll1Helper.firstOf(rule);
            if (first.contains(lexicalAnalyzer.getToken())) {
                found = true;
                for (int i = 0; i != rule.getRhs().size(); ++i) {
                    GrammarItem grammarItem = rule.getRhs().get(i);
                    updateSynthesizedAttr(tree, rule.getAttributeAssignments());
                    String idSuffix = String.valueOf(i + 1);

                    if (rule.getType() == Type.LEXER) {
                        Terminal terminal = (Terminal) grammarItem;
                        Tree child = ensureTerminal(terminal, rule.getType());
                        tree.addChild(child);
                        if(!tree.synthesizedAttr.containsKey(rule.getLhs().getValue() + "_0.result")) {
                            tree.synthesizedAttr.put(rule.getLhs().getValue() + "_0.result", ((Terminal) child.getNode()).getParsedValue());
                        }
                        continue;
                    }

                    if (grammarItem instanceof Terminal) {
                        Tree child = ensureTerminal((Terminal) grammarItem, rule.getType());
                        tree.addChild(child);
                        tree.synthesizedAttr.put(rule.getLhs().getValue() + "_" + idSuffix + ".result", ((Terminal) child.getNode()).getParsedValue());
                    } else if (grammarItem instanceof NonTerminal) {
                        String attributesPrefix = ((NonTerminal) grammarItem).getValue();
                        Map<String, String> childInhAttributes = prepareInhAttributes(tree, attributesPrefix, "_" + idSuffix, "_0");

                        Tree child = parse((NonTerminal) grammarItem, childInhAttributes);
                        child.setType(rule.getType());
                        tree.addChild(child);
                        copyChildSynthesizedAttributes(tree, child, attributesPrefix + "_" + idSuffix, attributesPrefix + "_" + 0);
//                        copyChildSynthesizedAttributes(tree, child, attributesPrefix + "_" + idSuffix, attributesPrefix + "_" + 1);
                    } else {
                        TranslationSymbol translationSymbol = (TranslationSymbol) grammarItem;
                        try {
                            Map<String, String> translationSymbolInhAttributes = prepareInhAttributes(tree, translationSymbol.getValue(), "", "");
                            String name = translationSymbol.getValue();
                            Utils.call(name, translationSymbolInhAttributes, tree.synthesizedAttr, name);
                        } catch (RuntimeException e) {
                            throw new ParseException(
                                    String.format("Exception occurred while processing TranslationSymbol '%s'", translationSymbol),
                                    e
                            );
                        }
                    }

                    updateSynthesizedAttr(tree, rule.getAttributeAssignments());
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


    private void updateSynthesizedAttr(final Tree tree, final Map<String, String> ruleAttributeAssignments) {
        for (Map.Entry<String, String> rule : ruleAttributeAssignments.entrySet()) {
            if (!tree.synthesizedAttr.containsKey(rule.getKey())) {
                if (tree.synthesizedAttr.containsKey(rule.getValue())) {
                    tree.synthesizedAttr.put(rule.getKey(), tree.synthesizedAttr.get(rule.getValue()));
                }
            }
        }
    }

    private Map<String, String> prepareInhAttributes(final Tree tree, final String attributePrefix, final String idSuffix, final String childIdSuffix) {
        final Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : tree.synthesizedAttr.entrySet()) {
            if (entry.getKey().startsWith(attributePrefix + idSuffix + ".")) {
                final int dot = entry.getKey().lastIndexOf('.');
                final String attributeName = entry.getKey().substring(dot);
                result.put(attributePrefix + childIdSuffix + attributeName, entry.getValue());
            }
        }
        return result;
    }

    private void copyChildSynthesizedAttributes(final Tree tree, final Tree child, final String inParentPrefix, final String inChildPrefix) {
        for (Map.Entry<String, String> entry : child.synthesizedAttr.entrySet()) {
            if (entry.getKey().startsWith(inChildPrefix)) {
                final int dot = entry.getKey().lastIndexOf('.');
                final String suffix = entry.getKey().substring(dot);
                if (!tree.synthesizedAttr.containsKey(inParentPrefix + suffix)) {
                    tree.synthesizedAttr.put(inParentPrefix + suffix, entry.getValue());
                }
            }
        }
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
        boolean first = true;
        for (Terminal terminal : expectedTerminals) {
            if (!first) {
                stringBuilder.append(", ");
            } else {
                first = false;
            }
            stringBuilder
                    .append("'")
                    .append(terminal.getValue())
                    .append("'");
        }
        stringBuilder
                .append("} , at position ")
                .append(lexicalAnalyzer.getPosition())
                .append(", but found ")
                .append("'")
                .append(lexicalAnalyzer.getToken().getValue())
                .append("'");
        if (lexicalAnalyzer.getToken().isRegexp()) {
            stringBuilder
                    .append(" with parsed value: '")
                    .append(lexicalAnalyzer.getToken().getParsedValue())
                    .append("'");
        }

        throw new ParseException(stringBuilder.toString());
    }
}
