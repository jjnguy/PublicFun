import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to be used as tree node for representing a file tree in a Swing application.
 */
public class FileNode {
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
	public FileNode(String path) {
		this(path, false);
	}

	/**
	 * Constructs a directory or non-directory node.
	 * 
	 * @param name
	 *            exact file or directory name
	 * @param isDir
	 *            true if this node should represent a directory
	 */
	public FileNode(String path, boolean isDir) {
		this.path = path;
		this.isDir = isDir;
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
			File f = new File(path);
			if (f.exists()) {
				File[] files = f.listFiles();
				if (f.isDirectory() && files != null) {
					System.out.println(f);
					for (File file : files) {
						FileNode node = new FileNode(file.getAbsolutePath(), file.isDirectory());
						children.add(node);
					}
				} else {
					// oops - it really wasn't a directory
					isDir = false;
					children = null;
				}
			} else {
				// indicate an error in creating the children
				children.add(new DummyNode());
			}
		}
		hasBeenExpanded = true;
	}
}
