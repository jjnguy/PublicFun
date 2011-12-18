package domain.mstest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Results {

   public List<UnitTestResult> UnitTestResult;

   public Set<String> getTestNames() {
      Set<String> names = new HashSet<String>();
      for (UnitTestResult result : UnitTestResult) {
         names.add(result.testName);
      }
      return names;
   }

   public UnitTestResult getResultForTest(String testName) {
      for (UnitTestResult result : UnitTestResult) {
         if (result.testName.equals(testName)) {
            return result;
         }
      }
      return null;
   }
}
