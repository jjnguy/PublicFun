package domain.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.mstest.TestRun;
import domain.mstest.UnitTestResult;
import domain.questionvalues.QuestionWeight;
import domain.questionvalues.QuestionWeightFile;

public class TestScores {

   private Map<String, List<QuestionScore>> resultsByPerson;

   public TestScores(QuestionWeightFile questions, TestRun results) {
      resultsByPerson = new HashMap<String, List<QuestionScore>>();
      for (QuestionWeight question : questions.questions) {
         if (!resultsByPerson.containsKey(question.participantName)) {
            resultsByPerson.put(question.participantName, new ArrayList<QuestionScore>());
         }
         String combinedTestName = question.participantName + "_" + question.questionName + "_tests";
         UnitTestResult testResults = results.Results.getResultForTest(combinedTestName);
         System.out.println(testResults.outcome);
         QuestionScore score = new QuestionScore(question, !testResults.outcome.equals("Failed") ? question.weight : 0);
         resultsByPerson.get(question.participantName).add(score);
      }
   }

   public double score(String person) {
      double sum = 0;
      for (QuestionScore score : resultsByPerson.get(person)) {
         sum += score.score;
      }
      return sum;
   }

   public Set<String> participants() {
      return resultsByPerson.keySet();
   }
}
