package cs319.taxreturn;

/**
 * Encapsulates relevant data from a Federal form W-2.
 */
public interface IFormW2
{
  /**
   * Returns the employer name for this W-2.
   * @return name of the employer
   */
  public String getEmployer();
  
  /**
   * Returns the wages reported on this W-2.
   * @return wages reported on this W-2
   */
  public double getWages();
  
  /**
   * Returns the tax withheld on this W-2.
   * @return tax withheld on this W-2
   */
  public double getTaxWithheld();
}
