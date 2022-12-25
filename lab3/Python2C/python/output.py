a = 10
b = True
c = 'c'
d = "sample_string"

magic = 42
a = magic
magic = 10

e = False
b = e
e = True

d = "another_string"
string_copy = d
string_copy = ""

c = 'k'
f = c
c = f

print(a)
print(a, b)
print(a, b, c)
print(a, b, c, d)
print(a, 10, b, False, c, 'c', d, "string")
