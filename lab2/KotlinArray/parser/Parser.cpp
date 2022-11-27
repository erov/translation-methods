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
        case Token::KEYWORD:  // D -> K N : A < T >
            D->addChild(K());
            D->addChild(N());
            D->addChild(ensureTerminal(Token::COLON));
            D->addChild(A());
            D->addChild(ensureTerminal(Token::LANGLE));
            D->addChild(T());
            D->addChild(ensureTerminal(Token::RANGLE));
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

Tree* Parser::N() noexcept(false) {
    Tree* N = new Tree(NonTerminal::N);
    switch (lexicalAnalyzer.getToken()) {
        case Token::NAME:  // N -> NAME
            N->addChild(ensureTerminal(Token::NAME));
            break;
        default:
            throwParseException({Token::NAME});
    }
    return N;
}

Tree* Parser::A() noexcept(false) {
    Tree* A = new Tree(NonTerminal::A);
    switch (lexicalAnalyzer.getToken()) {
        case Token::NAME:  // A -> NAME
            A->addChild(ensureTerminal(Token::NAME));
            break;
        default:
            throwParseException({Token::NAME});
    }
    return A;
}

Tree* Parser::T() noexcept(false) {
    Tree* T = new Tree(NonTerminal::T);
    switch (lexicalAnalyzer.getToken()) {
        case Token::NAME: // T -> NAME T'
            T->addChild(ensureTerminal(Token::NAME));
            T->addChild(TPrime());
            break;
        default:
            throwParseException({Token::NAME});
    }
    return T;
}

Tree* Parser::TPrime() noexcept(false) {
    Tree* TPrime = new Tree(NonTerminal::TPRIME);
    switch (lexicalAnalyzer.getToken()) {
        case Token::LANGLE:  // T' -> < T >
            TPrime->addChild(ensureTerminal(Token::LANGLE));
            TPrime->addChild(T()); //
            TPrime->addChild(ensureTerminal(Token::RANGLE));
            break;
        case Token::RANGLE:  // T' -> eps
            TPrime->addChild(new Tree(Token::EPS));
            break;
        default:
            throwParseException({Token::LANGLE, Token::RANGLE});
    }
    return TPrime;
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

