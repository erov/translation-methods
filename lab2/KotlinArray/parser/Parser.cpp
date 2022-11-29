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
        case Token::KEYWORD:  // D -> keyword name : G ;
            D->addChild(ensureTerminal(Token::KEYWORD));
            D->addChild(ensureTerminal(Token::NAME));
            D->addChild(ensureTerminal(Token::COLON));
            D->addChild(G());
            D->addChild(ensureTerminal(Token::SEMICOLON));
            D->addChild(ensureTerminal(Token::END));
            break;
        default:
            throwParseException({Token::KEYWORD});
    }
    return D;
}

Tree* Parser::G() noexcept(false) {
    Tree* G = new Tree(NonTerminal::G);
    switch (lexicalAnalyzer.getToken()) {
        case Token::ARRAY:  // G -> array < T >
            G->addChild(ensureTerminal(Token::ARRAY));
            G->addChild(ensureTerminal(Token::LANGLE));
            G->addChild(T());
            G->addChild(ensureTerminal(Token::RANGLE));
            break;
        case Token::MAP:  // G -> map < T, T >
            G->addChild(ensureTerminal(Token::MAP));
            G->addChild(ensureTerminal(Token::LANGLE));
            G->addChild(T());
            G->addChild(ensureTerminal(Token::COMMA));
            G->addChild(T());
            G->addChild(ensureTerminal(Token::RANGLE));
            break;
        default:
            throwParseException({Token::ARRAY, Token::MAP});
    }
    return G;
}

Tree* Parser::T() noexcept(false) {
    Tree* t = new Tree(NonTerminal::T);
    switch (lexicalAnalyzer.getToken()) {
        case Token::NAME:  // T -> name
            t->addChild(ensureTerminal(Token::NAME));
            break;
        case Token::ARRAY:  // T -> G
        case Token::MAP:
            t->addChild(G());
            break;
        default:
            throwParseException({Token::NAME, Token::ARRAY, Token::MAP});
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

