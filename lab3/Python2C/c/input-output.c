#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    char str1[256];
    scanf("%s", str1);
    
    char* str2="string";
    str2[256];
    scanf("%s", str2);
    
    char* str3=str2;
    
    printf("%s \n", str1);
    printf("%s %s \n", str2, str1);
    printf("%s %s %s \n", str3, str2, str1);
    
    int a=47;
    int b;
    scanf("%d", &b);
    
    printf("%d \n", a);
    printf("%d \n", b);
    
    int c=b;
    a=c;
    
    printf("%d %d %d \n", c, a, b);
    
    
}

