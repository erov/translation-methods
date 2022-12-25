#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    
    if(a==b){
        printf("%s \n", "Equal");
    }
    else{
        printf("%s \n", "Non-equal");
    }
    
    bool c=(a<b);
    if(c==true){
        printf("%s %d \n", "Less one: ", a);
    }
    else{
        printf("%s %d \n", "Less one: ", b);
    }
    
}

