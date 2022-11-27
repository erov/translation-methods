#include "FirstFollow.h"

#include <cassert>


FirstFollow::FirstFollow(FirstFollow::Rules_t rules) noexcept
    : rules(std::move(rules))
{}


void FirstFollow::buildFIRST() noexcept {
    if (!FIRST.empty()) {
        return;
    }

    for (;;) {
        bool changed = false;
        for (auto& [lhs, rhs] : rules) {
            if (std::holds_alternative<Terminal>(rhs[0])) {
                changed |= addTerminal(FIRST[lhs], std::get<Terminal>(rhs[0]));
            } else {
                changed |= mergeTerminals(FIRST[lhs], FIRST[std::get<NonTerminal>(rhs[0])]);
            }
        }
        if (!changed) {
            break;
        }
    }
}

void FirstFollow::buildFOLLOW(NonTerminal start) noexcept {
    if (!FOLLOW.empty()) {
        return;
    }

    FOLLOW[start].insert(Token::END);

    for (;;) {
        bool changed = false;
        for (auto& [lhs, rhs] : rules) {
            for (size_t i = 0; i != rhs.size(); ++i) {
                if (std::holds_alternative<Terminal>(rhs[i])) {
                    continue;
                }
                bool checkFollow = false;
                if (i + 1 != rhs.size()) {
                    std::unordered_set<Terminal> first;
                    if (std::holds_alternative<Terminal>(rhs[i + 1])) {
                        first.insert(std::get<Terminal>(rhs[i + 1]));
                    } else {
                        first = FIRST[std::get<NonTerminal>(rhs[i + 1])];
                    }
                    auto epsIter = first.find(Token::EPS);
                    if (epsIter != first.end()) {
                        first.erase(epsIter);
                        checkFollow = true;
                    }
                    changed |= mergeTerminals(FOLLOW[std::get<NonTerminal>(rhs[i])], first);
                } else {
                    checkFollow = true;  // cause gamma == EPS
                }

                if (checkFollow) {
                    changed |= mergeTerminals(
                        FOLLOW[std::get<NonTerminal>(rhs[i])],
                        FOLLOW[lhs]
                    );
                }
            }
        }
        if (!changed) {
            break;
        }
    }
}

bool FirstFollow::isDistinct(const FirstFollow::Terminals_t& lhs, const FirstFollow::Terminals_t& rhs) {
    for (auto& terminal : lhs) {
        if (rhs.find(terminal) != rhs.end()) {
            return false;
        }
    }
    return true;
}

FirstFollow::NonTerminal2Terminals FirstFollow::getFIRST() noexcept {
    return FIRST;
}

FirstFollow::NonTerminal2Terminals FirstFollow::getFOLLOW() noexcept {
    return FOLLOW;
}

FirstFollow::Terminals_t FirstFollow::FIRSTof(FirstFollow::RuleRhs_t &rhs) {
    assert(!FIRST.empty());
    if (std::holds_alternative<Terminal>(rhs[0])) {
        return {std::get<Terminal>(rhs[0])};
    }
    return FIRST[std::get<NonTerminal>(rhs[0])];
}


bool FirstFollow::mergeTerminals(Terminals_t& lhs, const Terminals_t& rhs) noexcept {
    bool changed = false;
    for (auto& term : rhs) {
        changed |= addTerminal(lhs, term);
    }
    return changed;
}

bool FirstFollow::addTerminal(Terminals_t& lhs, const FirstFollow::Terminal &rhs) noexcept {
    return lhs.insert(rhs).second;
}

