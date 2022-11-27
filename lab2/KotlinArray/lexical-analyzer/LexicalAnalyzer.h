#pragma once

#include "ParseException.h"

#include <cctype>
#include <string>
#include <unordered_set>
#include <unordered_map>

enum class Token {
    EPS,
    NAME,
    KEYWORD,
    ARRAY,
    LANGLE,
    RANGLE,
    COLON,
    SEMICOLON,
    END
};

static std::unordered_map<Token, std::string> Token2String = {
    {Token::EPS, "EPS"},
    {Token::NAME, "NAME"},
    {Token::KEYWORD, "KEYWORD"},
    {Token::ARRAY, "ARRAY"},
    {Token::LANGLE, "LANGLE"},
    {Token::RANGLE, "RANGLE"},
    {Token::COLON, "COLON"},
    {Token::SEMICOLON, "SEMICOLON"},
    {Token::END, "END"}
};


class LexicalAnalyzer {
public:
    LexicalAnalyzer() = delete;
    explicit LexicalAnalyzer(std::string) noexcept;

    Token getToken() noexcept;
    std::size_t getPosition() noexcept;
    void nextToken() noexcept(false);


private:
    static bool isSpace(char) noexcept;
    void nextChar() noexcept;

private:
    const std::string SpecialSymbols = ",;[]/<>:\\";
    const std::unordered_set<std::string> Keywords = {
        "var"
    };
    const std::string Array = "Array";

    std::string inputString;
    std::size_t currentPosition;
    char currentChar;
    Token currentToken;
};

