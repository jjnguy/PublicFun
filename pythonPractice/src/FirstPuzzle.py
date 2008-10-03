import math

# recursively computes the factors of a number
def factors(num):
    factorList = []
    numroot = int(math.sqrt(num)) + 1
    numleft = num
    # brute force divide the number until you find a factor
    for i in xrange(2, numroot):
        if num % i == 0:
            # if we found a factor, add it to the list and compute the remainder
            factorList.append(i)
            numleft = num / i
            break
    else: 
        # if we didn't find a factor, get out of here!
        factorList.append(num)
        return factorList
    # now recursively find the rest of the factors
    restFactors = factors(numleft)
    factorList.extend(restFactors)

    return factorList

# grabs  all of the twos in the list and puts them into 2 ^ x form
def transform_factor_list(factor_list):
    num2s = 0
    # remove all twos, counting them as we go
    old_size = len(factor_list)
    factor_list = [f for f in factor_list if f != 2]
    num2s = old_size - len(factor_list)
    # simply return the list with the 2's back in the right spot
    if num2s == 0: 
        return factor_list
    if num2s == 1:
        return [2] + factor_list
    return ['2 ^ %s' % num2s] + factor_list
        
print transform_factor_list(factors(17907120))