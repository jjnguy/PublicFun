'''
Created on Oct 4, 2009

@author: jnelson
'''

def first_zero_row(matrix):
    for i in range(len(matrix)):
        if isZeroRow(matrix[i]): return i
    return -1

def isZeroRow(row):
    for i in row:
        if i != 0: return False
    return True
