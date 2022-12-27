#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int a=10;
    int b=20;
    int c=30;
    int d=40;
    int a_1=a;
    int b_1=b;
    int c_1=c;
    int d_1=d;
    a=d_1;
    b=c_1;
    c=b_1;
    d=a_1;
    
    bool tr=true;
    bool fls=false;
    bool tr_1=tr;
    bool fls_1=fls;
    tr=fls_1;
    fls=tr_1;
    
    char x='x';
    char y='y';
    char z='z';
    char x_1=x;
    char y_1=y;
    char z_1=z;
    x=x_1;
    y=z_1;
    z=y_1;
    
    char* str1="string_1";
    char* str2="string_2";
    
    char* str1_1=str1;
    char* str2_1=str2;
    str1=str2_1;
    str2=str1_1;
    
}

