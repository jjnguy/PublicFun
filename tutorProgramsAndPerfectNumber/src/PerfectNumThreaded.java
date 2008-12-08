public class PerfectNumThreaded {
	public static int found = 0;
	public static int numthreads = 0;

	public static void main(String[] args) throws Exception {
		int curNum = 4;
		// Loop until we found the fifth perfect number
		while (found != 5) {
			if (numthreads < 3) {
				PerfectNumThread th = new PerfectNumThread(curNum);
				th.start();
				numthreads++;
				curNum += 2;
			}
		}
	}

	public static boolean isPerfect(int num) {
		int factorSum = 0;
		int halfnum = num / 2 + 1;
		// divide the number by every number
		// below it up until half the number
		for (int i = 1; i < halfnum; i++) {
			// if we found the factor then add it to the total
			if (num % i == 0)
				factorSum += i;
			// if we are past the total, then we can exit
			if (factorSum > num)
				break;
		}
		return factorSum == num;
	}
}

class PerfectNumThread extends Thread {

	int num;

	public PerfectNumThread(int num) {
		this.num = num;
	}

	@Override
	public void run() {
		if (PerfectNumThreaded.isPerfect(num)) {
			
			/*
			return break continue goto protected boolean for while do if else char 
			class enum interface extends implements this super try catch throws throw 
			abstract float double assert switch case default finally package import 
			null instanceof strictfp transient native const new true false null 
			*/
			
			PerfectNumThreaded.found++;
			System.out.println(num);
		}
		PerfectNumThreaded.numthreads--;
	}
}
