#include "Parser.h"


Parser::Parser(std::string inputString)
    : lexicalAnalyzer(std::move(inputString))
{
    lexicalAnalyzer.nextToken();
}



Tree* Parser::parse() noexcept(false) {
    return D();
}


Tree* Parser::D() noexcept(false) {
    Tree* D = new Tree(NonTerminal::D);
    switch (lexicalAnalyzer.getToken()) {
        case Token::KEYWORD:  // D -> K name : array < T > ;
            D->addChild(K());
            D->addChild(ensureTerminal(Token::NAME));
            D->addChild(ensureTerminal(Token::COLON));
            D->addChild(ensureTerminal(Token::ARRAY));
            D->addChild(ensureTerminal(Token::LANGLE));
            D->addChild(T());
            D->addChild(ensureTerminal(Token::RANGLE));
            D->addChild(ensureTerminal(Token::SEMICOLON));
            D->addChild(ensureTerminal(Token::END));
            break;
        default:
            throwParseException({Token::KEYWORD});
    }
    return D;
}

Tree* Parser::K() noexcept(false) {
    Tree* K = new Tree(NonTerminal::K);
    switch (lexicalAnalyzer.getToken()) {
        case Token::KEYWORD:  // K -> KEYWORD
            K->addChild(ensureTerminal(Token::KEYWORD));
            break;
        default:
            throwParseException({Token::KEYWORD});
    }
    return K;
}

Tree* Parser::T() noexcept(false) {
    Tree* t = new Tree(NonTerminal::T);
    switch (lexicalAnalyzer.getToken()) {
        case Token::NAME:  // T -> name
            t->addChild(ensureTerminal(Token::NAME));
            break;
        case Token::ARRAY:  // T -> array < T >
            t->addChild(ensureTerminal(Token::ARRAY));
            t->addChild(ensureTerminal(Token::LANGLE));
            t->addChild(T());
            t->addChild(ensureTerminal(Token::RANGLE));
            break;
        default:
            throwParseException({Token::NAME, Token::ARRAY});
    }
    return t;
}

Tree* Parser::ensureTerminal(FirstFollow::Terminal terminal) noexcept(false) {
    if (lexicalAnalyzer.getToken() != terminal) {
        throwParseException({terminal});
    }
    lexicalAnalyzer.nextToken();
    return new Tree(terminal);
}

void Parser::throwParseException(const std::vector<FirstFollow::Terminal>& expectedTerminals) noexcept(false) {
    std::string what = "Expected one of tokens: ";
    for (auto token : expectedTerminals) {
        what += Token2String[token];
        what += ' ';
    }
    what += "at position ";
    what += std::to_string(lexicalAnalyzer.getPosition());
    what += ", but found ";
    what += Token2String[lexicalAnalyzer.getToken()];
    throw ParseException(what);
}

