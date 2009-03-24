import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Swing viewer for a file tree on a remote server.
 */
public class FileTreeViewer extends JPanel {
	/**
	 * Preferred character set for URL encoding.
	 */
	private static final String DECODER_CHARSET = "UTF-8";

	/**
	 * Default hostname for the server.
	 */
	private static final String DEFAULT_HOST = "localhost";

	// Swing components
	private JTree tree;
	private FileTreeModel model;
	private JButton download;
	private JButton upload;
	private JButton refresh;
	private JFileChooser fc;

	/**
	 * Creates the viewer. This constructor is appropriate for use in both applets and applications.
	 * 
	 * @param host
	 *            the hostname for the server to which this viewer connects
	 * 
	 */
	public FileTreeViewer() {
		super(new BorderLayout());

		// shared file chooser, it it remembers previous directory
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);

		FileNode root = new FileNode("C:\\", true);

		// Construct the tree and add it to this panel.
		model = new FileTreeModel(root);
		tree = new JTree(model);
		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane, BorderLayout.CENTER);

		// Allow selections of single nodes only
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// add selection and expansion listeners
		tree.addTreeExpansionListener(new ExpansionListener());
		tree.addTreeSelectionListener(new SelectionListener());

		// Start out with the root node collapsed
		tree.collapseRow(0);

		// buttons
		download = new JButton("Download");
		download.setEnabled(false);
		download.addActionListener(new MyButtonListener());

		upload = new JButton("Upload");
		upload.setEnabled(false);
		upload.addActionListener(new MyButtonListener());

		refresh = new JButton("Refresh");
		refresh.setEnabled(false);
		refresh.addActionListener(new MyButtonListener());

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(download);
		bottomPanel.add(upload);
		bottomPanel.add(refresh);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Creates a FileTreeViewer in a frame and displays it, using the default hostname and port.
	 * 
	 * @param root
	 *            node to be used as the root of the tree's model
	 */
	private static void createAndShow() {
		// Create and set up the window.
		JFrame frame = new JFrame("File Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		FileTreeViewer newContentPane = new FileTreeViewer();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Entry point for running as a standalone application.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		start();
	}

	/**
	 * Creates a viewer from the given root node.
	 * 
	 * @param root
	 *            node to be used as the root of the tree's model
	 */
	public static void start() {
		// Create and start the application on the Swing
		// event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShow();
			}
		});
	}

	/**
	 * Expansion listener initiates update of a directory node when expanded.
	 */
	private class ExpansionListener implements TreeExpansionListener {
		@Override
		public void treeCollapsed(TreeExpansionEvent arg0) {
			// not used
		}

		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			TreePath path = event.getPath();
			System.out.println("Expanding " + path);
			tree.setSelectionPath(path);
			model.updateChildren(path, false);
		}
	}

	/**
	 * Selection listener enables appropriate buttons depending on whether a file or directory is selected.
	 */
	private class SelectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent event) {
			FileNode node = (FileNode) tree.getLastSelectedPathComponent();
			if (node == null) {
				download.setEnabled(false);
				upload.setEnabled(false);
				refresh.setEnabled(false);
			} else if (node.getIsDirectory()) {
				download.setEnabled(false);
				upload.setEnabled(true);
				refresh.setEnabled(true);
			} else {
				download.setEnabled(true);
				upload.setEnabled(false);
				refresh.setEnabled(false);
			}
		}
	}

	/**
	 * Button listener initiates upload, download, or directory refresh.
	 */
	private class MyButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			TreePath path = tree.getLeadSelectionPath();

			if (event.getSource() == download) {
				// TODO
			} else if (event.getSource() == upload) {
				// TODO
			} else {
				// directory refresh request
				model.updateChildren(path, true);
				tree.expandPath(path);
			}
		}
	}

}
