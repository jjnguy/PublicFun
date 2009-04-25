package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class WindowAbout extends JDialog {

	public WindowAbout(JFrame parent) {
		super(parent, "About CoLab");
		setLocation(new Point(parent.getLocation().x + 50, parent.getLocation().y + 50));
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setSize(300, 380);
		this.setResizable(false);
		setUpAppearance();
	}

	private void setUpAppearance() {
		ImageIcon logo = null;
		try {
			logo = new ImageIcon(ImageIO.read(new File("images/logo_1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel logoLabel = new JLabel(logo);
		logoLabel.setToolTipText("<html><font face=\"monospaced\">" +
				"    _.-\"\"\"-,<br>" +
				"  .'  ..::. `\\<br>" +
				" /  .::' `'` /<br>" +
				"/ .::' .--.=;<br>" +
				"| ::' /  C ..\\<br>" +
				"| :: |   \\  _.)<br>" +
				" \\ ':|   /  \\<br>" +
				"  '-, \\./ \\)\\)<br>" +
				"     `-|   );/<br>" +
				"        '--'-'</html>");
		//TODO logoLabel ToolTip
		JTextArea text = new JTextArea();
		text.setSize(280, 210);
		text.setEditable(false);
		text.setText("CoLab was brought to you by the letter C and \nby The Squirrels for "
				+ "their Computer Science 319 \nSemester Project. \n\nPlease Enjoy.");
		text.append("\n\nThe Squirrels:\n\tIan Dallas\n\tAmelia Gee\n\tJustin Nelson"
				+ "\n\tWayne Rowcliffe");
		text.setFont(new Font("SansSerif", Font.BOLD, 12));
		text.setForeground(new Color(0, 200, 100));
		text.setBackground(Color.DARK_GRAY);
		Insets borderInsets = new Insets(10, 10, 0, 10);
		logoLabel.setBorder(new EmptyBorder(borderInsets));
		text.setBorder(new EmptyBorder(borderInsets));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.add(logoLabel, BorderLayout.NORTH);
		mainPanel.add(text, BorderLayout.CENTER);
		add(mainPanel);
	}

	public static void showAboutWindow(JFrame parent) {
		WindowAbout about = new WindowAbout(parent);
		about.setVisible(true);
	}
}
