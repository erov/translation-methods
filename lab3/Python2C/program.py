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

