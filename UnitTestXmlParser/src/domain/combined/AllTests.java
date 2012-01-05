package domain.combined;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import domain.junit.testrun;
import domain.mstest.TestRun;
import domain.questionvalues.SingleUnitTestInfo;
import domain.questionvalues.UnitTestInfoFile;

public class AllTests {
   public final UnitTestInfoFile testInfo;
   public final Map<String, CombinedSingleTest> allTests;
   public Set<String> problemNames;
   public Set<String> testNames;
   public Set<String> userNames;

   public AllTests(TestRun cSharp, testrun java, UnitTestInfoFile testInfo) {
      this.testInfo = testInfo;
      Map<String, CombinedSingleTest> allTests = new HashMap<String, CombinedSingleTest>();
      problemNames = new HashSet<String>();
      userNames = new HashSet<String>();
      testNames = new HashSet<String>();
      for (SingleUnitTestInfo info : testInfo) {
         allTests.put(info.fullTestName(), new CombinedSingleTest(java.getResultForTest(info.fullTestName()),
               cSharp.Results.getResultForTest(info.fullTestName()), info));
         problemNames.add(info.questionName);
         userNames.add(info.participantName);
         testNames.add(info.testName);
      }
      this.allTests = Collections.unmodifiableMap(allTests);
   }

   public double getScoreForUser(String userName) {
      double score = 0;
      for (SingleUnitTestInfo singleInfo : testInfo.testsByUserName.get(userName)) {
         CombinedSingleTest result = allTests.get(singleInfo.fullTestName());
         score += result.score();
      }
      return score;
   }

   public Set<CombinedSingleTest> getTestsForUserAndProblem(String userName, String problemName) {
      Set<CombinedSingleTest> results = new HashSet<CombinedSingleTest>();
      for (SingleUnitTestInfo singleInfo : testInfo.testsByUserName.get(userName)) {
         if (singleInfo.questionName.equals(problemName)) {
            CombinedSingleTest result = allTests.get(singleInfo.fullTestName());
            results.add(result);
         }
      }
      return results;
   }

   public double getScoreForUserAndProblem(String userName, String problemName) {
      double score = 0;
      for (SingleUnitTestInfo singleInfo : testInfo.testsByUserName.get(userName)) {
         if (singleInfo.questionName.equals(problemName)) {
            CombinedSingleTest result = allTests.get(singleInfo.fullTestName());
            score += result.score();
         }
      }
      return score;
   }

   public double getScoreByUniqueName(String uniqueName) {
      CombinedSingleTest result = allTests.get(uniqueName);
      return result.score();
   }

   public Set<CombinedSingleTest> allTestsForUser(String userName) {
      Set<CombinedSingleTest> result = new HashSet<CombinedSingleTest>();
      for (SingleUnitTestInfo singleInfo : testInfo.testsByUserName.get(userName)) {
         result.add(allTests.get(singleInfo.fullTestName()));
      }
      return result;
   }

   public Set<CombinedSingleProblem> allProblemsForUser(String userName) {
      Set<CombinedSingleProblem> results = new HashSet<CombinedSingleProblem>();
      for (String problemName : problemNames) {
         results.add(new CombinedSingleProblem(problemName, getTestsForUserAndProblem(userName, problemName)));
      }
      return results;
   }

   public CombinedSingleProblem singleProblemForUser(String userName, String problemName) {
      return new CombinedSingleProblem(problemName, getTestsForUserAndProblem(userName, problemName));
   }

   public Set<String> getUsersWhoPassedTest(String testName) {
      Set<String> results = new HashSet<String>();
      for (CombinedSingleTest result : allTests.values()) {
         if (result.singleInfo.testName.equals(testName) && result.passed()) {
            results.add(result.singleInfo.participantName);
         }
      }
      return results;
   }

   public Set<String> getUsersWhoFailedTest(String testName) {
      Set<String> results = new HashSet<String>();
      for (CombinedSingleTest result : allTests.values()) {
         if (result.singleInfo.testName.equals(testName) && !result.passed()) {
            results.add(result.singleInfo.participantName);
         }
      }
      return results;
   }
}
