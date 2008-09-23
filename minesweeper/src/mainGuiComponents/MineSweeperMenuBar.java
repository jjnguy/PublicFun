package mainGuiComponents;

import genericComponents.Difficulty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MineSweeperMenuBar extends JMenuBar {

	private MinesweeperGame owner;


	public MineSweeperMenuBar(MinesweeperGame owner) {
		super();

		this.owner = owner;

		JMenu difficultyMenu = new JMenu("Difficulty");
		JMenuItem easyDifficultyItem = new JMenuItem("Easy");
		JMenuItem intermediateDifficultyItem = new JMenuItem("Intermediate");
		JMenuItem expertDifficultyItem = new JMenuItem("Expert");
		easyDifficultyItem.addActionListener(easy);
		intermediateDifficultyItem.addActionListener(intermediate);
		expertDifficultyItem.addActionListener(hard);
		difficultyMenu.add(easyDifficultyItem);
		difficultyMenu.add(intermediateDifficultyItem);
		difficultyMenu.add(expertDifficultyItem);

		JMenu fileMenu = new JMenu("File");
		JMenuItem newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(owner.newGameAction);
		fileMenu.add(newGameItem);
		JMenuItem highScoresMenuItem = new JMenuItem("High Scores");
		highScoresMenuItem.addActionListener(highscoresAction);
		fileMenu.add(highScoresMenuItem);
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		add(fileMenu);
		add(difficultyMenu);
	}

	private ActionListener easy = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			owner.changeDifficulty(Difficulty.EASY);
		}
	};

	private ActionListener intermediate = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			owner.changeDifficulty(Difficulty.INTERMEDIATE);
		}
	};

	private ActionListener hard = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			owner.changeDifficulty(Difficulty.EXPERT);
		}
	};
	private ActionListener highscoresAction = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog highscores = new JDialog(owner);
			JTextArea scores = new JTextArea(owner.getHighScores());
			scores.setEditable(false);
			highscores.getContentPane().add(scores);
			highscores.pack();
			highscores.setModal(true);
			highscores.setVisible(true);
		}};
}
