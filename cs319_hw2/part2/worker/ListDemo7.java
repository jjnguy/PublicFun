package worker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Demo using a JList with a custom implementation of ListModel. The model
 * encapsulates the actual data, and when the data is changed the JList is
 * notified.
 * <p>
 * In this version there is an "Add" button that starts up a time-consuming
 * lookup task that is performed in the background using a SwingWorker. An
 * animated progress bar is shown while the background work is being done, and
 * the user has the opportunity to cancel the task.
 */
@SuppressWarnings("serial")
public class ListDemo7 extends JFrame {

	// The widgets!!!
	private JButton deleteButton;
	private JButton addButton;
	private JTextField addField;
	private JCheckBox checkBox;
	private JPanel panel;
	private JList list;
	private JTextArea textArea;

	private JPanel bottomPanel;
	private JProgressBar progressBar;
	private JButton cancelButton;
	// End the widgets!!

	// A worker for performing the lookup in the background
	private SwingWorker<Integer, StackOverflowError> worker;
	// Remember, we ignore the second generic parameter

	// The actual data is stored in this custom
	// instance of ListModel
	private DemoModel model;

	/**
	 * Constructor creates all components that will be contained in this frame,
	 * but does not set the size or attempt to make itself visible.
	 * 
	 * @param initialData
	 *            data with which the list is populated when first displayed
	 */
	public ListDemo7(List<StaffData> initialData) {
		super("Some List Demo Thing");
		ListDemo7.setLookAndFeel();
		// Actual construction begins here. Except for the
		// construction of the JList, the code is the same
		// as in the previous ListDemo

		// Create the model that contains the actual
		// data
		if (initialData != null) {
			model = new DemoModel(initialData);
		} else {
			model = new DemoModel(new ArrayList<StaffData>());
		}

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a list box with the underlying model.
		// The list box will add itself as a listener
		// to be notified of changes in the model's data.
		model.sortUp();
		list = new JList(model);

		// Allow only one list item to be selected at a time
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Initially the list comes up with the first item selected
		list.setSelectedIndex(0);

		// Start out with vertical space for four items
		list.setVisibleRowCount(6);

		// Create the selection listener
		list.addListSelectionListener(this.listSelL);
		list.setPreferredSize(new Dimension(300, 100));

		// panel to contain the checkbox and Delete button
		panel = new JPanel();

		// check box initially checked
		checkBox = new JCheckBox("sort ascending");
		checkBox.setSelected(true);
		checkBox.addActionListener(this.checkBoxListener);
		panel.add(checkBox);

		// the Delete button
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(deleteActionL);
		panel.add(deleteButton);

		// Add button
		addButton = new JButton("Add");
		addButton.addActionListener(addActionL);
		panel.add(addButton);
		addField = new JTextField(15);
		panel.add(addField);

		// textArea for display of full data for the highlighted
		// item in the list. Make sure it is at least 25 columns wide.
		// Display initial data from element 0 because we set up the
		// JList to start with item 0 selected
		textArea = new JTextArea(10, 40);
		textArea.setEditable(false);
		textArea.setText(model.getElement(0).toString());

		// progress bar and cancel button
		progressBar = new JProgressBar(0, 1000);
		progressBar.setIndeterminate(false);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(cancelActionL);
		bottomPanel = new JPanel();
		bottomPanel.add(progressBar);
		bottomPanel.add(cancelButton);

		// Put the list box and the text area in scroll
		// panes, and put them both in a JSplitPane.
		// The preferred width of the list will
		// set the initial position of the divider
		// for the JSplitPane.
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		JScrollPane scroll1 = new JScrollPane(list);
		JScrollPane scroll2 = new JScrollPane(textArea);
		splitPane.setLeftComponent(scroll1);
		splitPane.setRightComponent(scroll2);

		// add panel as the NORTH component and use
		// CENTER for the JSplitPane to take up
		// all remaining space
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		// bottom panel for progress/cancellation initially invisible
		bottomPanel.setVisible(false);
	}

