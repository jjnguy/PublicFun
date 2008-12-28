package logic;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SudoukuBoard {

	private int[][] slots;
	private List<Point> locks;

	public SudoukuBoard() {
		slots = new int[9][9];
		locks = new ArrayList<Point>();
	}

	public SudoukuBoard(int[][] nums) {
		// TODO error checking
		slots = nums;
		locks = new ArrayList<Point>();
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param number
	 * @return true if the placement was successful (not oob or illegal move), false otherwise
	 */
	public boolean place(int row, int col, int num, boolean lock) {
		if (num > 9 || num < 1)
			return false;
		// TODO error checking and return bool if move is allowed.
		if (getPieceAt(row, col) != 0)
			return false;
		boolean isValid = isValidPlacement(row, col, num);
		if (!isValid)
			return false;
		slots[row][col] = num;
		if (lock) {
			locks.add(new Point(row, col));
		}
		return true;
	}

	/**
	 * The default place locks a piece. This is for creating puzzles.
	 * 
	 * @param row
	 * @param col
	 * @param num
	 * @return
	 */
	public boolean place(int row, int col, int num) {
		return place(row, col, num, true);
	}

	private boolean isValidPlacement(int row, int col, int num) {
		if (num == 0)
			return true;
		boolean isValidRow = numInRowCount(row, num) == 0;
		boolean isValidCol = numInColCount(col, num) == 0;
		int boxNum = getBoxNum(row, col);
		if (boxNum == -1)
			return false;
		boolean isValidBox = numInBoxCount(boxNum, num) == 0;
		return isValidBox && isValidCol && isValidRow;
	}

	private int getBoxNum(int row, int col) {
		int boxNum = -1;
		if (row == 0 || row == 1 || row == 2) {
			if (col == 0 || col == 1 || col == 2)
				boxNum = 0;
			if (col == 3 || col == 4 || col == 5)
				boxNum = 1;
			if (col == 6 || col == 7 || col == 8)
				boxNum = 2;
		}
		if (row == 3 || row == 4 || row == 5) {
			if (col == 0 || col == 1 || col == 2)
				boxNum = 3;
			if (col == 3 || col == 4 || col == 5)
				boxNum = 4;
			if (col == 6 || col == 7 || col == 8)
				boxNum = 5;
		}
		if (row == 6 || row == 7 || row == 8) {
			if (col == 0 || col == 1 || col == 2)
				boxNum = 6;
			if (col == 3 || col == 4 || col == 5)
				boxNum = 7;
			if (col == 6 || col == 7 || col == 8)
				boxNum = 8;
		}
		return boxNum;
	}

	private int numInColCount(int col, int num) {
		int count = 0;
		for (int row = 0; row < slots.length; row++) {
			if (slots[row][col] == num) {
				count++;
			}
		}
		return count;
	}

	private int numInRowCount(int row, int num) {
		int count = 0;
		for (int col = 0; col < slots[0].length; col++) {
			if (slots[row][col] == num) {
				count++;;
			}
		}
		return  count;
	}

	/**
	 * 
	 * @param boxNum
	 *            boxes are labeled from left to right, and top to bottom, 0 - 8
	 * @param num
	 * @return
	 */
	private int numInBoxCount(int boxNum, int num) {
		if (boxNum == 0) {
			return numInRange(0, 2, 0, 2, num);
		}
		if (boxNum == 1) {
			return numInRange(0, 2, 3, 5, num);
		}
		if (boxNum == 2) {
			return numInRange(0, 2, 6, 8, num);
		}
		if (boxNum == 3) {
			return numInRange(3, 5, 0, 2, num);
		}
		if (boxNum == 4) {
			return numInRange(3, 5, 3, 5, num);
		}
		if (boxNum == 5) {
			return numInRange(3, 5, 6, 8, num);
		}
		if (boxNum == 6) {
			return numInRange(6, 8, 0, 2, num);
		}
		if (boxNum == 7) {
			return numInRange(6, 8, 3, 5, num);
		}
		if (boxNum == 8) {
			return numInRange(6, 8, 6, 8, num);
		}
		return 0;
	}

	/**
	 * 
	 * @param rowS
	 *            the starting row
	 * @param rowE
	 *            the ending row (inclusive)
	 * @param colS
	 *            the starting col
	 * @param colE
	 *            the ending col (inclusive)
	 * @return
	 */
	private int numInRange(int rowS, int rowE, int colS, int colE, int num) {
		int count = 0;
		for (int row = rowS; row <= rowE; row++) {
			for (int col = colS; col <= colE; col++) {
				if (slots[row][col] == num) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Fills in every square with the solution
	 * 
	 * @return if there was a possible solution
	 */
	public boolean solve() {
		return solve(0, 0);
	}

	private boolean solve(int r, int c) {
		if (locks.contains(new Point(r, c))) {
			int nextRow = r;
			int nextCol = c + 1;
			if (nextCol > 8) {
				nextCol = 0;
				nextRow++;
				if (nextRow > 8)
					return true;
			}
			return solve(nextRow, nextCol);
		}
		boolean canPlace = false;
		for (int num = 1; num < 10; num++) {
			canPlace = place(r, c, num, false);
			if (canPlace) {
				// find next row
				int nextRow = r;
				int nextCol = c + 1;
				if (nextCol > 8) {
					nextCol = 0;
					nextRow++;
					if (nextRow > 8)
						return true;
				}
				boolean nextPossible = solve(nextRow, nextCol);
				if (!nextPossible) {
					slots[r][c] = 0; // reset so place doesn't fail
					continue;
				} else
					return true;
			}
		}
		return false;
	}

	public int[][] getBoard() {
		return slots;
	}

	public int getPieceAt(int row, int col) {
		return slots[row][col];
	}

	public boolean solved() {
		return isValidBoard() && isFull();
	}

	private boolean isFull() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (slots[row][col] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for (int[] row : slots) {
			ret.append(Arrays.toString(row));
			ret.append("\n");
		}
		return ret.toString();
	}

	/**
	 * @return true if there are no invalid numbers placed, false otherwise
	 */
	public boolean isValidBoard() {
		// TODO probalby need to do some actual work here
		return true;
	}

	public static void main(String[] args) throws FileNotFoundException {
		SudoukuBoard b = new SudoukuBoard(loadFile(new File("sdfg.txt")));
		System.out.println(b);
		System.out.println("\n\n\n");
		System.out.println(b.solve());
		System.out.println(b);
	}

	public static int[][] loadFile(File loc) throws FileNotFoundException {
		Scanner fIn = null;
		try {
			fIn = new Scanner(loc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int linenum = 0;
		String[][] nums = new String[9][9];
		while (fIn.hasNextLine()) {
			nums[linenum] = fIn.nextLine().split(",", -1);
			linenum++;
		}
		int[][] ret = new int[9][9];
		for (int r = 0; r < ret.length; r++) {
			for (int c = 0; c < ret[r].length; c++) {
				String s = nums[r][c].trim();
				if (s.equals(""))
					s = "0";
				ret[r][c] = Integer.parseInt(s);
			}
		}
		return ret;
	}
}
