package domain.scoring;

import domain.questionvalues.QuestionWeight;

public class QuestionScore {
   public final QuestionWeight question;
   public final double score;

   public QuestionScore(QuestionWeight question, double score) {
      this.question = question;
      this.score = score;
   }
}
