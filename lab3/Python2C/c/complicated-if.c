#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    char d[256];
    scanf("%s", d);
    
    if((a==b)||(10+30==a+b+10)&&("test"!=d)){
        printf("%s \n", "Equal");
    }
    
    bool c=(a<b);
    if((c==true)&&!(d=="comparison")){
        printf("%s %d \n", "Less one: ", a);
    }
    else{
        printf("%s %d \n", "Less one: ", b);
    }
    
}

