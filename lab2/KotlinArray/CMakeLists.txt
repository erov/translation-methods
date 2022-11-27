cmake_minimum_required(VERSION 3.23)
project(KotlinArray)

set(CMAKE_CXX_STANDARD 17)

add_executable(KotlinArray
    main.cpp
    lexical-analyzer/LexicalAnalyzer.h
    lexical-analyzer/LexicalAnalyzer.cpp
    lexical-analyzer/ParseException.h
    lexical-analyzer/ParseException.cpp
    helpers/FirstFollow.h
    helpers/FirstFollow.cpp
    parser/Tree.h
    parser/Tree.cpp
    parser/Parser.h
    parser/Parser.cpp
)

add_executable(KotlinArrayTest
    tests.cpp
    lexical-analyzer/LexicalAnalyzer.h
    lexical-analyzer/LexicalAnalyzer.cpp
    lexical-analyzer/ParseException.h
    lexical-analyzer/ParseException.cpp
    helpers/FirstFollow.h
    helpers/FirstFollow.cpp
    parser/Tree.h
    parser/Tree.cpp
    parser/Parser.h
    parser/Parser.cpp
)

