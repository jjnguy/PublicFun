package domain.questionvalues;

public class QuestionWeight {

   public String participantName;
   public String questionName;
   public double weight;

   public static QuestionWeight parse(String line) {
      String[] splitLine = line.trim().split("\t|_");
      String participantName = splitLine[0];
      String questionName = splitLine[1];
      double weight = Double.parseDouble(splitLine[3]);
      QuestionWeight item = new QuestionWeight();
      item.participantName = participantName;
      item.questionName = questionName;
      item.weight = weight;
      return item;
   }
}
