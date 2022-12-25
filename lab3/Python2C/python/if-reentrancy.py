a = 10
b = 20
c = 30

if a + b == c:
    if a < b:
        if c + b >= a:
            print("ogo")
            a = 5
        else :
            print("fail")
            d = input()
            print("Message: ", d)
        v = 47 * 47
    else:
        print(a, "is ge than", b)
        tmp = a
        a = b
        b = tmp
print(a, b)
