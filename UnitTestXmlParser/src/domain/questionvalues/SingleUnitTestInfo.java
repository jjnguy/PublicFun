package domain.questionvalues;

/**
 * Represents a test, including its name the person who it's testing, and how many points it is worth.
 * 
 * @author Justin Nelson
 */
public class SingleUnitTestInfo {

   public final String participantName;
   public final String questionName;
   public final double weight;

   private SingleUnitTestInfo(String participantName, String questionName, double weight) {
      this.participantName = participantName;
      this.questionName = questionName;
      this.weight = weight;
   }

   public static SingleUnitTestInfo parse(String line) {
      String[] splitLine = line.trim().split("\t|_");
      String participantName = splitLine[0];
      String questionName = splitLine[1];
      double weight = Double.parseDouble(splitLine[3]);
      SingleUnitTestInfo item = new SingleUnitTestInfo(participantName, questionName, weight);
      return item;
   }
}
