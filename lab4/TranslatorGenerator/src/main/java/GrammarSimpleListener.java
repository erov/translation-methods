import grammar.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public class GrammarSimpleListener extends GrammarBaseListener {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final List<Rule> rules = new ArrayList<>();
    private final Set<Terminal> terminals = new HashSet<>();
    private final Set<NonTerminal> nonTerminals = new HashSet<>();

    private boolean nowInRhs;

    private String lastLhs;
    private List<GrammarItem> lastRhs;
    private Type lastRuleType;
    private List<String> lastAttributeAssignment;


    @Override
    public void enterGrammar_rule(GrammarParser.Grammar_ruleContext ctx) {
        lastRhs = new ArrayList<>();
        lastAttributeAssignment = new ArrayList<>();
    }

    @Override
    public void exitGrammar_rule(GrammarParser.Grammar_ruleContext ctx) {
        Map<String, String> attributeAssignments = new HashMap<>();
        if (!lastAttributeAssignment.isEmpty()) {
            assert(lastAttributeAssignment.size() % 2 == 0);
            for (int i = 0; i != lastAttributeAssignment.size(); i += 2) {
                attributeAssignments.put(lastAttributeAssignment.get(i), lastAttributeAssignment.get(i + 1));
            }
        }
        rules.add(new Rule(lastLhs, lastRhs, lastRuleType, attributeAssignments));
        nowInRhs = false;
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
    public void enterTranslation_symbol(GrammarParser.Translation_symbolContext ctx) {
        assert(nowInRhs);
        lastRhs.add(new TranslationSymbol(ctx.TRANSLATION_SYMBOL().getText()));
    }

    @Override
    public void exitTranslation_symbol(GrammarParser.Translation_symbolContext ctx) {

    }

    @Override
    public void enterAttr_assign_line(GrammarParser.Attr_assign_lineContext ctx) {

    }

    @Override
    public void exitAttr_assign_line(GrammarParser.Attr_assign_lineContext ctx) { }


    @Override
    public void visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == GrammarParser.ARROW) {
            nowInRhs = true;
            return;
        }

        if (node.getSymbol().getType() == GrammarParser.FLPAREN) {
            nowInRhs = false;
            return;
        }

        if (node.getSymbol().getType() == GrammarParser.FRPAREN) {
            return;
        }

        if (node.getSymbol().getType() == GrammarParser.ATTRIBUTE) {
            lastAttributeAssignment.add(node.getText());
            return;
        }

        if (node.getSymbol().getType() == GrammarParser.SKIP_EQ) {
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
