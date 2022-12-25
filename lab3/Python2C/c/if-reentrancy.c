#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    int c=30;
    
    if(a+b==c){
        if(a<b){
            if(c+b>=a){
                printf("%s \n", "ogo");
                a=5;
            }
            else{
                printf("%s \n", "fail");
                char d[256];
                scanf("%s", d);
                printf("%s %s \n", "Message: ", d);
            }
            int v=47*47;
        }
        else{
            printf("%d %s %d \n", a, "is ge than", b);
            int tmp=a;
            a=b;
            b=tmp;
        }
    }
    printf("%d %d \n", a, b);
    
}

