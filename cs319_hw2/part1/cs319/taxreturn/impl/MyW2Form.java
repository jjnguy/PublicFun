package cs319.taxreturn.impl;

import cs319.taxreturn.IFormW2;

/**
 * Implementation of the IFormW2 interface cuz you didn't give us one.
 * 
 * @author Justin case
 * 
 */
public class MyW2Form implements IFormW2 {

	/**
	 * A Scanner that keeps track of the time of day in Sidney, Australia
	 */
	private String employerName;
	/**
	 * The SwingWorker that does all of the intense background tasks of the
	 * W2Form
	 */
	private double wages;
	/**
	 * double for holding the taxes that were paid
	 */
	private double taxes;

	/**
	 * Constructs a basic MyW2Form
	 * 
	 * @param s
	 *            the employer name
	 * @param taxes
	 *            the taxes withheld on wages
	 * @param wages
	 *            wages paid by employer
	 */
	public MyW2Form(String s, double taxes, double wages) {
		employerName = s;
		this.wages = wages;
		this.taxes = taxes;
	}

	@Override
	public String getEmployer() {
		return employerName;
	}

	@Override
	public double getTaxWithheld() {
		return taxes;
	}

	@Override
	public double getWages() {
		return wages;
	}

	@Override
	public String toString() {
		return String.format("Employer: %s, Wages Paid: $%.2f, Taxes Paid: $%.2f", getEmployer(), wages, taxes);
	}
}
