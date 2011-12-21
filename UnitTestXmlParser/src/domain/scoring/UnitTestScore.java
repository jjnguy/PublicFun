package domain.scoring;

import domain.questionvalues.SingleUnitTestInfo;

/**
 * Represents the score a user got on a specific unit test
 * 
 * @author Justin Nelson
 */
public class UnitTestScore {
   public final SingleUnitTestInfo question;
   public final double score;

   public UnitTestScore(SingleUnitTestInfo question, double score) {
      this.question = question;
      this.score = score;
   }
}
