package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.SudoukuBoard;

public class SudokuFrame extends JFrame {

	private SudokuNumberArea[][] numbers;
	private JButton generateButton;
	private JButton loadButton;

	public SudokuFrame() {
		super("Judoku");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		numbers = new SudokuNumberArea[9][9];
		for (int r = 0; r < numbers.length; r++) {
			for (int c = 0; c < numbers[r].length; c++) {
				numbers[r][c] = new SudokuNumberArea();
			}
		}
		JPanel numberPane = new JPanel(new GridLayout(9, 9));
		for (int r = 0; r < numbers.length; r++) {
			for (int c = 0; c < numbers[r].length; c++) {
				SudokuNumberArea a = numbers[r][c];
				numberPane.add(a);
			}
		}
		JPanel mainPane = new JPanel();
		mainPane.add(numberPane);
		generateButton = new JButton("Save Puzzle");
		generateButton.addActionListener(generateAction);
		mainPane.add(generateButton);
		loadButton = new JButton("Load Board");
		loadButton.addActionListener(loadAction);
		mainPane.add(loadButton);
		add(mainPane);
		pack();
		setVisible(true);
	}

	public void save(File loc) {
		PrintStream out = null;
		try {
			out = new PrintStream(loc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int r = 0; r < numbers.length; r++) {
			for (int c = 0; c < numbers[r].length; c++) {
				out.print(numbers[r][c].getText());
				if (c != numbers[r].length - 1)
					out.print(",");
			}
			out.print("\n");
		}
		out.close();
	}

	public void load(File loc, boolean lockValues) {
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
		for (int r = 0; r < numbers.length; r++) {
			for (int c = 0; c < numbers[r].length; c++) {
				numbers[r][c].setText(nums[r][c]);
			}
		}
		fIn.close();
	}

	private int[][] getData() {
		int[][] ret = new int[9][9];
		for (int r = 0; r < numbers.length; r++) {
			for (int c = 0; c < numbers[r].length; c++) {
				String num = numbers[r][c].getText().trim();
				if (num.equals(""))
					num = "0";
				ret[r][c] = Integer.parseInt(num);
			}
		}
		return ret;
	}

	private ActionListener loadAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser choose = new JFileChooser();
			int choice = choose.showOpenDialog(SudokuFrame.this);
			if (choice == JFileChooser.CANCEL_OPTION)
				return;
			File f = choose.getSelectedFile();
			load(f, false);
		}
	};

	private ActionListener generateAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {			
			JFileChooser choose = new JFileChooser();
			int choice = choose.showSaveDialog(SudokuFrame.this);
			if (choice == JFileChooser.CANCEL_OPTION)
				return;
			File f = choose.getSelectedFile();
			save(f);
		}
	};

	public static void main(String[] args) {
		new SudokuFrame();
	}
}
