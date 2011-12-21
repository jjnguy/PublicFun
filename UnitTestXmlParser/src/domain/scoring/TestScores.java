package domain.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.mstest.TestRun;
import domain.mstest.UnitTestResult;
import domain.questionvalues.SingleUnitTestInfo;
import domain.questionvalues.UnitTestInfoFile;

/**
 * Represents all of the users in the system and all of their results on their tests
 * 
 * @author Justin Nelson
 */
public class TestScores {

   private Map<String, List<UnitTestScore>> resultsByPerson;

   public TestScores(UnitTestInfoFile questions, TestRun results) {
      resultsByPerson = new HashMap<String, List<UnitTestScore>>();
      for (SingleUnitTestInfo question : questions.questions) {
         if (!resultsByPerson.containsKey(question.participantName)) {
            resultsByPerson.put(question.participantName, new ArrayList<UnitTestScore>());
         }
         String combinedTestName = question.participantName + "_" + question.questionName + "_tests";
         UnitTestResult testResults = results.Results.getResultForTest(combinedTestName);
         System.out.println(testResults.outcome);
         UnitTestScore score = new UnitTestScore(question, !testResults.outcome.equals("Failed") ? question.weight : 0);
         resultsByPerson.get(question.participantName).add(score);
      }
   }

   public Set<String> getUsersWhoPassedProblem(String problemName) {
      Set<String> result = new HashSet<String>();
      for (String key : resultsByPerson.keySet()) {
         for (UnitTestScore score : resultsByPerson.get(key)) {
            if (score.question.questionName.equals(problemName) && score.score > 0)
               result.add(key);
         }
      }
      return result;
   }

   public Set<String> getUsersWhoFailedProblem(String problemName) {
      Set<String> result = new HashSet<String>();
      for (String key : resultsByPerson.keySet()) {
         for (UnitTestScore score : resultsByPerson.get(key)) {
            if (score.question.questionName.equals(problemName) && score.score == 0)
               result.add(key);
         }
      }
      return result;
   }

   public Set<String> getAllProblems() {
      Set<String> result = new HashSet<String>();
      for (String key : resultsByPerson.keySet()) {
         for (UnitTestScore score : resultsByPerson.get(key)) {
            result.add(score.question.questionName);
         }
      }
      return result;
   }

   public double score(String person) {
      double sum = 0;
      for (UnitTestScore score : resultsByPerson.get(person)) {
         sum += score.score;
      }
      return sum;
   }

   public List<UnitTestScore> getScoresForParticipant(String participant) {
      return resultsByPerson.get(participant);
   }

   public Set<String> participants() {
      return resultsByPerson.keySet();
   }
}
