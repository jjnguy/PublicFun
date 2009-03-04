package cs319.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cs319.gui.validators.EmptyFieldValidator;
import cs319.gui.validators.NumberFieldValidator;
import cs319.taxreturn.IFormW2;
import cs319.taxreturn.ITaxReturn;

@SuppressWarnings("serial")
public class W2InfoFrame extends JDialog implements IFormW2 {

	public static final int CANCEL_ACTION = -13;
	public static final int SUBMIT_ACTION = -31;

	private W2TextField employerName, taxWithheld, wages;
	private JButton cancel, submit;
	private int closeOption;
	private ITaxReturn logic;
	private boolean editMode;

	public W2InfoFrame(TaxReturnFrame parent, ITaxReturn logic, String empName, double wages,
			double taxWthheld) {
		this(parent, logic);
		this.employerName.setText(empName);
		this.taxWithheld.setText(taxWthheld + "");
		this.wages.setText(wages + "");
		editMode = true;
	}

	public W2InfoFrame(TaxReturnFrame parent, ITaxReturn logic) {
		super(parent);
		editMode = false;
		this.logic = logic;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		closeOption = CANCEL_ACTION;
		Dimension size = new Dimension(150, 20);
		employerName = new W2TextField("Employer Name:", new EmptyFieldValidator(),
				W2TextField.HORIZONTAL);
		employerName.setPreferredSize(size);
		employerName.addKeyListener(enterPrssed);
		taxWithheld = new W2TextField("Total Tax Withheld:", new NumberFieldValidator(),
				W2TextField.HORIZONTAL);
		taxWithheld.setPreferredSize(size);
		taxWithheld.addKeyListener(enterPrssed);
		wages = new W2TextField("Total Wages Paid:", new NumberFieldValidator(),
				W2TextField.HORIZONTAL);
		wages.setPreferredSize(size);
		wages.addKeyListener(enterPrssed);
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancelAction);
		submit = new JButton("Submit");
		submit.addActionListener(submitAction);
		JPanel mainPanel = new JPanel();
		mainPanel.add(employerName);
		mainPanel.add(taxWithheld);
		mainPanel.add(wages);
		mainPanel.add(cancel);
		mainPanel.add(submit);
		add(mainPanel);
		setResizable(false);
		setTitle("W2 Information");
		setPreferredSize(new Dimension(360, 135));
	}

	public int showW2Form() {
		pack();
		setVisible(true);
		return closeOption;
	}

	@Override
	public String getEmployer() {
		return employerName.getText();
	}

	@Override
	public double getTaxWithheld() {
		return Double.parseDouble(taxWithheld.getText());
	}

	@Override
	public double getWages() {
		return Double.parseDouble(wages.getText());
	}

	private KeyListener enterPrssed = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				submit.doClick();
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	};
	private ActionListener submitAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<IFormW2> allForms = logic.getAllW2s();
			if (allForms != null) {
				for (IFormW2 formW2 : allForms) {
					if (!editMode && formW2.getEmployer().equals(employerName.getText())) {
						JOptionPane.showMessageDialog(W2InfoFrame.this,
								"You have already submitted a W2 for the entered employer.",
								"Duplicate Employer", JOptionPane.ERROR_MESSAGE, null);
						return;
					}
				}
			}
			if (wages.hasBeenVerified() && employerName.hasBeenVerified()
					&& taxWithheld.hasBeenVerified()) {
				closeOption = SUBMIT_ACTION;
				dispose();
			} else {
				JOptionPane.showMessageDialog(W2InfoFrame.this,
						"One ore more of the forms have not been verified.", "Invalid Form",
						JOptionPane.ERROR_MESSAGE, null);
			}
			return;
		}
	};

	private ActionListener cancelAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			closeOption = CANCEL_ACTION;
			dispose();
		}
	};
}
