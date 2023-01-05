import grammar.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrammarSimpleListener extends GrammarBaseListener {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final List<Rule> rules = new ArrayList<>();
    private final Set<Terminal> terminals = new HashSet<>();
    private final Set<NonTerminal> nonTerminals = new HashSet<>();

    private boolean nowInRhs;

    private String lastLhs;
    private List<GrammarItem> lastRhs;
    private Type lastRuleType;


    @Override
    public void enterGrammar_rule(GrammarParser.Grammar_ruleContext ctx) {
        nowInRhs = false;
        lastRhs = new ArrayList<>();
    }

    @Override
    public void exitGrammar_rule(GrammarParser.Grammar_ruleContext ctx) {
        rules.add(new Rule(lastLhs, lastRhs, lastRuleType));
    }


    @Override
    public void enterLexer_rule(GrammarParser.Lexer_ruleContext ctx) {
        lastLhs = ctx.non_terminal().getText();
        lastRuleType = Type.LEXER;
    }

    @Override
    public void exitLexer_rule(GrammarParser.Lexer_ruleContext ctx) {

    }

    @Override
    public void enterParser_rule(GrammarParser.Parser_ruleContext ctx) {
        lastLhs = ctx.non_terminal(0).getText();
        lastRuleType = Type.PARSER;
    }

    @Override
    public void exitParser_rule(GrammarParser.Parser_ruleContext ctx) {

    }

    @Override
    public void enterNon_terminal(GrammarParser.Non_terminalContext ctx) {
        if (nowInRhs) {
            lastRhs.add(new NonTerminal(ctx.NON_TERMINAL().getText()));
        }
    }

    @Override
    public void exitNon_terminal(GrammarParser.Non_terminalContext ctx) {

    }

    @Override
    public void enterTerminal(GrammarParser.TerminalContext ctx) {

    }

    @Override
    public void exitTerminal(GrammarParser.TerminalContext ctx) {

    }


    @Override
    public void visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == GrammarParser.ARROW) {
            nowInRhs = true;
            return;
        }

        if (node.getSymbol().getType() == GrammarParser.STRING) {
            Terminal terminal = new Terminal(node.getText().substring(1, node.getText().length() - 1), false);
            if (nowInRhs) {
                lastRhs.add(terminal);
            }
            terminals.add(terminal);
        }

        if (node.getSymbol().getType() == GrammarParser.REGEXP) {
            Terminal terminal = new Terminal(node.getText(), true);
            if (nowInRhs) {
                lastRhs.add(terminal);
            }
            terminals.add(terminal);
        }

        if (node.getSymbol().getType() == GrammarParser.NON_TERMINAL) {
            nonTerminals.add(new NonTerminal(node.getText()));
        }

        stringBuilder
                .append(node.getText())
                .append(' ');
    }


    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public Set<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }
}
