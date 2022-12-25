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
    
    bool e=false;
    b=e;
    e=true;
    
    d="another_string";
    char* string_copy=d;
    string_copy="";
    
    c='k';
    char f=c;
    c=f;
    
    printf("%d \n", a);
    printf("%d %d \n", a, b);
    printf("%d %d %c \n", a, b, c);
    printf("%d %d %c %s \n", a, b, c, d);
    printf("%d %d %d %d %c %c %s %s \n", a, 10, b, false, c, 'c', d, "string");
    
}