	/**
	 * Sets the look and feel of an application to that of the system it is
	 * running on. (Java's default looks bad)
	 */
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}
	}

	/**
	 * Entry point. This method should normally do nothing except (possibly)
	 * parse command-line arguments and invoke a helper method for creating and
	 * starting up the UI.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShow();
			}
		});
	}

	/**
	 * Static helper method creates the frame and makes it visible.
	 */
	private static void createAndShow() {
		JFrame frame = new ListDemo7(createTestData());
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Some fake data for testing.
	 */
	private static String[] testData = { "Bud Tugly", "Makeup artist", "Mike Easter", "Seat cushion tester", "Pikop Andropov",
			"Russian limosine driver", "Ajustos Miros", "Quality control technician", "Erasmus B. Draggin",
			"Working mother support group", "Warren Peace", "Leo Tolstoy biographer", };

	/**
	 * Creates an initial list of the fake data for testing.
	 */
	private static List<StaffData> createTestData() {
		List<StaffData> ret = new ArrayList<StaffData>();
		int i = 0;
		int id = 0;
		while (i < testData.length) {
			String name = testData[i++];
			String title = testData[i++];
			ret.add(new StaffData(name, id++, title));
		}
		return ret;
	}

	/**
	 * Instance of an action listener to deal with a user clicking the add
	 * button
	 */
	private ActionListener addActionL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			String name = addField.getText().trim();
			if (name.length() == 0) {
				JOptionPane.showMessageDialog(ListDemo7.this, "You cannot look up a blank name.", "Invalid Name Lookup",
						JOptionPane.WARNING_MESSAGE, null);
				return;
			}
			addField.setText("");
			worker = new LookupWorker(name);
			addButton.setEnabled(false);
			bottomPanel.setVisible(true);
			pack();
			worker.execute();
		}
	};
	/**
	 * Instance of an action listener to deal with a user clicking the delete
	 * button
	 */
	private ActionListener deleteActionL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			int index = list.getSelectedIndex();
			if (index >= 0 && index < model.getSize()) {
				model.removeElement(index);
				if (model.getSize() > 0) {
					list.setSelectedIndex(0);
					textArea.setText(model.getElement(0).toString());
				} else {
					textArea.setText("");
				}
			} else {
				System.out.println("HOw did you select an index out of the range of hte list?");
			}
		}
	};
	/**
	 * Instance of an action listener to deal with a user clicking the cancel
	 * button
	 */
	private ActionListener cancelActionL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			// Cancel button
			System.out.println("cancelling");
			if (worker != null && !worker.isCancelled() && !worker.isDone()) worker.cancel(true);
		}
	};
	/**
	 * Listener for the check box causes the list to be sorted in ascending or
	 * descending order.
	 */
	private ActionListener checkBoxListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (checkBox.isSelected()) {
				model.sortUp();
			} else {
				model.sortDown();
			}
			list.setSelectedIndex(0);
			list.ensureIndexIsVisible(0);
			textArea.setText(model.getElement(0).toString());
		}
	};
	/**
	 * List selection listener. When a list item is selected, we display the
	 * full text describing the highlighted item in the text area.
	 */
	private ListSelectionListener listSelL = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent event) {
			int index = list.getSelectedIndex();
			if (index >= 0 && index < model.getSize()) {
				StaffData d = model.getElement(index);
				textArea.setText(d.toString());
			}
		}
	};

	/**
	 * Subclass of SwingWorker for background task. Note the second type
	 * parameter is not used.
	 */
	private class LookupWorker extends SwingWorker<Integer, StackOverflowError> {
		private String employeeName;
		private Timer timeoutTimer, animateTimer;

		/**
		 * Constructs a LookupWorker Initializes all members
		 * 
		 * @param employeeName
		 *            the name of the employee, duh!
		 */
		public LookupWorker(String employeeName) {
			this.employeeName = employeeName;
			final int TEN_SECONDS = 10 * 1000;
			final int ONE_TENTH_OF_A_SECOND = (int) (.1 * 1000);
			timeoutTimer = new Timer(TEN_SECONDS, timeoutActionL);
			timeoutTimer.setRepeats(false);
			animateTimer = new Timer(ONE_TENTH_OF_A_SECOND, adjustProgressL);
			animateTimer.setRepeats(true);
		}

		/**
		 * Work to be performed by the background thread.
		 */
		@Override
		protected Integer doInBackground() throws Exception {
			System.out.println("doInBackground() executing in " + Thread.currentThread().getName());
			timeoutTimer.restart();
			animateTimer.restart();
			int ret = LookupService.lookup(employeeName);
			timeoutTimer.stop();
			animateTimer.stop();
			return ret;
		}

		/**
		 * Executed on the event thread when the background task is complete or
		 * is canceled.
		 */
		@Override
		protected void done() {
			System.out.println("done() executing in " + Thread.currentThread().getName());

			// retrieve the return value from the background task
			int id = 0;
			try {
				id = get();
			} catch (ExecutionException e) {
			} catch (InterruptedException e) {
			} catch (CancellationException e) {
			}

			// update model
			if (!isCancelled()) {
				model.addElement(employeeName, id, "unemployed");
				int i = model.getSize();
				list.setSelectedIndex(i - 1);
				list.ensureIndexIsVisible(i - 1);
			}

			// re-enable add button and remove progress bar
			addButton.setEnabled(true);
			bottomPanel.setVisible(false);
			progressBar.setValue(0);
			ListDemo7.this.pack();
		}

		/**
		 * Instance of an action listener to deal with timing out a lookup
		 */
		private ActionListener timeoutActionL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Lookup Timed Out");
				LookupWorker.this.cancel(true);
				JOptionPane.showMessageDialog(ListDemo7.this, "The action has timed out.  Sorry to waste your time.",
						"Timeout Error", JOptionPane.WARNING_MESSAGE, null);
			}
		};
		/**
		 * Instance of an action listener to deal with incrementing the progress
		 */
		private ActionListener adjustProgressL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = progressBar.getValue();
				final int EIGHTY_PERCENT_OF_THE_BAR = (int) (progressBar.getMaximum() * .8);
				final int LARGE_INCREMENT_AMMOUNT = 20;
				final int SMALL_INCREMENT_AMMOUNT = 3;
				// If the value is less than 80% of the whole, do the fast
				// progress
				if (value < EIGHTY_PERCENT_OF_THE_BAR) {
					progressBar.setValue(value + LARGE_INCREMENT_AMMOUNT);
				} else {
					// Otherwise do the slow progress
					progressBar.setValue(value + SMALL_INCREMENT_AMMOUNT);
				}
			}
		};
	}
}
