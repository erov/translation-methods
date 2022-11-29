#pragma once

#include "../helpers/FirstFollow.h"
#include "Tree.h"

class Parser {
public:
    Parser() = delete;
    explicit Parser(std::string);

    Tree* parse() noexcept(false);

private:
    Tree* D() noexcept(false);
    Tree* G() noexcept(false);
    Tree* T() noexcept(false);


private:
    Tree* ensureTerminal(FirstFollow::Terminal) noexcept(false);
    void throwParseException(const std::vector<FirstFollow::Terminal>&) noexcept(false);

private:
    LexicalAnalyzer lexicalAnalyzer;
};

