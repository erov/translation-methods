package helpers;

import grammar.*;
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
        for (int i = 0; i != rule.getRhs().size(); ++i) {
            if (rule.getRhs().get(i) instanceof TranslationSymbol) {
                continue;
            }
            if (rule.getRhs().get(i) instanceof Terminal) {
                result.add((Terminal) rule.getRhs().get(i));
            } else {
                result.addAll(first.get((NonTerminal) rule.getRhs().get(i)));
            }
            if (result.contains(EPS)) {
                result.remove(EPS);
                result.addAll(follow.get(rule.getLhs()));
            }
            break;
        }
        return result;
    }

    public List<Rule> getRules() {
        return rules;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("----- Specified grammar First sets -----\n");
        for (Map.Entry<NonTerminal, Set<Terminal>> entry : first.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }

        stringBuilder.append("\n----- Specified grammar Follow sets -----\n");
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
                for (int i = 0; i != rule.getRhs().size(); ++i) {
                    if (rule.getRhs().get(i) instanceof TranslationSymbol) {
                        continue;
                    }
                    if (rule.getRhs().get(i) instanceof Terminal) {
                        changed |= addTerminal(first.get(rule.getLhs()), (Terminal) rule.getRhs().get(i));
                    } else {
                        changed |= mergeTerminals(first.get(rule.getLhs()), first.get((NonTerminal) rule.getRhs().get(i)), false);
                    }
                    break;
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
                    if (!(rule.getRhs().get(i) instanceof NonTerminal nonTerminal)) {
                        continue;
                    }
                    boolean checkFollow = false;
                    if (i + 1 != rule.getRhs().size()) {
                        Set<Terminal> nextFirst = new HashSet<>();
                        for (int j = i + 1; j < rule.getRhs().size(); ++j) {
                            if (rule.getRhs().get(j) instanceof TranslationSymbol) {
                                continue;
                            }

                            if (rule.getRhs().get(j) instanceof Terminal) {
                                nextFirst.add((Terminal) rule.getRhs().get(j));
                            } else {
                                nextFirst = first.get((NonTerminal) rule.getRhs().get(j));
                            }
                            break;
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
