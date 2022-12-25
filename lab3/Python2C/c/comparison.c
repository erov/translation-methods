#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    bool result=(a==b);
    result=(a!=b)||(b==a);
    
    
    bool c=true;
    bool d=false;
    result=(c<d);
    result=(c>d)||(c<d);
    
    char e='a';
    char f=e;
    result=(e!=f)||(e>=f);
    
    char* g="one";
    char* h="two";
    
    result=!(g==e);
    
}

