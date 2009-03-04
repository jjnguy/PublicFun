package cs319.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import cs319.taxreturn.IFormW2;

/**
 * A model for backing a W2JList
 * 
 * @author Justin Timberlake
 * 
 */
@SuppressWarnings("serial")
public class W2ListModel extends AbstractListModel {

	/**
	 * The meat and potatoes of the model, the data!!!
	 */
	private List<IFormW2> w2s;

	/**
	 * constructs a basic W2ListModel. Begins empty
	 */
	public W2ListModel() {
		w2s = new ArrayList<IFormW2>();
	}

	/**
	 * Adds a new w2 form to the model
	 * 
	 * @param w2
	 *            The form to add
	 */
	public void addW2(IFormW2 w2) {
		w2s.add(w2);
		this.fireIntervalAdded(this, w2s.size() - 1, w2s.size() - 1);
	}

	/**
	 * Finds a W2 given the employers name and removes it
	 * 
	 * @param empName
	 *            the employers name
	 */
	public void removeW2(String empName) {
		for (int i = 0; i < w2s.size(); i++) {
			if (w2s.get(i).getEmployer().toLowerCase().equals(empName.toLowerCase())) {
				w2s.remove(i);
				if (w2s.size() == 0) {
					this.fireIntervalRemoved(this, 0, 0);
				} else {
					this.fireIntervalRemoved(this, w2s.size() - 1, w2s.size() - 1);
				}
				break;
			}
		}
	}

	public List<IFormW2> getW2s() {
		return w2s;
	}

	@Override
	public Object getElementAt(int index) {
		return w2s.get(index);
	}

	@Override
	public int getSize() {
		return w2s.size();
	}

}
