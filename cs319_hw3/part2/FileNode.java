import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to be used as tree node for representing a file tree in a Swing application.
 */
public class FileNode {

	private String host;
	private int port;

	/**
	 * Children of this node.
	 */
	private List<FileNode> children;

	/**
	 * Path represented by this node.
	 */
	private String path;

	/**
	 * Indicates whether this node represents a directory.
	 */
	private boolean isDir;

	/**
	 * Indicates whether the children of this node have ever been created.
	 */
	private boolean hasBeenExpanded;

	/**
	 * Constructs a non-directory node.
	 * 
	 * @param name
	 *            exact file or directory name
	 */
	public FileNode(String host, int port, String path) {
		this(host, port, path, false);
	}

	/**
	 * Constructs a directory or non-directory node.
	 * 
	 * @param name
	 *            exact file or directory name
	 * @param isDir
	 *            true if this node should represent a directory
	 */
	public FileNode(String host, int port, String path, boolean isDir) {
		this.path = path;
		this.isDir = isDir;
		this.host = host;
		this.port = port;
	}

	/**
	 * Returns the child at a given position.
	 * 
	 * @param i
	 *            0-based index of the child node position
	 * @return child at the given position, or null if there is no child at the given position
	 */
	public FileNode getChild(int i) {
		if (isDir && !hasBeenExpanded) {
			refreshChildren(true);
		}
		if (children != null && i >= 0 && i < children.size()) {
			return children.get(i);
		}
		return null;
	}

	/**
	 * Returns the total number of children of this node
	 * 
	 * @return total number of children of this node
	 */
	public int getChildCount() {
		if (isDir && !hasBeenExpanded) {
			refreshChildren(true);
		}
		return children != null ? children.size() : 0;
	}

	/**
	 * Returns the filename represented by this node.
	 * 
	 * @return the filename represented by this node
	 */
	public String toString() {
		int index = path.lastIndexOf("\\");
		if (index >= 0) {
			return path.substring(index + 1);
		} else {
			return path;
		}
	}

	/**
	 * Determines whether this node represents a directory.
	 * 
	 * @return true if this node represents a directory, false otherwise
	 */
	public boolean getIsDirectory() {
		return isDir;
	}

	/**
	 * Create the child nodes for this node, if necessary. If the <code>forceRefresh</code> parameter is true, recreates
	 * the child nodes whether or not they have been created before.
	 * 
	 * @param forceRefresh
	 *            indicates whether to recreate the child nodes even if they have already been created
	 */
	public void refreshChildren(boolean forceRefresh) {
		if (!isDir) {
			return;
		}
		if (children == null) {
			children = new ArrayList<FileNode>();
		}
		if (forceRefresh || !hasBeenExpanded) {
			children.clear();
			// fill in child nodes using the File object
			List<FileNode> children = retrieveListOfChildren(host, port, path);
			if (children != null) {
				this.children = children;
			} else {
				// indicate an error in creating the children
				this.children.add(new DummyNode());
			}
		}
		hasBeenExpanded = true;
	}

	public static List<FileNode> retrieveListOfChildren(String host, int port, String path) {
		Socket s = null;
		try {
			// open a connection to the server on port 2222
			s = new Socket(host, port);
			OutputStream out = s.getOutputStream();

			// for line-oriented output we use a PrintWriter
			PrintWriter pw = new PrintWriter(out);
			pw.println("GET " + path + " HTTP/1.1");
			pw.print("\r\n"); // empty line
			pw.flush();

			List<FileNode> ret = new ArrayList<FileNode>();
			Scanner in = new Scanner(s.getInputStream());

			// need to get rid of headers
			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.trim().length() == 0)
					break;
			}

			while (in.hasNextLine()) {
				String line = in.nextLine();
				if (line.startsWith("DIR:")) {
					ret.add(new FileNode(host, port, path + "/" + line.substring(4), true));
				} else {
					ret.add(new FileNode(host, port, path + "/" + line, false));
				}
			}
			out.close();
			pw.close();

			return ret;
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (s != null)
					s.close();
			} catch (IOException ignore) {
			}
		}
		return null;
	}
}
