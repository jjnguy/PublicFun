public class PerfectNum {
	public static void main(String[] args) {
		int curNum = 4;
		int found = 0;
		// Loop until we found the fifth perfect number
		while (found != 5) {
			int factorSum = 0;
			int halfnum = curNum / 2 + 1;
			// divide the number by every number
			// below it up until half the number
			for (int i = 1; i < halfnum; i++) {
				// if we found the factor then add it to the total
				if (curNum % i == 0)
					factorSum += i;
				// if we are past the total, then we can exit
				if (factorSum > curNum)
					break;
			}
			if (factorSum == curNum) {
				found++;
				System.out.println(curNum);
			}
			// check only even numbers
			// if no one else has found any odd perfect numbers, neither will we
			curNum += 2;
		}
	}
}
