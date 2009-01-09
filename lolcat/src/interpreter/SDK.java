package interpreter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

/*********************************************************
 * LolCode SDK for Matthew Hall's Lolcode java interpreter Hereby released under GPL by Eric
 * Bash 2007 requires ICanHasParser and Lolcode to run
 *********************************************************/

public class SDK extends JFrame {
	public final static String version = "0.5.0.0";
	private JTextArea editor = new JTextArea();
	private String codNam = "NAMURCOD!!1";
	private JLabel namLbl = new JLabel("COD IZ: " + codNam);

	public SDK() {
		super("LOLCODE SDK VRZN: " + version);

		JScrollPane scrollArea = new JScrollPane(editor);
		JButton compileButton = new JButton("MAKRUN");
		mahMenu lolMenu = new mahMenu(this);

		setJMenuBar(lolMenu);

		setLayout(new BorderLayout());

		add(namLbl, BorderLayout.NORTH);

		add(scrollArea, BorderLayout.CENTER);

		compileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compileNRun(editor.getText());
			}// KTHXBYE actionPerformed
		});

		add(compileButton, BorderLayout.SOUTH);

	}// KTHXBYE Constructor

	private static void compileNRun(String lolcode) {
		ICanHasParser parser = new ICanHasParser(lolcode);
		Lolcode runzNow = new Lolcode(parser.parse());
		runzNow.run();
	}

	private static void compileNSav(String lolcode, File oFile) {
		try {
			PrintWriter o = new PrintWriter(new FileOutputStream(oFile));
			ICanHasParser parseDat = new ICanHasParser(lolcode);
			String[] mahCode = parseDat.parse();
			for (int mahCounter = 0; mahCounter < mahCode.length; mahCounter++) {
				o.println(mahCode[mahCounter]);
			}// KTHX for
			o.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Kitty sez: lol wut file dat?");
			System.err.println("Kitty kant sav thar");
		}// KTHX catch
	}// KTHXBYE compileNSav

	private static void run(String[] parsedCode) {
		Lolcode makrun = new Lolcode(parsedCode);
		makrun.run();
	}

	private static void sav(File oFile, String mahCode) {
		try {
			PrintWriter o = new PrintWriter(new FileOutputStream(oFile));
			o.printf(mahCode);
			o.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Kitty sez: lol wut file dat?");
			System.err.println("Kitty kant sav thar");
		}// KTHX catch
	}// KTHXBYE sav

	public String gimmehCod() {
		return editor.getText();
	}

	public String gimmehNam() {
		return codNam;
	}

	public void urNamIz(String nam) {
		codNam = nam;
		namLbl.setText("COD IZ: " + codNam);
		namLbl.repaint();
	}

	public void urCodIz(String mahCode) {
		editor.setText(mahCode);
		editor.repaint();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			SDK frame = new SDK();

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.setSize(400, 500);

			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} else {
			File nFile = new File(args[0]);
			int numLinz = (int) (2.0f * (float) nFile.length() / 16.0f);
			String[] mahCode = new String[numLinz];
			Scanner n = new Scanner(args[0]);
			for (int mahCounter = 0; n.hasNextLine(); mahCounter++) {
				mahCode[mahCounter] = n.nextLine();
			}// KTHX for
			run(mahCode);
		}// KTHX else
	}// KTHXBYE main

	private class mahMenu extends JMenuBar {

		private final LCDFileFilter mahLCDFilter = new LCDFileFilter();
		private final LRNFileFilter mahLRNFilter = new LRNFileFilter();
		private JFileChooser fc = new JFileChooser(".");
		private SDK parent;
		private JFileChooser lrnfc = new JFileChooser(".");

		public mahMenu(SDK prnt) {
			parent = prnt;
			fc.setFileFilter(mahLCDFilter);
			lrnfc.setFileFilter(mahLRNFilter);

			// BTW File Menu Mak
			JMenu file = new JMenu("File");
			JMenuItem opin = file.add("Opin");
			JMenuItem sav = file.add("Sav");
			JMenuItem kthxbye = file.add("KTHXBYE");

			// BTW Mak File Menu Shot Web
			opin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (fc.getFileFilter() != mahLCDFilter)
						fc.setFileFilter(mahLCDFilter);
					int success = fc.showOpenDialog(null);
					if (success == JFileChooser.APPROVE_OPTION) {
						try {
							File opinFile = fc.getSelectedFile();
							parent.urNamIz(opinFile.getName());
							String urCod = "";
							Scanner n = new Scanner(opinFile);
							while (n.hasNextLine()) {
								urCod += n.nextLine() + "\n";
							}// KTHX while
							parent.urCodIz(urCod);
						} catch (FileNotFoundException e1) {
							System.err.println("Kitty sez: wut dat?");
							System.err.println("Kitty kant opin :'(");
						}// KTHX catch
					}// KTHX if
				}// KTHXBYE actionPerformed
			});
			sav.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int success = fc.showSaveDialog(null);
					if (success == JFileChooser.APPROVE_OPTION) {
						File savFile = fc.getSelectedFile();
						String filePath = savFile.getPath();
						if (!filePath.toLowerCase().endsWith(".lcd")) {
							filePath += ".lcd";
						}// KTHX if(fileName)
						try {
							SDK.sav(new File(filePath), parent.gimmehCod());
							parent.urNamIz(savFile.getName());
						} catch (Exception e1) {
							System.err.println("Kitty kant Sav frm menu :'(");
						}
					}// KTHX if(success)
				}// KTHXBYE actionPerformed
			});
			kthxbye.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}// KTHXBYE actionPerformed
			});

			// BTW MakCod Menu Mak
			JMenu makCod = new JMenu("MakCod");
			JMenuItem compileNSav = makCod.add("MakCodNSav");
			JMenuItem compileNRun = makCod.add("MakCodNRun");

			// BTW Mak MakCod Menu Shot Web
			compileNSav.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int success = lrnfc.showSaveDialog(null);
					if (success == JFileChooser.APPROVE_OPTION) {
						File savFile = lrnfc.getSelectedFile();
						String filePath = savFile.getPath();
						if (!filePath.toLowerCase().endsWith(".lrn")) {
							filePath += ".lrn";
						}// KTHX if(fileName)
						try {
							SDK.compileNSav(parent.gimmehCod(), new File(filePath));
						} catch (Exception e1) {
							System.err.println("Kitty kant Sav frm menu :'(");
						}// KTHX catch
					}// KTHX if(success)
				}// KTHXBYE actionPerformed
			});
			compileNRun.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SDK.compileNRun(parent.gimmehCod());
				}// KTHXBYE actionPerformed
			});

			// BTW LOL Needz Deez
			add(file);
			add(makCod);
		}// KTHXBYE constructor

		private class LCDFileFilter extends javax.swing.filechooser.FileFilter {
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".lcd");
			}// KTHXBYE accept

			public String getDescription() {
				return "*.lcd";
			}// KTHXBYE getDescription
		}// KTHXBYE LCDFileFilter

		private class LRNFileFilter extends javax.swing.filechooser.FileFilter {
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".lrn");
			}// KTHXBYE accept

			public String getDescription() {
				return "*.lrn";
			}// KTHXBYE getDescription
		}// KTHXBYE LCDFileFilter
	}// KTHXBYE mahMenu
}// KTHXBYE SDK