#pragma once

#include "../lexical-analyzer/LexicalAnalyzer.h"

#include <string>
#include <variant>
#include <vector>
#include <unordered_map>
#include <unordered_set>

enum class NonTerminal {
    D,
    T,
    G
};

static std::unordered_map<NonTerminal, std::string> NonTerminal2String = {
    {NonTerminal::D, "D"},
    {NonTerminal::T, "T"},
    {NonTerminal::G, "G"}
};

class FirstFollow {
public:
    using Terminal = Token;
    using RuleRhs_t = std::vector<std::variant<NonTerminal, Terminal>>;
    using Rules_t = std::vector<std::pair<NonTerminal, RuleRhs_t>>;
    using Terminals_t = std::unordered_set<Terminal>;
    using NonTerminal2Terminals = std::unordered_map<NonTerminal, Terminals_t>;

    FirstFollow() = delete;
    explicit FirstFollow(Rules_t) noexcept;

    void buildFIRST() noexcept;
    void buildFOLLOW(NonTerminal) noexcept;

    static bool isDistinct(const Terminals_t&, const Terminals_t&);

    NonTerminal2Terminals getFIRST() noexcept;
    NonTerminal2Terminals getFOLLOW() noexcept;

    Terminals_t FIRSTof(RuleRhs_t&);

private:
    static bool mergeTerminals(Terminals_t& lhs, const Terminals_t& rhs) noexcept;
    static bool addTerminal(Terminals_t& lhs, const Terminal& rhs) noexcept;

private:
    Rules_t rules;
    NonTerminal2Terminals FIRST;
    NonTerminal2Terminals FOLLOW;
};
