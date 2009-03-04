package cs319.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JList;

import cs319.taxreturn.IFormW2;
import cs319.taxreturn.impl.MyW2Form;

@SuppressWarnings("serial")
public class W2JList extends JList implements W2Component {

	private W2ListModel model;

	public W2JList() {
		model = new W2ListModel();
		setPreferredSize(new Dimension(400, 100));
		setModel(model);
	}

	public void addW2(String emp, double taxes, double wages) {
		model.addW2(new MyW2Form(emp, taxes, wages));
	}

	public void removeW2(String empName) {
		model.removeW2(empName);
	}

	public List<IFormW2> getW2s() {
		return model.getW2s();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (model.getSize() <= 0) {
			setBackground(W2TextField.INVALID_COLOR);
			g.drawString("You must add at least one(1) W2 form.", 5, 15);
		} else {
			setBackground(W2TextField.VALID_COLOR);
		}
	}

	@Override
	public boolean hasBeenVerified() {
		return model.getSize() > 0;
	}
}
