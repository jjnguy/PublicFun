package domain.combined;

import java.util.Collections;
import java.util.Set;

public class CombinedSingleProblem {
   public final Set<CombinedSingleTest> tests;
public final String problemName;
   public CombinedSingleProblem(String problemName, Set<CombinedSingleTest> tests) {
      this.tests = Collections.unmodifiableSet(tests);
      this.problemName = problemName;
   }

   public double score() {
      double score = 0;
      for (CombinedSingleTest test : tests) {
         score += test.score();
      }
      return score;
   }

   public double totalPossible() {
      double weight = 0;
      for (CombinedSingleTest test : tests) {
         weight += test.singleInfo.weight;
      }
      return weight;
   }
}
