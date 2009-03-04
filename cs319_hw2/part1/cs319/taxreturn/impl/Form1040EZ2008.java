package cs319.taxreturn.impl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs319.taxreturn.IFormW2;
import cs319.taxreturn.ITaxReturn;

/**
 * Implementation of the ITaxReturn interface for 2008
 * returns based on form 1040EZ.
 */
public class Form1040EZ2008 implements ITaxReturn
{
  private static final double WORKSHEET_LINE_A_OFFSET = 300;
  private static final double MINIMUM_STANDARD_DEDUCTION = 900;
  private static final double MARRIED_STANDARD_DEDUCTION = 10900;
  private static final double SINGLE_STANDARD_DEDUCTION = 5450;
  private static final double EXEMPTION = 3500;
    
  private static final double[] SINGLE_BRACKETS = 
  {
    0,
    8350,     // 10%
    33950,    // 15%
    82250,    // 25%
    171550,   // 28%
    372950    // 33%
  };
  
  private static final double[] MARRIED_BRACKETS =
  {
    0,
    16700,    // 10%
    67900,    // 15%
    137050,   // 25%
    208850,   // 28%
    372950    // 33%
  };
  
  private static final double[] BRACKET_RATES =
  {
    .10,
    .15,
    .25,
    .28,
    .33,
    .35
  };

  private double interestIncome;
  private boolean married;
  private boolean dependent;
  private boolean dependentSpouse;
  private List<IFormW2> w2s = new ArrayList<IFormW2>();
  
  @Override
  public void addW2Info(String employer, double wages, double taxWithheld)
  {
    w2s.add(new FormW2(employer, wages, taxWithheld));
  }

  @Override
  public double computeTax()
  {
    double tax = 0;
    double[] brackets = isMarried() ? MARRIED_BRACKETS : SINGLE_BRACKETS;
    
    double taxable = getTaxableIncome();   
    for (int i = brackets.length - 1; i >= 0; --i)
    {
      if (taxable > brackets[i])
      {
        double marginal = (taxable - brackets[i]) * BRACKET_RATES[i];
        tax += marginal;
        taxable = brackets[i];
      }
    }
    return tax;
  }

  @Override
  public double getAdjustedGross()
  {
    return getTotalWages() + interestIncome;
  }

  @Override
  public List<IFormW2> getAllW2s()
  {
    return w2s;
  }

  @Override
  public double getTaxableIncome()
  {
    double adjusted = getAdjustedGross();
    if (!isDependent() && !isSpouseDependent())
    {
      double totalExemption;
      if (isMarried())
      {
        totalExemption = MARRIED_STANDARD_DEDUCTION + 2 * EXEMPTION;
      }
      else
      {
        totalExemption = SINGLE_STANDARD_DEDUCTION + EXEMPTION;
      }
      return Math.max(0, adjusted - totalExemption);
    }
    
    // otherwise, at least one of taxpayer and spouse
    // is a dependent, so do the worksheet calculation
    double wages = adjusted - interestIncome;
    double lineA = wages + WORKSHEET_LINE_A_OFFSET;
    double lineC = Math.max(lineA, MINIMUM_STANDARD_DEDUCTION);
    double lineD = isMarried() ? MARRIED_STANDARD_DEDUCTION : SINGLE_STANDARD_DEDUCTION;
    double lineE = Math.min(lineC, lineD);
    double lineF = 0;
    if (isMarried())
    {
      // if only one of taxpayer and spouse is a dependent
      // enter 3500, otherwise zero
      if (!isDependent() || !isSpouseDependent())
      {
        lineF = 3500;
      }
    }
    double lineG = lineE + lineF;
    return Math.max(0, adjusted - lineG);
  }

  @Override
  public double getTaxableInterest()
  {
    return interestIncome;
  }

  @Override
  public double getTotalTaxWithheld()
  {
    double total = 0;
    for (IFormW2 f : w2s)
    {
      total += f.getTaxWithheld();
    }
    return total;
  }

  @Override
  public double getTotalWages()
  {
    double total = 0;
    for (IFormW2 f : w2s)
    {
      total += f.getWages();
    }
    return total;
  }

  @Override
  public boolean isDependent()
  {
    return dependent;
  }

  @Override
  public boolean isMarried()
  {
    return married;
  }

  @Override
  public boolean isSpouseDependent()
  {
    return dependentSpouse;
  }

  @Override
  public void removeW2(String employer)
  {
    Iterator<IFormW2> iter = w2s.iterator();
    while (iter.hasNext())
    {
      IFormW2 f = iter.next();
      if (f.getEmployer().equals(employer))
      {
        iter.remove();
        break;
      }
    }
  }

  @Override
  public void setDependent(boolean dependent)
  {
    this.dependent = dependent;
  }

  @Override
  public void setMarried(boolean married)
  {
    this.married = married;
  }

  @Override
  public void setSpouseDependent(boolean dependent)
  {
      this.dependentSpouse = dependent && isMarried();
  }

  @Override
  public void setTaxableInterest(double interest)
  {
    interestIncome = interest;
  }

  private static class FormW2 implements IFormW2
  {
    private String employer;
    private double wages;
    private double taxWithheld;
    
    public FormW2(String pEmployer, double pWages, double pTaxWithheld)
    {
      employer = pEmployer;
      wages = pWages;
      taxWithheld = pTaxWithheld;
    }
    
    @Override
    public String getEmployer()
    {
      return employer;
    }

    @Override
    public double getTaxWithheld()
    {
      return taxWithheld;
    }

    @Override
    public double getWages()
    {
      return wages;
    }
    
  }
}
