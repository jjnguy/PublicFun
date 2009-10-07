'''
Created on Oct 4, 2009

@author: jnelson
'''
from Program import Program

def fib(n):
    o = 0;
    t = 1;
    for i in range(n):
        yield t
        o, t = t, o + t

if __name__ != '__main__':
    exit

p = Program("Program.py")
p.parse_file()