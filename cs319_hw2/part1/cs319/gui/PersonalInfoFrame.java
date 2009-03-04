package cs319.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs319.gui.validators.CityStateZipValidator;
import cs319.gui.validators.FirstNameMiddleInitialValidator;
import cs319.gui.validators.LastNameValidator;
import cs319.gui.validators.SSNValidator;
import cs319.gui.validators.StreetAddressValidator;

@SuppressWarnings("serial")
public class PersonalInfoFrame extends JDialog {

	// Below are the text fields that will contain values in the form.
	// Some of them will be automatically populated, others will be auto
	// populated
	private W2TextField firstName, lastName, spouseFirName, spouseLaName, addr, cityStateZip;
	private W2TextField ssN, spouseSSn;
	private JLabel helpLabel;

	// These panels will contain groups of widgets, they will help organize the
	// form
	private JPanel personalInformation;

	public PersonalInfoFrame(TaxReturnFrame parent) {
		super(parent, "Personal Information");
		createComponents();
		layoutComponents();
		addListeners();
	}

	public void setMarried(boolean married) {
		spouseFirName.setEnabled(married);
		spouseLaName.setEnabled(married);
		spouseSSn.setEnabled(married);
	}

	public boolean isValid() {

		if (!(firstName.hasBeenVerified() && lastName.hasBeenVerified()
				&& addr.hasBeenVerified() && cityStateZip.hasBeenVerified() && ssN
				.hasBeenVerified())) {
			return false;
		}
		if (spouseFirName.isEnabled()) {
			return spouseFirName.hasBeenVerified() && spouseLaName.hasBeenVerified()
					&& spouseSSn.hasBeenVerified();
		}
		return true;
	}

	public String firstName() {
		return firstName.getText().trim();
	}

	public String lastName() {
		return lastName.getText().trim();
	}

	public String spousesFirstName() {
		return spouseFirName.getText().trim();
	}

	public String spousesLastName() {
		return spouseLaName.getText().trim();
	}

	public String streetAddress() {
		return addr.getText().trim();
	}

	public String city() {
		return cityStateZip.getText().trim().split(",")[0].trim();
	}

	public String state() {
		return cityStateZip.getText().trim().split(",")[1].trim();
	}

	public String zipcode() {
		return cityStateZip.getText().trim().split(",")[2].trim();
	}

	public String SSN() {
		return ssN.getText().trim();
	}

	public String spousesSSN() {
		return spouseSSn.getText().trim();
	}

	private void createComponents() {
		personalInformation = buildPersonalInfoPanel();
	}

	private JPanel buildPersonalInfoPanel() {
		JPanel ret = new JPanel();
		ret.setBorder(BorderFactory.createTitledBorder("Personal Information"));
		ret.setLayout(new GridBagLayout());

		helpLabel = new JLabel("For help see the tooltip for each field.");

		Dimension theSize = new Dimension(200, 20);

		firstName = new W2TextField("Your First Name, Middle Initial:",
				new FirstNameMiddleInitialValidator(), W2TextField.VERTICAL);
		firstName.setPreferredSize(theSize);
		lastName = new W2TextField("Your Last Name:", new LastNameValidator(),
				W2TextField.VERTICAL);
		lastName.setPreferredSize(theSize);
		spouseFirName = new W2TextField("Your Spouses First Name:",
				new FirstNameMiddleInitialValidator(), W2TextField.VERTICAL);
		spouseFirName.setPreferredSize(theSize);
		spouseLaName = new W2TextField("Your Spouses Last Name:", new LastNameValidator(),
				W2TextField.VERTICAL);
		spouseLaName.setPreferredSize(theSize);

		addr = new W2TextField("Street Address:", new StreetAddressValidator(),
				W2TextField.VERTICAL);
		addr.setPreferredSize(theSize);
		cityStateZip = new W2TextField("City, State, Zip Code", new CityStateZipValidator(),
				W2TextField.VERTICAL);
		cityStateZip.setPreferredSize(theSize);

		ssN = new W2TextField("Your SSN:", new SSNValidator(), W2TextField.VERTICAL);
		ssN.setPreferredSize(theSize);
		spouseSSn = new W2TextField("Your Spouses SSN", new SSNValidator(),
				W2TextField.VERTICAL);
		spouseSSn.setPreferredSize(theSize);

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		gc.gridwidth = 2;
		ret.add(helpLabel, gc);
		gc.gridwidth = 1;
		gc.gridy = 1;
		ret.add(firstName, gc);
		gc.gridx = 1;
		ret.add(lastName, gc);
		gc.gridy = 2;
		gc.gridx = 0;
		ret.add(spouseFirName, gc);
		gc.gridx = 1;
		ret.add(spouseLaName, gc);
		gc.gridy = 3;
		gc.gridx = 0;
		ret.add(addr, gc);
		gc.gridx = 1;
		ret.add(cityStateZip, gc);
		gc.gridy = 4;
		gc.gridx = 0;
		ret.add(ssN, gc);
		gc.gridx = 1;
		ret.add(spouseSSn, gc);

		return ret;
	}

	private void layoutComponents() {
		getContentPane().add(personalInformation, BorderLayout.CENTER);
	}

	private void addListeners() {
	}

	public void createshow() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}
