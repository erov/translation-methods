#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    bool b=true;
    char c='c';
    char* d="sample_string";
    
    int magic=42;
    a=magic;
    magic=10;
    
    magic=magic;
    
    bool e=false;
    b=e;
    e=true;
    
    d="another_string";
    char* string_copy=d;
    string_copy="";
    
    c='k';
    char f=c;
    c=f;
    
}

