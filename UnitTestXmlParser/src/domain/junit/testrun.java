package domain.junit;

import java.util.List;

public class testrun {

   private int errors;

   private int failures;

   private int ignored;

   private String name;

   private String project;

   private int started;

   private int tests;

   public List<testsuite> testsuite;

   public testcase getResultForTest(String testName) {
      for (testsuite s : testsuite) {
         for (testcase t : s.testcase) {
            if (t.name.equals(testName))
               return t;
         }
      }
      return null;
   }
}
