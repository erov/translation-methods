a = True
b = False
c = not a
d = not True

e = a and b
f = a or b
g = (not e or not f and not (a or b or c and f)) or a

print(g)
