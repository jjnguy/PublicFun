package domain.combined;

import domain.junit.testcase;
import domain.mstest.UnitTestResult;
import domain.questionvalues.SingleUnitTestInfo;

public class CombinedSingleTest {

   private testcase junitTestCase;
   private UnitTestResult mstestCase;
   public final SingleUnitTestInfo singleInfo;

   public CombinedSingleTest(testcase junitTestCase, UnitTestResult mstestCase, SingleUnitTestInfo singleInfo) {
      this.junitTestCase = junitTestCase;
      this.mstestCase = mstestCase;
      this.singleInfo = singleInfo;
   }

   public boolean passed() {
      return !junitTestCase.failed() || !mstestCase.failed();
   }

   public double score() {
      return passed() ? singleInfo.weight : 0;
   }
}
