package cs319.taxreturn;
import java.util.List;

/**
 * Computes Federal income tax according to form 1040EZ.
 */
public interface ITaxReturn
{
  /**
   * Adds information from a W-2 to this tax return.
   * @param employer name of the employer (must be unique)
   * @param wages income reported on the W-2
   * @param taxWithheld amount of Federal income tax withheld
   */
  public void addW2Info(String employer, double wages, double taxWithheld);
  
  /**
   * Removes a W-2 from this tax return, as identified by the 
   * employer name.
   * @param employer name of the employer for this W-2
   */
  public void removeW2(String employer);
  
  /**
   * Returns a list of all W-2s for this tax return.
   * @return snapshot of the list of W-2s associated with this return 
   */
  public List<IFormW2> getAllW2s();
  
  /**
   * Returns the total wages from all W-2s for this tax return.
   * @return total wages
   */
  public double getTotalWages();
  
  /**
   * Returns the total amount of tax withheld on all W-2s for
   * this tax return.
   * @return total of tax withheld
   */
  public double getTotalTaxWithheld();
  
  /**
   * Set the amount of taxable interest income for this tax return.
   * @param interest the amount of taxable interest income
   */
  public void setTaxableInterest(double interest);
  
  /**
   * Returns the taxable interest income for this tax return.
   * @return interest income currently recorded on this return
   */
  public double getTaxableInterest();
  
  /**
   * Sets the married status for this return.
   * @param married true if the filing status is married, false otherwise
   */
  public void setMarried(boolean married);
  
  /**
   * Returns the filing status for this return.
   * @return true if the filing status is married, false otherwise
   */
  public boolean isMarried();
  
  /**
   * Sets the dependent status of the taxpayer.
   * @param dependent true if the taxpayer can be claimed as a dependent
   * on another person's return
   */
  public void setDependent(boolean dependent);
  
  /**
   * Sets the dependent status of the taxpayer's spouse.  Has no effect
   * unless the filing status is married.
   * @param dependent true if the spouse can be claimed as a dependent
   * on another person's return
   */
  public void setSpouseDependent(boolean dependent);
  
  /**
   * Returns the dependent status of the taxpayer.
   * @return true if the taxpayer can be claimed as a dependent
   * on another person's return
   */
  public boolean isDependent();
  
  /**
   * Returns the dependent status of the taxpayer's spouse.  Returns false
   * if the filing status is not married.
   * @param dependent true if the spouse can be claimed as a dependent
   * on another person's return
   */
  public boolean isSpouseDependent();
  
  /**
   * Returns the adjusted gross income as calculated on line 4
   * of form 1040EZ
   * @return adjusted gross income
   */
  public double getAdjustedGross();
  
  /**
   * Returns the taxable income as calculated on line 6 of 
   * form 1040EZ
   * @return taxable income
   */
  public double getTaxableIncome();
  
  /**
   * Returns the income tax owed as calculated on line 11 of
   * form 1040EZ.
   */
  public double computeTax();
}
