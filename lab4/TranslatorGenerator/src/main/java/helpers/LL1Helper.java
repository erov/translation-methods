package helpers;

import grammar.GrammarItem;
import grammar.NonTerminal;
import grammar.Rule;
import grammar.Terminal;
import lexicalAnalyzer.LexicalAnalyzer;

import java.util.*;

public class LL1Helper {
    public static Terminal EPS = new Terminal("", false);
    private final List<Rule> rules;
    private final Map<NonTerminal, Set<Terminal>> first;
    private final Map<NonTerminal, Set<Terminal>> follow;

    public LL1Helper(List<Rule> rules, NonTerminal start) {
        this.rules = rules;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();

        for (Rule rule : rules) {
            initNonTerminalMaps(rule.getLhs());
            for (GrammarItem grammarItem : rule.getRhs()) {
                if (grammarItem instanceof NonTerminal) {
                    initNonTerminalMaps((NonTerminal) grammarItem);
                }
            }
        }
        buildFirst();
        buildFollow(start);
    }

    public Set<Terminal> firstOf(Rule rule) {
        Set<Terminal> result = new HashSet<>();
        if (rule.getRhs().get(0) instanceof Terminal) {
            result.add((Terminal) rule.getRhs().get(0));
        } else {
            result.addAll(first.get((NonTerminal) rule.getRhs().get(0)));
        }
        if (result.contains(EPS)) {
            result.remove(EPS);
            result.addAll(follow.get(rule.getLhs()));
        }
        return result;
    }

    public List<Rule> getRules() {
        return rules;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("First:\n");
        for (Map.Entry<NonTerminal, Set<Terminal>> entry : first.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }

        stringBuilder.append("\nFollow:\n");
        for (Map.Entry<NonTerminal, Set<Terminal>> entry : follow.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }

        return stringBuilder.toString();
    }


    private void initNonTerminalMaps(NonTerminal nonTerminal) {
        if (!first.containsKey(nonTerminal)) {
            first.put(nonTerminal, new HashSet<>());
        }
        if (!follow.containsKey(nonTerminal)) {
            follow.put(nonTerminal, new HashSet<>());
        }
    }

    private void buildFirst() {
        while (true) {
            boolean changed = false;
            for (Rule rule : rules) {
                if (rule.getRhs().get(0) instanceof Terminal) {
                    changed |= addTerminal(first.get(rule.getLhs()), (Terminal) rule.getRhs().get(0));
                } else {
                    changed |= mergeTerminals(first.get(rule.getLhs()), first.get((NonTerminal) rule.getRhs().get(0)), false);
                }
            }
            if (!changed) {
                break;
            }
        }
    }

    private void buildFollow(NonTerminal start) {
        follow.get(start).add(LexicalAnalyzer.END);
        while (true) {
            boolean changed = false;
            for (Rule rule : rules) {
                for (int i = 0; i != rule.getRhs().size(); ++i) {
                    if (rule.getRhs().get(i) instanceof Terminal) {
                        continue;
                    }
                    NonTerminal nonTerminal = (NonTerminal) rule.getRhs().get(i);
                    boolean checkFollow = false;
                    if (i + 1 != rule.getRhs().size()) {
                        Set<Terminal> nextFirst;
                        if (rule.getRhs().get(i + 1) instanceof Terminal) {
                            nextFirst = new HashSet<>();
                            nextFirst.add((Terminal) rule.getRhs().get(i + 1));
                        } else {
                            nextFirst = first.get((NonTerminal) rule.getRhs().get(i + 1));
                        }
                        if (nextFirst.contains(EPS)) {
                            checkFollow = true;
                        }
                        changed |= mergeTerminals(follow.get(nonTerminal), nextFirst, true);
                    } else {
                        checkFollow = true;  // cause gamma == EPS
                    }

                    if (checkFollow) {
                        changed |= mergeTerminals(follow.get(nonTerminal), follow.get(rule.getLhs()), true);
                    }
                }
            }
            if (!changed) {
                break;
            }
        }
    }

    private boolean mergeTerminals(Set<Terminal> lhs, Set<Terminal> rhs, boolean dropEps) {
        boolean changed = false;
        for (Terminal terminal : rhs) {
            if (dropEps && terminal.equals(EPS)) {
                continue;
            }
            changed |= addTerminal(lhs, terminal);
        }
        return changed;
    }
    private boolean addTerminal(Set<Terminal> lhs, Terminal rhs) {
        return lhs.add(rhs);
    }
}
