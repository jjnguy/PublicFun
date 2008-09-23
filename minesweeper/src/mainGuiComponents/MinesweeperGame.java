package mainGuiComponents;

import genericComponents.CounterPanel;
import genericComponents.Difficulty;
import genericComponents.HighScoreList;
import genericComponents.TimeCounter;
import genericComponents.Util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MinesweeperGame extends JFrame {

	public static final String configFileLocation = ".sweepConf";

	public static final int LOSE = 1;
	public static final int WIN = 0;

	private JButton newGame;
	private Board board;
	private JPanel mainPane;
	private int mineNumber;
	private CounterPanel mines;
	private TimeCounter timer;
	private GridBagConstraints gc;

	private Difficulty curDifficulty;

	private HighScoreList highScores;

	public MinesweeperGame(Difficulty d) {
		super("Java-Sweeper");
		curDifficulty = d;
		highScores = new HighScoreList(new File(
				MinesweeperGame.configFileLocation));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mineNumber = d.mines();
		board = new Board(d.width(), d.height(), mineNumber);
		board.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(Board.WINLOSE_PROP)) {
					if (evt.getNewValue().equals(Board.WIN)) {
						endGame(WIN);
					} else if (evt.getNewValue().equals(Board.LOSE)) {
						endGame(LOSE);
					}
				} else if (evt.getPropertyName().equals(Board.FLAGCHANGE)) {
					mines.setValue(mineNumber - (Integer) evt.getNewValue());
				} else if (evt.getPropertyName().equals(
						Board.HAS_UNCOVERED_CELLS)) {
					if (evt.getNewValue().equals(
							new Boolean(true)
									&& evt.getOldValue().equals(
											new Boolean(false)))) {
						timer.start();
					}
				}
			}
		});

		timer = new TimeCounter(TimeCounter.UP_MODE);

		setJMenuBar(new MineSweeperMenuBar(this));

		mines = new CounterPanel(mineNumber);

		newGame = new JButton("New Game");
		newGame.addActionListener(newGameAction);
		mainPane = new JPanel(new GridBagLayout());

		gc = new GridBagConstraints();
		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.BASELINE_LEADING;
		mainPane.add(mines, gc);
		gc.gridx++;
		gc.anchor = GridBagConstraints.CENTER;
		mainPane.add(newGame, gc);
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		gc.gridx++;
		mainPane.add(timer, gc);
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 3;
		mainPane.add(board, gc);
		add(mainPane);
		setResizable(false);
		addWindowListener(windowListen);
		pack();
		Util.moveToMiddle(this);
	}

	public void play() {
		setVisible(true);
	}

	public ActionListener newGameAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			timer.reset();
			mines.setValue(curDifficulty.mines());
			board.setEnabled(true);
			newGame.setText("New Game");
			board.reset(curDifficulty);
		}
	};

	public static void main(String[] args) {
		MinesweeperGame mine = new MinesweeperGame(Difficulty.EASY);
		mine.play();
	}

	private void endGame(int win_lose) {
		timer.stop();
		if (win_lose == LOSE) {
			newGame.setText("Game Ovr");
			board.uncoverAll();
			board.setEnabled(false);
		} else if (win_lose == WIN) {
			newGame.setText("You WIN!");
			board.setEnabled(false);
			if (highScores.isHighScore(timer.getValue(), curDifficulty)) {
				String name = JOptionPane.showInputDialog("Please enter your name");
				updateHighScoreList(name, timer.getValue());
			}
		}
	}

	public void changeDifficulty(Difficulty d) {
		curDifficulty = d;
		newGame.doClick();
		pack();
		Util.moveToMiddle(this);
	}

	private void updateHighScoreList(String name, int value) {
		highScores.addHighScore(name, value, curDifficulty);
	}

	private WindowListener windowListen = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			highScores
					.writeToFile(new File(MinesweeperGame.configFileLocation));
		}
	};

	public String getHighScores() {
		return highScores.getTable();
	}
}
