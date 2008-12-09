import java.util.Arrays;

public class SmallSort {

	/**
	 * Sorts the provided array
	 * 
	 * @param arr
	 * @return the sum of the numbers involved in swaps
	 */
	public static long sort(int[] arr) {
		return bubbleSort(arr);
	}

	public static long insertionSort(int[] arr) {
		long starttime = System.currentTimeMillis();
		// start with nothing sorted
		// incrementing 'i' signifies that everything before 'i' is sorted
		// start 'i' at 1 because the 0th element is sorted already
		for (int i = 1; i < arr.length; i++) {
			int value = arr[i];
			int pos = -1;
			// now we find the next place where the element at 'i'
			// should go (must be between 0 and i)
			for (int j = 0; j < i; j++) {
				// if the element at 'j' is larger than the element at 'i'
				// we have found our swaping spot
				if (arr[j] > value) {
					pos = j;
					break;
				}
			}
			// if pos = -1 then the element we are on is already sorted
			if (pos == -1)
				continue;
			// now we move everything right one spot to make room for the new val
			for (int j = i; j > pos; j--) {
				int temp = arr[j - 1];
				arr[j - 1] = arr[j];
				arr[j] = temp;
			}
			// last we place the value in the correct spot
			arr[pos] = value;
		}
		return System.currentTimeMillis() - starttime;
	}

	public static long selectionSort(int[] arr) {
		long starttime = System.currentTimeMillis();
		// loop from 0 to the length of out arr
		// everything from 0 to 'i' is kept sorted
		for (int i = 0; i < arr.length; i++) {
			// now we find the smallest value and put it into place
			int smallPos = i;
			// if the
			for (int j = i; j < arr.length; j++) {
				if (arr[j] < arr[smallPos]) {
					smallPos = j;
				}
			}
			// if small pos hasnt moved..then don't swap
			if (smallPos == i)
				continue;
			int temp = arr[i];
			arr[i] = arr[smallPos];
			arr[smallPos] = temp;
		}
		return System.currentTimeMillis() - starttime;
	}

	public static long bubbleSort(int[] arr) {
		long starttime = System.currentTimeMillis();
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					int temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
		return System.currentTimeMillis() - starttime;
	}

	public static boolean isSorted(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] > arr[i + 1])
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		long timeTakenCreatinglists = 0;
		long timeTakenCheckingSorted = 0;
		long startTime = System.currentTimeMillis();
		long inserTotal = 0;
		long selecTotal = 0;
		long bubleTotal = 0;
		int numTests = 10;
		int arrSize = 50000;
		int[] rndArr = new int[arrSize];
		for (int h = 0; h < numTests; h++) {
			long start = System.currentTimeMillis();
			for (int i = 0; i < rndArr.length; i++) {
				rndArr[i] = (int) (Math.random() * 20);
			}
			int[] arr = rndArr.clone();
			int[] arr2 = rndArr.clone();
			int[] arr3 = rndArr.clone();
			timeTakenCreatinglists += System.currentTimeMillis() - start;
			inserTotal += insertionSort(arr) / 1000.0;
			selecTotal += selectionSort(arr2) / 1000.0;
			bubleTotal += bubbleSort(arr3) / 1000.0;
			start = System.currentTimeMillis();
			if (!(isSorted(arr) && isSorted(arr) && isSorted(arr))) {
				System.out.println("Sort failed");
				break;
			}
			timeTakenCheckingSorted += System.currentTimeMillis() - start;
		}
		System.out.println("Insertion ave: " + inserTotal / (double) numTests);
		System.out.println("Selection ave: " + selecTotal / (double) numTests);
		System.out.println("Bubble ave: " + bubleTotal / (double) numTests);
		System.out.println("Total time: " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println("Creation time: " + timeTakenCreatinglists / 1000.0);
		System.out.println("Check time: " + timeTakenCheckingSorted / 1000.0);
	}
}
