#include "lexical-analyzer/LexicalAnalyzer.h"
#include "helpers/FirstFollow.h"
#include "parser/Parser.h"
#include "parser/Tree.h"

#include <iostream>
#include <cassert>

void checkLexicalAnalyzer(const std::string& sample) {
    std::cout << "Check LexicalAnalyzer for string=\"" << sample << "\"\n";
    LexicalAnalyzer lexicalAnalyzer(sample);
    for (;;) {
        lexicalAnalyzer.nextToken();
        Token token = lexicalAnalyzer.getToken();
        std::cout << Token2String[token] << ' ';
        if (token == Token::END) {
            break;
        }
    }
    std::cout << '\n' << '\n';
}

auto buildRules() {
    FirstFollow::Rules_t result;
    result.push_back({NonTerminal::D, {
        NonTerminal::K,
        Token::NAME,
        Token::COLON,
        Token::ARRAY,
        Token::LANGLE,
        NonTerminal::T,
        Token::RANGLE,
        Token::SEMICOLON
    }});  // D -> K N: array <T> ;
    result.push_back({NonTerminal::K, {Token::KEYWORD}});  // K -> keyword
    result.push_back({NonTerminal::T, {Token::NAME}}); // T -> name
    result.push_back({NonTerminal::T, {
        Token::ARRAY,
        Token::LANGLE,
        NonTerminal::T,
        Token::RANGLE
    }}); // T -> array < T >

    return result;
}

void printNonTerminal2Terminals(const FirstFollow::NonTerminal2Terminals& nonTerminal2Terminals) {
    for (auto& [lhs, rhs] : nonTerminal2Terminals) {
        std::cout << NonTerminal2String[lhs] << ":";
        for (auto& term : rhs) {
            std::cout << ' ' << Token2String[term];
        }
        std::cout << '\n';
    }
    std::cout << '\n';
}

bool ensureLL1(FirstFollow& helper, FirstFollow::Rules_t& rules) {
    auto printRhs = [](const FirstFollow::RuleRhs_t& rhs) {
        for (auto& item : rhs) {
            if (std::holds_alternative<FirstFollow::Terminal>(item)) {
                std::cerr << Token2String[std::get<FirstFollow::Terminal>(item)] << ' ';
            } else {
                std::cerr << NonTerminal2String[std::get<NonTerminal>(item)] << ' ';
            }
        }
        std::cerr << '\n';
    };

    bool ok = true;
    for (auto& [A, alpha] : rules) {
        for (auto& [B, betta] : rules) {
            if (A == B && alpha != betta) {
                auto fst = helper.FIRSTof(alpha);
                auto snd = helper.FIRSTof(betta);
                if (!FirstFollow::isDistinct(fst, snd)) {
                    std::cerr << "[INFO] Grammar is not LL(1) cause following rules has" \
                                 "non-empty intersection of their FIRST:"
                              << "(1) " << NonTerminal2String[A] << " -> ";
                    printRhs(alpha);
                    std::cerr << "and\n(2) " << NonTerminal2String[B] << " -> ";
                    printRhs(betta);
                    ok = false;
                }
                if (fst.find(Token::EPS) != fst.end()) {
                    snd = helper.getFOLLOW()[A];
                    if (!FirstFollow::isDistinct(fst, snd)) {
                        std::cerr << "[INFO] Grammar is not LL(1) cause following rules has" \
                                     "non-empty intersection of their FOLLOW and FIRST correspondingly:"
                                  << "(1) " << NonTerminal2String[A] << " -> ";
                        printRhs(alpha);
                        std::cerr << "and\n(2) " << NonTerminal2String[B] << " -> ";
                        printRhs(betta);
                        ok = false;
                    }
                }
            }
        }
    }

    if (ok) {
        std::cout << "[INFO] ok, grammar is in LL(1) class.\n\n";
    }

    return ok;
}

int main() {
    checkLexicalAnalyzer("var array: Array<Array<Int>>;");

    auto rules = buildRules();
    auto helper = FirstFollow(rules);

    std::cout << "FIRST:\n";
    helper.buildFIRST();
    printNonTerminal2Terminals(helper.getFIRST());

    std::cout << "FOLLOW\n";
    helper.buildFOLLOW(NonTerminal::D);
    printNonTerminal2Terminals(helper.getFOLLOW());

    assert(ensureLL1(helper, rules));

    Parser parser("var array: Array<Array<Int>>;");
    Tree* expr = parser.parse();
    std::cout << "Parse result:\n";
    for (auto terminal : expr->walkthrough()) {
        std::cout << Token2String[terminal] << ' ';
    }
    std::cout << '\n';

    expr->walkthroughGraphviz("../visualizer/test.dot");
}
