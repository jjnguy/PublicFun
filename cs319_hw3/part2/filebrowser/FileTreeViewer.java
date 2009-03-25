package filebrowser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

import filebrowser.SimpleHttpServer2;

/**
 * Swing viewer for a file tree on a remote server.
 */
public class FileTreeViewer extends JPanel {
	/**
	 * Preferred character set for URL encoding.
	 */
	private static final String DECODER_CHARSET = "UTF-8";

	// wrowclif.student.iastate.edu:8180/filebrowser/listfiles/
	/**
	 * Default hostname for the server.
	 */
	private String host;
	private int port;

	// Swing components
	private JTree tree;
	private FileTreeModel model;
	private JButton download;
	private JButton upload;
	private JButton refresh;
	private JFileChooser fc;

	private final String contentBaseDir = "/filebrowser/listfiles/";

	/**
	 * Creates the viewer. This constructor is appropriate for use in both applets and applications.
	 * 
	 * @param host
	 *            the hostname for the server to which this viewer connects
	 * 
	 */
	public FileTreeViewer(String hostname, int port) {
		super(new BorderLayout());
		host = hostname;
		this.port = port;

		// shared file chooser, it it remembers previous directory
		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);

		FileNode root = new FileNode(hostname, port, "/", true);

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
	private static void createAndShow(String host, int port) {
		// Create and set up the window.
		JFrame frame = new JFrame("File Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		FileTreeViewer newContentPane = new FileTreeViewer(host, port);
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
		start("localhost", 2222);
		// start("wrowclif.student.iastate.edu", 8180);
		// start(args[0], Integer.parseInt(args[1]));
	}

	/**
	 * Creates a viewer from the given root node.
	 * 
	 * @param root
	 *            node to be used as the root of the tree's model
	 */
	public static void start(final String host, final int port) {
		// Create and start the application on the Swing
		// event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShow(host, port);
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
				try {
					download();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (event.getSource() == upload) {
				// TODO
				try {
					upload();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// directory refresh request
				model.updateChildren(path, true);
				tree.expandPath(path);
			}
		}
	}

	private void download() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showSaveDialog(this);
		if (choice != JFileChooser.APPROVE_OPTION)
			return;
		String path = tree.getSelectionPath().toString();
		System.out.println("Selected path to string: " + path);
		getRemoteFileFromPath(treePathToFilePath(tree.getSelectionPath()), new FileOutputStream(chooser
				.getSelectedFile()));
	}

	private void upload() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showOpenDialog(this);
		if (choice != JFileChooser.APPROVE_OPTION)
			return;
		String path = tree.getSelectionPath().toString();
		System.out.println("Selected path to string: " + path);
		sendFileToRemotePath(FileTreeViewer.treePathToFilePath(tree.getSelectionPath()) + "/"
				+ chooser.getSelectedFile().getName(), new FileInputStream(chooser.getSelectedFile()));
	}

	private static String treePathToFilePath(TreePath tree) {
		String lastElement = tree.getLastPathComponent().toString().substring(1);
		return lastElement;
	}

	private boolean sendFileToRemotePath(String path, FileInputStream fin) {
		Socket s = null;
		try {
			s = new Socket(host, port);
			// for line-oriented output we use a PrintWriter
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			pw.println("POST " + SimpleHttpServer2.APP_DIR + path + " HTTP/1.1");
			pw.print("\r\n"); // empty line
			pw.flush();

			copyStream(fin, s.getOutputStream());

			pw.close();

			return true;
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (IOException ignore) {
			}
		}
		return false;
	}

	private boolean getRemoteFileFromPath(String remotepath, FileOutputStream fout) {
		Socket s = null;
		try {
			s = new Socket(host, port);
			// for line-oriented output we use a PrintWriter
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			pw.println("GET " + SimpleHttpServer2.APP_DIR + remotepath + " HTTP/1.1");
			pw.print("\r\n"); // empty line
			pw.flush();

			InputStream in = s.getInputStream();
			String response = SimpleHttpServer2.readLine(in);
			
			// get rid of headers
			while (true) {
				String line = SimpleHttpServer2.readLine(in);
				if (line.trim().length() == 0)
					break;
			}

			copyStream(in, fout);

			pw.close();

			return true;
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (IOException ignore) {
			}
		}
		return false;
	}

	private static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, bytesRead);
		}
	}
}
