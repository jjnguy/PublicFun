package cs319.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cs319.gui.validators.NumberFieldValidator;
import cs319.taxreturn.IFormW2;
import cs319.taxreturn.ITaxReturn;
import cs319.taxreturn.impl.Form1040EZ2008;

/**
 * 
 * @author Justin Nelson
 */
@SuppressWarnings("serial")
public class TaxReturnFrame extends JFrame {

	private JFormattedTextField totalWages, totalTaxWithheld, taxOwed, adjustedGrossIncome,
			taxableIncome;
	private JLabel totalWagesL, totalTaxWithheldL, taxOwedL, adjustedGrossIncomeL,
			taxableIncomeL;

	private W2CheckBox marriedCheck, dependantCheck, spouseDependantCheck;
	private W2TextField taxableIntrest;

	private JButton addW2, removeW2, editW2;

	private W2JList w2s;
	private ITaxReturn logic;
	private PersonalInfoFrame personalInfo;

	/**
	 * Constructor
	 */
	public TaxReturnFrame() {
		super("A Cool Tax Return Program");
		setLookAndFeel();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(createMenu());
		logic = getW2FormInstance();
		logic = new Form1040EZ2008();// TODO remove this
		personalInfo = new PersonalInfoFrame(this);
		createComponents();
		add(createInfoPanel(), BorderLayout.SOUTH);
		add(createW2ListPanel(), BorderLayout.CENTER);
		add(createAditionalInfoPanel(), BorderLayout.EAST);
		setResizable(false);
		pack();
		setVisible(true);
	}

