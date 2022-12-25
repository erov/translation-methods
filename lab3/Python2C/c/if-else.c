#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    char* a="";
    
    if(true){
        if(true){
            if(true){
                if(true){
                    if(false){
                        printf("%d \n", 5);
                        a="fifth if";
                    }
                }
                else{
                    printf("%d \n", 4);
                    a="fourth else";
                }
            }
        }
        else{
            printf("%d \n", 2);
            a="second else";
        }
    }
    printf("%s \n", "exit");
    
}

