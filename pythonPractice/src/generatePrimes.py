from time import clock
from math import sqrt

# generate all primes between 2 and lim (exclusive)
def listPrimes(lim):
    if lim > 2:
        yield 2
    else:
        return
    # loop through the desired range
    for num in xrange(3, lim, 2):
        numroot = int(sqrt(num)) + 1
        # determine the primality of the number through brute-force division
        for i in xrange(3, numroot, 2):
            if not num % i:
                break
        else:
            yield num

primes = open("primes", 'w') 
start_time = clock()        
for num in listPrimes(1000):
    primes.write("%s\n" % num)
total_time = clock() - start_time
print total_time