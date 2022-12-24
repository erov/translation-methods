# Перевод с Python на Си

При работе использовал автоматический генератор анализаторов ANTLR.

Поддержанное подмножество языка:
* Объявление переменных следующих типов с проверкой на соответствие типов при повторном присваивании:
  * `int`;
  * `bool`;
  * `char`;
  * `string`.
* `Ввод` с консоли переменных типа string, int;
* `Вывод` в консоль переменных и значений вышеупомянутых типов (bool через каст к int);
* Арифметический выражения над типом int (`+`, `-`, `*`, `/`);
* Логические выражения над типом bool (`and`, `or`, `not`);
* Условный оператор `if` с `else` веткой. Допускается любое количество вложенностей в исходном коде на Python

## Пример трансляции
> Pyhon:
```
integer = 47
boolean = True
character = 'c'
string = "string"

integer_copy = integer
boolean_copy = boolean
character_copy = character
string_copy = string

input_int = int(input())
input_string = input()

print(integer, boolean, character_copy, string_copy)
print(input_int)

a = 10
b = a * 100 // 44 - 78 + (5 + 10)

if a != b:
    c = True
    d = False
    e = not c and (c or d) or not (c and d)
    if e == True:
        my_string = "str"
        if my_string == string:
            print("Wow!")
        else:
            print("Strings", my_string, "and",   string, "are not equals")
            if a <= b:
                f = b
                b = a
                a = f 
    else :
        print("Ooops..")

string = "Is there else-branch?"

```
![](/lab3/python.png)

> C
```
#include <stdbool.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int integer=47;
    bool boolean=true;
    char character='c';
    char* string="string";

    int integer_copy=integer;
    bool boolean_copy=boolean;
    char character_copy=character;
    char* string_copy=string;

    int input_int;
    scanf("%d", &input_int);
    char input_string[256];
    scanf("%s", input_string);

    printf("%d %d %c %s \n", integer, boolean, character_copy, string_copy);
    printf("%d \n", input_int);

    int a=10;
    int b=a*100/44-78+(5+10);

    if(a!=b){
        bool c=true;
        bool d=false;
        bool e=!c&&(c||d)||!(c&&d);
        if(e==true){
            char* my_string="str";
            if(my_string==string){
                printf("%s \n", "Wow!");
            }
            else{
                printf("%s %s %s %s %s \n", "Strings", my_string, "and", string, "are not equals");
                if(a<=b){
                    int f=b;
                    b=a;
                    a=f;
                }
            }
        }
        else{
            printf("%s \n", "Ooops..");

        }
    }
    string="Is there else-branch?";


}
```

![](/lab3/c.png)
