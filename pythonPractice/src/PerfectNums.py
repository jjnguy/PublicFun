import threading

def isPerfect(num):
    factorTotal = 0
    halfNum = num / 2 + 1
    for i in range(1, halfNum):
        if num % i == 0:
            factorTotal += i
        if factorTotal > num:
            break
    return factorTotal == num


class PerfNumThread(threading.Thread):
    startNum = 0
    def __init__(self, startNum):
        self.startNum = startNum
        threading.Thread.__init__(self)
    def run(self):
       curNum = self.startNum
       while True:
            if isPerfect(curNum):
                print "%i %s" % (curNum, ' is perfect')
            curNum += 4
            
th2 = PerfNumThread(6)
th1 = PerfNumThread(8)
th2.start()
th1.start()
        