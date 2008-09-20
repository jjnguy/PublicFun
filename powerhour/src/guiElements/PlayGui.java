package guiElements;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Util;

@SuppressWarnings("serial")
public class PlayGui extends JFrame {
	
	private int SONG_AMNT;
	private List<File> songs;
	private JButton playPauseButton;
	private JLabel curSongLableLable;
	private JLabel cursongLable;
	private Process p;
	private int songCount;
	private String vlcLocation;
	private JLabel songcountLabel;
	private JLabel songCountLabelLabel;
	
	public PlayGui(List<File> songsP, int numberOfSongs) {
		super("Power Hour");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Util.setLookAndFeel();
		
		SONG_AMNT = numberOfSongs;
		songs = songsP;
		songCount = 0;
		
		addWindowListener(closeListener);
		
		songCountLabelLabel = new JLabel("Number of songs (Played / Left):");
		songcountLabel = new JLabel(songCount + " / " + (SONG_AMNT - songCount));
		
		cursongLable = new JLabel("None");
		curSongLableLable = new JLabel("Current Song:");
		
		playPauseButton = new JButton("Play");
		playPauseButton.addActionListener(playpauseAction);
		
		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		mainPane.add(curSongLableLable, gc);
		mainPane.add(cursongLable, gc);
		gc.gridy = 1;
		mainPane.add(songCountLabelLabel, gc);
		mainPane.add(songcountLabel, gc);
		gc.gridy = 2;
		mainPane.add(playPauseButton, gc);
		
		add(mainPane);
		pack();
		Util.moveToMiddle(this);
		setVisible(true);
	}
	
	private void shuffleSongs() {
		Random r = new Random();
		for (int i = 0; i < songs.size(); i++) {
			songs.add(r.nextInt(songs.size()), songs.remove(i));
		}
	}
	
	private ActionListener playpauseAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				PlayGui.this.play();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	private WindowAdapter closeListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			if (PlayGui.this.p != null)
				PlayGui.this.p.destroy();
		}
	};
	
	public void play() throws IOException {
		playPauseButton.setVisible(false);
		pack();
		
		shuffleSongs();
		TimerTask tt = new TimerTask() {
			private String drinkBitchPath = "drinkBitch.mp3";
			
			@Override
			public void run() {
				if (vlcLocation == null)
					return;
				if (songCount > SONG_AMNT)
					return;
				if (p != null)
					p.destroy();
				try {
					Process drinkBitch = Runtime.getRuntime().exec(new String[] { vlcLocation, "--volume=1024", drinkBitchPath });
					Thread.sleep(1000);
					p = Runtime.getRuntime().exec(new String[] { vlcLocation, songs.get(songCount).getAbsolutePath() });
					cursongLable.setText(songs.get(songCount).getName());
					pack();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				incrementSongcount();
			}
		};
		Timer t = new Timer();
		t.scheduleAtFixedRate(tt, 0, 60000);
	}
	
	private void incrementSongcount() {
		songCount++;
		songcountLabel.setText(songCount + " / " + (SONG_AMNT - songCount));
	}
	
	public void setVlcLocation(String text) {
		vlcLocation = text;
	}
}
