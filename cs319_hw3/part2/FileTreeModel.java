import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Data model for the file tree viewer.
 */
public class FileTreeModel implements TreeModel {
	/**
	 * Listeners for this tree model.
	 */
	private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

	/**
	 * Root node for this tree model.
	 */
	private FileNode root;

	/**
	 * Constructs a tree model with the given root.
	 * 
	 * @param r
	 *            node to be used as the root for this model
	 */
	public FileTreeModel(FileNode r) {
		root = r;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		System.out.println("adding tree model listener " + l);
		treeModelListeners.add(l);
	}

	@Override
	public Object getChild(Object node, int i) {
		return ((FileNode) node).getChild(i);
	}

	@Override
	public int getChildCount(Object node) {
		return ((FileNode) node).getChildCount();
	}

	@Override
	public int getIndexOfChild(Object node, Object child) {
		FileNode n = (FileNode) node;
		for (int i = 0; i < n.getChildCount(); ++i) {
			if (n.getChild(i).equals(child)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return !((FileNode) node).getIsDirectory();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// Required by TreeModel, but not used here
	}

	/**
	 * Replaces the children of the node represented by path with the given list of new nodes, and fires an event to
	 * model listeners.
	 * 
	 * @param path
	 *            the TreePath whose last component is the node to be updated
	 * @param newNodes
	 *            a list of new nodes to replace the existing children
	 */
	public void updateChildren(TreePath path, boolean forceRefresh) {
		FileNode node = (FileNode) path.getLastPathComponent();
		node.refreshChildren(forceRefresh);

		// Notify the tree that the model has changed.
		// The event provides a path to the node beneath which the
		// structure has changed.
		TreeModelEvent event = new TreeModelEvent(this, path);

		for (TreeModelListener listener : treeModelListeners) {
			listener.treeStructureChanged(event);
		}
	}

}
