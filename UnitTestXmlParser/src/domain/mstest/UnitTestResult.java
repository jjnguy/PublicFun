package domain.mstest;

import xmlcomponents.autoparse.annotation.XmlProperty;

public class UnitTestResult {
   public String executionId;
   public String testId;
   public String testName;
   public String computerName;
   public String duration;
   public String startTime;
   public String endTime;
   public String testType;
   public String outcome;
   public String testListId;
   public String relativeResultsDirectory;
   @XmlProperty(optional = true)
   public Output Output;

   public boolean failed() {
      return outcome.equals("Failed");
   }
}
