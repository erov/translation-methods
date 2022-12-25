#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    bool a=true;
    bool b=false;
    bool c=!a;
    bool d=!true;
    
    bool e=a&&b;
    bool f=a||b;
    bool g=(!e||!f&&!(a||b||c&&f))||a;
    
    printf("%d \n", g);
    
}

