#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    int c=-30;
    int d=10*20;
    int e=a*b/d+40*(-100-b)+c;
    e=e;
    printf("%s %d %d %d %d %d \n", "Result:", a, b, c, d, e);
    
}

