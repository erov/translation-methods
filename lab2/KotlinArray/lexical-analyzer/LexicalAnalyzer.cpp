#include <iostream>
#include "LexicalAnalyzer.h"


LexicalAnalyzer::LexicalAnalyzer(std::string inputString) noexcept
    : inputString(std::move(inputString))
    , currentPosition(0)
    , currentChar('\0')
    , currentToken(Token::END)
{
    nextChar();
}


Token LexicalAnalyzer::getToken() noexcept {
    return currentToken;
}

std::size_t LexicalAnalyzer::getPosition() noexcept {
    return currentPosition;
}

void LexicalAnalyzer::nextToken() noexcept(false) {
    std::string builder;

    while (isSpace(currentChar)) {
        nextChar();
    }

    while (currentChar != '\0'
           && !isSpace(currentChar)
           && SpecialSymbols.find(currentChar) == std::string::npos) {

        builder += currentChar;
        nextChar();
    }

    if (Keywords.find(builder) != Keywords.end()) {
        currentToken = Token::KEYWORD;
    } else if (builder == Array) {
        currentToken = Token::ARRAY;
    } else if (!builder.empty()) {
        currentToken = Token::NAME;
    } else if (currentChar == ':') {
        nextChar();
        currentToken = Token::COLON;
    } else if (currentChar == '<') {
        nextChar();
        currentToken = Token::LANGLE;
    } else if (currentChar == '>') {
        nextChar();
        currentToken = Token::RANGLE;
    } else if (currentChar == ';') {
        nextChar();
        currentToken = Token::SEMICOLON;
    } else if (currentChar == '\0') {
        currentToken = Token::END;
    } else {
        std::string what = "Found unexpected symbol '";
        what += currentChar;
        what += "' at position ";
        what += std::to_string(currentPosition);
        throw ParseException(what);
    }
}


bool LexicalAnalyzer::isSpace(char ch) noexcept {
    return std::isspace(static_cast<int>(ch));
}

void LexicalAnalyzer::nextChar() noexcept {
    currentChar = currentPosition != inputString.size() ? inputString[currentPosition++] : '\0';
}


