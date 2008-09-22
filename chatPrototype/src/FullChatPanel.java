import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class FullChatPanel extends JPanel implements ChatInterface {

	private JTextArea conversation, outgoingMesages;
	private JScrollPane recievedScroller, outgoingScroller;
	private JSplitPane incomingAndOutgoingSplit;

	private List<String> outgoingMessageBuffer;

	private JButton send;

	public FullChatPanel() {
		super(new GridBagLayout());
		incomingAndOutgoingSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		conversation = new JTextArea();
		outgoingMesages = new JTextArea();
		recievedScroller = new JScrollPane(conversation);
		outgoingScroller = new JScrollPane(outgoingMesages);
		outgoingScroller.setPreferredSize(new Dimension(300, 150));
		recievedScroller.setPreferredSize(new Dimension(300, 150));
		incomingAndOutgoingSplit.add(recievedScroller);
		incomingAndOutgoingSplit.add(outgoingScroller);
		incomingAndOutgoingSplit.setResizeWeight(.5);
		send = new JButton("Send");
		send.addActionListener(sendAction);
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		add(incomingAndOutgoingSplit, gc);
		gc.weightx = gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.BASELINE_TRAILING;
		add(send, gc);
	}

	public void newMessage(String text, String userName) {
		conversation.append("\n");
		conversation.append(FullChatPanel.timestamp(System.currentTimeMillis()));
		conversation.append(text);
	}

	/**
	 * 
	 * @return the mesage to be sent
	 */
	public String getMessage() {
		if (outgoingMessageBuffer.isEmpty())
			throw new NoSuchElementException();
		return outgoingMessageBuffer.remove(0);
	}

	private static String timestamp(long currentTimeMillis) {
		return getTimeStringFromMiliseconds(currentTimeMillis) + " : ";
	}

	/**
	 * Simple way to turn miliseconds into the current date
	 * 
	 * @param secondsP
	 *            Miliseconds since Jan 1 1970, or something like that
	 * @return a string representing the current date
	 */
	public static String getTimeStringFromMiliseconds(long secondsP) {
		int seconds = (int) (secondsP / 1000);
		double year = seconds / 60.0 / 60 / 24.0 / 365;
		double day = (year - Math.floor(year)) * 365;
		double hour = (day - Math.floor(day)) * 24;
		double min = (hour - Math.floor(hour)) * 60;
		double sec = (min - Math.floor(min)) * 60;
		return (hour < 10 ? "0" : "") + (int) hour + ":" + (min < 10 ? "0" : "")
				+ (int) min + ":" + (Math.round(sec) < 10 ? "0" : "") + Math.round(sec);
	}

	@Override
	public boolean hasMesageToSend() {
		return !outgoingMessageBuffer.isEmpty();
	}

	@Override
	public void saveConversation(String location) {
		JFileChooser choose = new JFileChooser();
		int choice = choose.showSaveDialog(this);
		if (choice == JFileChooser.CANCEL_OPTION)
			return;
		File f = choose.getSelectedFile();

		PrintStream out = null;
		try {
			out = new PrintStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(conversation.getText());
	}

	private ActionListener sendAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (outgoingMesages.getText().trim().equals(""))
				return;
			newMessage(outgoingMesages.getText(), "you");
			outgoingMessageBuffer.add(outgoingMesages.getText());
			outgoingMesages.setText("");
		}
	};

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(new FullChatPanel());
		f.pack();
		f.setVisible(true);
	}
}
