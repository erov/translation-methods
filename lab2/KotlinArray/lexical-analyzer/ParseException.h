#pragma once

#include <stdexcept>

class ParseException : public std::runtime_error {
public:
    explicit ParseException(const std::string& what_arg);
    explicit ParseException(const char* what_arg);
};
