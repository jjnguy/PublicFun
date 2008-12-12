package turtledisplay;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import TurtleInterpreter.IllegalStatementException;
import TurtleInterpreter.LanguageParser;

public class TurtleFrame extends JFrame {
	public static ErrorWindow errorlog;
	static {
		errorlog = new ErrorWindow();
	}
	private TurtlePane pane;
	private JTextArea textPane;
	private LanguageParser parse;

	public TurtleFrame() {
		super("Etch-A-Sketch");
		// ----->>>
		setJMenuBar(getMenu());
		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pane = new TurtlePane(new Dimension(300, 300));
		main.add(pane);
		textPane = new JTextArea();
		textPane.setFont(new Font("Courier New", Font.PLAIN, 12));
		textPane.setTabSize(3);
		textPane.addKeyListener(crlS);
		JScrollPane scrol = new JScrollPane(textPane);
		scrol.setPreferredSize(pane.getPreferredSize());
		main.add(scrol);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(main);
		pack();
		setVisible(true);
	}

	private JMenuBar getMenu() {
		JMenuBar ret = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem run = new JMenuItem("Run");
		run.addActionListener(runAction);
		file.add(run);
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(saveAction);
		file.add(save);
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(loadAction);
		file.add(load);
		ret.add(file);
		return ret;
	}

	Dog dog = new Shoe();

	private ActionListener runAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File f = null;
			try {
				f = File.createTempFile("turt", "tur");
			} catch (IOException e2) {
				e2.printStackTrace();
				return;
			}
			PrintStream out = null;
			try {
				out = new PrintStream(f);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			}
			String[] text = textPane.getText().split("\n");
			for (String string : text) {
				out.println(string);
			}
			out.close();
			try {
				parse = new LanguageParser(f);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return;
			}
			try {
				pane.setExecutable(parse.parseFile());
			} catch (IllegalStatementException e2) {
				TurtleFrame.errorlog.writeException(e2);
				TurtleFrame.errorlog.setVisible(true);
			}
		}
	};

	private ActionListener loadAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser choose = new JFileChooser(new File(System
					.getProperty("user.dir")));
			int choice = choose.showOpenDialog(TurtleFrame.this);
			if (choice == JFileChooser.CANCEL_OPTION)
				return;
			Scanner in = null;
			try {
				in = new Scanner(choose.getSelectedFile());
			} catch (FileNotFoundException e1) {
				return;
			}
			textPane.setText("");
			while (in.hasNextLine()) {
				textPane.append(in.nextLine() + "\n");
			}
			in.close();
		}
	};

	private ActionListener saveAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser choose = new JFileChooser(new File(System
					.getProperty("user.dir")));
			int choice = choose.showSaveDialog(TurtleFrame.this);
			if (choice == JFileChooser.CANCEL_OPTION)
				return;
			PrintStream out = null;
			try {
				out = new PrintStream(choose.getSelectedFile());
			} catch (FileNotFoundException e1) {
				return;
			}
			String[] text = textPane.getText().split("\n");
			for (String string : text) {
				out.println(string);
			}
			out.close();
		}
	};
	
	

	private KeyListener crlS = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int modifiers = e.getModifiersEx();
			if (modifiers == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_S) {
				saveAction.actionPerformed(null);
			} else if (modifiers == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_O) {
				loadAction.actionPerformed(null);
			} else if (modifiers == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_R) {
				runAction.actionPerformed(null);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	};

	public static void main(String[] args) throws FileNotFoundException {
		new TurtleFrame();
	}
}
