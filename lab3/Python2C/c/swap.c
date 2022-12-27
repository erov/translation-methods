#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    int c=30;
    int a_1=a;
    int b_1=b;
    a=b_1;
    b=a_1;
    
    a_1=a;
    b_1=b;
    a=c;
    b=a_1;
    
}

