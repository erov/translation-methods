#include "lexical-analyzer/LexicalAnalyzer.h"
#include "helpers/FirstFollow.h"
#include "parser/Parser.h"
#include "parser/Tree.h"

#include <iostream>
#include <functional>


bool isEqual(std::vector<Token> lhs, const std::vector<Token>& rhs) {
    if (lhs.size() != rhs.size()) {
        return false;
    }
    for (size_t i = 0; i != lhs.size(); ++i) {
        if (lhs[i] != rhs[i]) {
            return false;
        }
    }
    return true;
}

void print(const std::vector<Token>& vec) {
    for (size_t i = 0; i != vec.size(); ++i) {
        if (i != 0) {
            std::cerr << ' ';
        }
        std::cerr << Token2String[vec[i]];
    }
}

bool expectSuccess(const std::string& testName,
                   const std::string& input,
                   const std::vector<Token>& expectedResult) {
    std::vector<Token> result;
    try {
        Parser parser(input);
        Tree* tree = parser.parse();
        result = tree->walkthrough();
        if (isEqual(result, expectedResult)) {
            std::cerr << "[ OK ] Test '" << testName << "' completed successfully.\nInput: '" << input << "'\n\n";
            return true;
        }
    } catch (ParseException& e) {
        std::cerr << "[FAIL] Test '" << testName << "' unexpectedly failed.\nInput: '" << input
                  << "'\nMessage:\n'" << e.what() << "'\n\n";
        return false;
    } catch (...) {
        std::cerr << "[FAIL] Test '" << testName << "' unexpectedly failed\n\n";
        throw;
    }
    std::cerr << "[FAIL] Test '" << testName << "' failed.\nInput: '" << input << "'\nExpected: '";
    print(expectedResult);
    std::cerr << "'\nActual: '";
    print(result);
    std::cerr << "'\n\n";
    return false;
}

bool expectFailure(const std::string& testName,
                   const std::string& input) {
    std::vector<Token> result;
    try {
        Parser parser(input);
        Tree* tree = parser.parse();
        result = tree->walkthrough();
        std::cerr << "[FAIL] Test '" << testName << "' unexpectedly didn't fail.\nInput: '" << input << "'\nResult: '";
        print(result);
        std::cerr << "'\n\n";
        return false;
    } catch (ParseException& e) {
        std::cerr << "[ OK ] Test '" << testName << "' successfully completed with message:\n" << e.what() << "\n\n";
        return true;
    } catch (...) {
        std::cerr << "[FAIL] Test '" << testName << "' unexpectedly failed\n\n";
        throw;
    }
}

int main() {
    {
        expectSuccess("simple test for rule D -> K N: array<T>;",
                      "var array: Array<Int>;",
                      {Token::KEYWORD,
                       Token::NAME,
                       Token::COLON,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::NAME,
                       Token::RANGLE,
                       Token::SEMICOLON,
                       Token::END
                      }
        );
    }
    {
        expectSuccess("reentrancy test for rule D -> K N: array<T>;",
                      "var array: Array<Array<Int>>;",
                      {Token::KEYWORD,
                       Token::NAME,
                       Token::COLON,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::NAME,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::SEMICOLON,
                       Token::END
                      }
        );
    }
    {
        expectSuccess("odd spaces test for rule D -> K N: array<T>;",
                      "var    array         :  Array    <   Array<        Int> >        ;   ",
                      {Token::KEYWORD,
                       Token::NAME,
                       Token::COLON,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::NAME,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::SEMICOLON,
                       Token::END
                      }
        );
    }
    {
        expectSuccess("big reentrancy test for rule D -> K N: array<T>;",
                      "var array: Array<Array<Array<Array<Array<Array<Array<Array<Array<Array<Int>>>>>>>>>>;",
                      {Token::KEYWORD,
                       Token::NAME,
                       Token::COLON,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::NAME,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::SEMICOLON,
                       Token::END
                      }
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- missing all spaces",
                      "vararray:Array<Array<Int>>;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- ';' instead of ':'",
                      "var array; Array<Array<Int>>;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- 'array' instead of 'Array'",
                      "var array: array<String>;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- missing '<'",
                      "var array: Array String>;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- '(' instead of '<'",
                      "var array: Array (String>;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- missing '>'",
                      "var array: Array<String;"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- '}' instead of '>'",
                      "var array: Array<String};"
        );
    }
    {
        expectFailure("wrong test for rule D -> K N: array<T>; -- missing ';'",
                      "var array: Array<String>"
        );
    }
    {
        expectFailure("wrong test for rule K -> keyword -- wrong keyword name",
                      "val array: Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule K -> keyword -- missing keyword",
                      "array: Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule K -> keyword -- typename instead of keyword",
                      "Array array: Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- wrong typename",
                      "var array: Array<;>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- wrong typename 2",
                      "var array: Array<var>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- missing typename",
                      "var array: Array<>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- wrong variable name",
                      "var Array: Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- wrong variable name 2",
                      "var var: Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> name -- missing variable name",
                      "var      : Array<Int>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> array < T > -- missing parameter",
                      "var array: Array<Array<>>;"
        );
    }
    {
        expectFailure("wrong test for rule T -> array < T > -- missing parameterization",
                      "var array: Array<Array>;"
        );
    }
    {
        expectSuccess("weird naming test",
                      "var $array: Array<Array<Int>>;",
                      {Token::KEYWORD,
                       Token::NAME,
                       Token::COLON,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::ARRAY,
                       Token::LANGLE,
                       Token::NAME,
                       Token::RANGLE,
                       Token::RANGLE,
                       Token::SEMICOLON,
                       Token::END
                      }
        );
    }
    {
        expectFailure("wrong empty test",
                      ""
        );
    }
}