	/**
	 * Sets the look and feel of an application to that of the system it is running on. (Java's
	 * default looks bad)
	 */
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private static ITaxReturn getW2FormInstance() {
		ITaxReturn instance = null;
		try {
			String name = System.getProperty("cs319.taxreturn.name");
			Class<?> clss = Class.forName(name);
			instance = (ITaxReturn) clss.newInstance();
		} catch (ClassNotFoundException e) {
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (NullPointerException e) {
		}
		return instance;
	}

	private JPanel createAditionalInfoPanel() {
		JPanel ret = new JPanel(new BorderLayout());
		ret.setBorder(BorderFactory.createTitledBorder("Other Income Statistics"));
		JTextArea moreInfoField = new JTextArea();
		moreInfoField.setOpaque(false);
		moreInfoField.setText("Check the following respectivly if:\n" + "* You are married\n"
				+ "* You claim a dependant\n" + "* Your spouse claims a dependant");
		moreInfoField.setFont(new Font("Sans", Font.PLAIN, 12));
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel centerCenter = new JPanel();
		center.setLayout(new BorderLayout());
		north.add(taxableIntrest);
		centerCenter.add(marriedCheck);
		centerCenter.add(dependantCheck);
		centerCenter.add(spouseDependantCheck);
		center.add(moreInfoField, BorderLayout.NORTH);
		center.add(centerCenter, BorderLayout.CENTER);
		ret.add(north, BorderLayout.NORTH);
		ret.add(center, BorderLayout.CENTER);
		return ret;
	}

	private JPanel createInfoPanel() {
		JPanel ret = new JPanel();
		ret.setBorder(BorderFactory.createTitledBorder("Tax Return Calculations"));
		ret.setLayout(new BorderLayout());

		JPanel boxes = new JPanel(new GridLayout());
		boxes.add(totalWages);
		boxes.add(taxableIncome);
		boxes.add(taxOwed);
		boxes.add(adjustedGrossIncome);
		boxes.add(totalTaxWithheld);

		JPanel labels = new JPanel(new GridLayout());
		labels.add(totalWagesL);
		labels.add(taxableIncomeL);
		labels.add(taxOwedL);
		labels.add(adjustedGrossIncomeL);
		labels.add(totalTaxWithheldL);

		ret.add(labels, BorderLayout.NORTH);
		ret.add(boxes, BorderLayout.SOUTH);

		return ret;
	}

	private JPanel createW2ListPanel() {
		JPanel ret = new JPanel();
		ret.setBorder(BorderFactory.createTitledBorder("W2 Information"));
		ret.setLayout(new BorderLayout());
		ret.add(w2s, BorderLayout.CENTER);
		JPanel southPanel = new JPanel();
		southPanel.add(addW2);
		southPanel.add(editW2);
		southPanel.add(removeW2);
		ret.add(southPanel, BorderLayout.SOUTH);
		return ret;
	}

	private void createComponents() {
		w2s = new W2JList();
		addW2 = new JButton("Add New W2");
		addW2.addActionListener(newW2FormAction);
		removeW2 = new JButton("Delete W2");
		removeW2.addActionListener(removeW2Action);
		editW2 = new JButton("Edit W2");
		editW2.addActionListener(editW2Action);

		Dimension sizeOfTheTextBoxs = new Dimension(150, 20);
		Border theBorder = BorderFactory.createEmptyBorder();
		totalWages = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		totalWages.setText("$0.00");
		totalWages.setPreferredSize(sizeOfTheTextBoxs);
		totalWages.setEditable(false);
		totalWages.setBorder(theBorder);
		totalTaxWithheld = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		totalTaxWithheld.setText("$0.00");
		totalTaxWithheld.setPreferredSize(sizeOfTheTextBoxs);
		totalTaxWithheld.setEditable(false);
		totalTaxWithheld.setBorder(theBorder);
		taxOwed = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		taxOwed.setText("$0.00");
		taxOwed.setPreferredSize(sizeOfTheTextBoxs);
		taxOwed.setEditable(false);
		taxOwed.setBorder(theBorder);
		adjustedGrossIncome = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		adjustedGrossIncome.setText("$0.00");
		adjustedGrossIncome.setPreferredSize(sizeOfTheTextBoxs);
		adjustedGrossIncome.setEditable(false);
		adjustedGrossIncome.setBorder(theBorder);
		taxableIncome = new JFormattedTextField(NumberFormat.getCurrencyInstance());
		taxableIncome.setText("$0.00");
		taxableIncome.setPreferredSize(sizeOfTheTextBoxs);
		taxableIncome.setEditable(false);
		taxableIncome.setBorder(theBorder);
		totalWagesL = new JLabel("Total Wages Paid");
		totalTaxWithheldL = new JLabel("Total Tax Withheld");
		taxOwedL = new JLabel("Total Fedral Taxes Owed");
		adjustedGrossIncomeL = new JLabel("Adjusted Gross Income");
		taxableIncomeL = new JLabel("Total Taxable Income");

		taxableIntrest = new W2TextField("Taxable Intrest Recieved",
				new NumberFieldValidator(), W2TextField.VERTICAL);
		taxableIntrest.addDocumentListener(taxableIntrestChangeL);
		marriedCheck = new W2CheckBox("Married");
		marriedCheck.addActionListener(checkChangeListener);
		marriedCheck.setSelected(true);
		dependantCheck = new W2CheckBox("Dependant");
		dependantCheck.addActionListener(checkChangeListener);
		spouseDependantCheck = new W2CheckBox("Spouse Dependant");
		spouseDependantCheck.addActionListener(checkChangeListener);
	}

	private JMenuBar createMenu() {
		JMenuItem newW2 = new JMenuItem("Enter a New W2 Form");
		newW2.addActionListener(newW2FormAction);
		JMenuItem personalInfo = new JMenuItem("Enter Personal Info");
		personalInfo.addActionListener(personalInfoAct);
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(saveAction);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(exitAction);
		JMenu file = new JMenu("File");
		file.add(newW2);
		file.add(personalInfo);
		file.add(save);
		file.add(exit);
		JMenuBar theBar = new JMenuBar();
		theBar.add(file);
		return theBar;
	}

	private void checkboxChange() {
		logic.setMarried(marriedCheck.isSelected());
		logic.setDependent(dependantCheck.isSelected());
		logic.setSpouseDependent(marriedCheck.isSelected()
				&& spouseDependantCheck.isSelected());
		personalInfo.setMarried(marriedCheck.isSelected());
		updateFields();
	}

	public void addW2(String emp, double wage, double taxes) {
		logic.addW2Info(emp, wage, taxes);
		w2s.addW2(emp, taxes, wage);
		updateFields();
	}

	public void removeW2() {
		IFormW2 selectedForm = (IFormW2) w2s.getSelectedValue();
		if (selectedForm == null)
			return;
		w2s.removeW2(selectedForm.getEmployer());
		logic.removeW2(selectedForm.getEmployer());
		updateFields();
	}

	private static String makeDollarString(double value) {
		return String.format("$%.2f", value);
	}

	private void updateFields() {
		taxableIncome.setValue(logic.getTaxableIncome());
		adjustedGrossIncome.setValue(logic.getAdjustedGross());
		taxOwed.setValue(logic.computeTax());
		totalTaxWithheld.setValue(logic.getTotalTaxWithheld());
		totalWages.setValue(logic.getTotalWages());
	}

	public void saveForm(File dest) {
		PrintStream out = null;
		try {
			out = new PrintStream(dest);
		} catch (FileNotFoundException e) {
			return;
		}
		out.println("2009 1040EZ Tax Form -- Created Using JTaxFormDoer Tool");
		out.println("Personal Information ================================================");

		out.println("First Name: " + personalInfo.firstName());
		out.println("Last Name: " + personalInfo.lastName());
		if (marriedCheck.isSelected()) {
			out.println("Spouse's First Name: " + personalInfo.spousesFirstName());
			out.println("Spouse's Last Name: " + personalInfo.spousesLastName());
		}
		out.println("Social Security Number: " + personalInfo.SSN());
		if (marriedCheck.isSelected()) {
			out.println("Spouses Social Security Number" + personalInfo.spousesSSN());
		}
		out.println("Street Address: " + personalInfo.streetAddress());
		out.println("City: " + personalInfo.city());
		out.println("State: " + personalInfo.state());
		out.println("Zip Code: " + personalInfo.zipcode());
		out.println();

		if (w2s.getW2s().size() != 0) {
			out
					.println("Supplied W2 Information =============================================");
		}
		for (IFormW2 form : w2s.getW2s()) {
			out.println("Employer Name: " + form.getEmployer());
			out.println("Wages Paid: " + TaxReturnFrame.makeDollarString(form.getWages()));
			out.println("Tax Withheld: "
					+ TaxReturnFrame.makeDollarString(form.getTaxWithheld()));
			out.println();
		}
		out.println("Tax Return Statistics ===============================================");
		if (logic.isDependent()) {
			out.println("You claim one or more dependant.");
		}
		if (logic.isSpouseDependent() && logic.isMarried()) {
			out.println("Your spouse claims one or more dependant.");
		}
		out.println("Total Wages Paid: "
				+ TaxReturnFrame.makeDollarString(logic.getTotalWages()));
		out.println("Total Taxable Income: "
				+ TaxReturnFrame.makeDollarString(logic.getTaxableIncome()));
		out.println("Total Taxable Interest: "
				+ TaxReturnFrame.makeDollarString(logic.getTaxableInterest()));
		out.println("Total Ferdal Taxes Owed: "
				+ TaxReturnFrame.makeDollarString(logic.computeTax()));
		out.println("Adjusted Gross Income: "
				+ TaxReturnFrame.makeDollarString(logic.getAdjustedGross()));
		out.println("Total Tax Withheld: "
				+ TaxReturnFrame.makeDollarString(logic.getTotalTaxWithheld()));

		out.close();
	}

	private void updateTaxableIntrest() {
		try {
			double taxableIntrest = Double.parseDouble(this.taxableIntrest.getText());
			if (this.taxableIntrest.hasBeenVerified()) {
				logic.setTaxableInterest(taxableIntrest);
			} else {
				logic.setTaxableInterest(0);
			}
			if (taxableIntrest > 1500) {
				JOptionPane
						.showMessageDialog(
								this,
								"If your taxable intrest is greater than $1,500\nyou cannot use the 1040EZ form for your return.",
								"Invalid Taxable Intrest", JOptionPane.WARNING_MESSAGE, null);
				return;
			}
		} catch (NumberFormatException nfe) {
			logic.setTaxableInterest(0);
		}
		updateFields();
	}

	private ActionListener checkChangeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkboxChange();
			spouseDependantCheck.setEnabled(marriedCheck.isSelected());
		}
	};
	private DocumentListener taxableIntrestChangeL = new DocumentListener() {
		@Override
		public void changedUpdate(DocumentEvent e) {
			updateTaxableIntrest();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			updateTaxableIntrest();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			updateTaxableIntrest();
		}
	};
	private ActionListener newW2FormAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			W2InfoFrame w2info = new W2InfoFrame(TaxReturnFrame.this, logic);
			int choice = w2info.showW2Form();
			if (choice == W2InfoFrame.CANCEL_ACTION)
				return;
			addW2(w2info.getEmployer(), w2info.getWages(), w2info.getTaxWithheld());
		}
	};
	private ActionListener personalInfoAct = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			personalInfo.createshow();
		}
	};
	private ActionListener removeW2Action = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeW2();
		}
	};
	private ActionListener editW2Action = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			IFormW2 selectedForm = (IFormW2) w2s.getSelectedValue();
			if (selectedForm == null)
				return;
			W2InfoFrame w2info = new W2InfoFrame(TaxReturnFrame.this, logic, selectedForm
					.getEmployer(), selectedForm.getWages(), selectedForm.getTaxWithheld());
			int choice = w2info.showW2Form();
			if (choice == W2InfoFrame.CANCEL_ACTION)
				return;
			removeW2();
			addW2(w2info.getEmployer(), w2info.getWages(), w2info.getTaxWithheld());
		}
	};
	private ActionListener saveAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!personalInfo.isValid()) {
				JOptionPane
						.showMessageDialog(
								TaxReturnFrame.this,
								"You have not filled out your personal information.\nPlease look in the 'File' menu and fill out the form.",
								"Personal Informanton Not Filled Out",
								JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			if (!w2s.hasBeenVerified()) {
				JOptionPane
						.showMessageDialog(
								TaxReturnFrame.this,
								"You have not filled out any W2s.\nPlease look in the 'File' menu and add at least one W2.",
								"No W2s Added", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			if (!taxableIntrest.hasBeenVerified()) {
				JOptionPane
						.showMessageDialog(
								TaxReturnFrame.this,
								"You have not filled out the taxable interest field.\nYou can find it on the right side of the form.",
								"Taxable Interest Not Filled Out", JOptionPane.ERROR_MESSAGE,
								null);
				return;
			}
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showSaveDialog(TaxReturnFrame.this);
			if (choice != JFileChooser.APPROVE_OPTION) {
				return;
			}
			saveForm(chooser.getSelectedFile());
		}
	};
	private ActionListener exitAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};
}
