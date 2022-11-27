#include "ParseException.h"

ParseException::ParseException(const std::string &what_arg)
    : runtime_error(what_arg)
{}

ParseException::ParseException(const char *what_arg)
    : runtime_error(what_arg)
{}

