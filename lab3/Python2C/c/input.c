#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    char str1[256];
    scanf("%s", str1);
    
    char* str2="string";
    str2[256];
    scanf("%s", str2);
    
    char* str3=str2;
    
    int a=47;
    int b;
    scanf("%d", &b);
    
    int c=b;
    a=c;
    
}

