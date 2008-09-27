
def isPerfect(num):
    factorTotal = 0
    halfNum = num / 2 + 1
    for i in range(1, halfNum):
        if num % i == 0:
            factorTotal+=i
        if factorTotal > num:
            break
    return factorTotal == num