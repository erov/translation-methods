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
* Арифметический выражения над типом int (`+`, `-`, `*`, `//`);
* Логические выражения над типом bool (`and`, `or`, `not`);
* Операции сравнения для всех типов (`==`, `!=`, `<`, `<=`, `>`, `>=`);
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


## Тестирование
```
$ ./Python2C/gradlew clean build test

> Task :test

Python2CTests > test_09_simpleIf() STANDARD_ERROR
    Success of compiling file 'c/simple-if.c'!

Python2CTests > test_09_simpleIf() PASSED

Python2CTests > test_04_outputPrinting() STANDARD_ERROR
    Success of compiling file 'c/output.c'!

Python2CTests > test_04_outputPrinting() PASSED

Python2CTests > test_05_inputOutput() STANDARD_ERROR
    Success of compiling file 'c/input-output.c'!

Python2CTests > test_05_inputOutput() PASSED

Python2CTests > test_12_ifElse() STANDARD_ERROR
    Success of compiling file 'c/if-else.c'!

Python2CTests > test_12_ifElse() PASSED

Python2CTests > test_07_boolExpression() STANDARD_ERROR
    Success of compiling file 'c/bool-expression.c'!

Python2CTests > test_07_boolExpression() PASSED

Python2CTests > test_01_primitivesDeclaration() STANDARD_ERROR
    Success of compiling file 'c/primitives.c'!

Python2CTests > test_01_primitivesDeclaration() PASSED

Python2CTests > test_06_intExpression() STANDARD_ERROR
    Success of compiling file 'c/int-expression.c'!

Python2CTests > test_06_intExpression() PASSED

Python2CTests > test_08_comparison() STANDARD_ERROR
    Success of compiling file 'c/comparison.c'!

Python2CTests > test_08_comparison() PASSED

Python2CTests > test_11_ifReentrancy() STANDARD_ERROR
    Success of compiling file 'c/if-reentrancy.c'!

Python2CTests > test_11_ifReentrancy() PASSED

Python2CTests > test_10_complicatedIf() STANDARD_ERROR
    Success of compiling file 'c/complicated-if.c'!

Python2CTests > test_10_complicatedIf() PASSED

Python2CTests > test_02_assignment() STANDARD_ERROR
    Success of compiling file 'c/assignment.c'!

Python2CTests > test_02_assignment() PASSED

Python2CTests > test_03_inputReading() STANDARD_ERROR
    Success of compiling file 'c/input.c'!

Python2CTests > test_03_inputReading() PASSED

Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See https://docs.gradle.org/7.4/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 2s
6 actionable tasks: 6 executed

```
