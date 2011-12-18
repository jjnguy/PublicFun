import java.io.FileNotFoundException;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.mstest.TestRun;
import domain.questionvalues.QuestionWeightFile;
import domain.scoring.TestScores;

public class Main {
   public static void main(String[] args) throws FileNotFoundException {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/testResult.xml";
      String otherFileLocation = "D:/Development/Foldering/TestResults/MergedTestResults.xml";
      Jode j = Jocument.load(fileLocation, "TestRun");
      TestRun tesstRun = j.toObject(TestRun.class);
      System.out.println("WooHoo");
      QuestionWeightFile file = QuestionWeightFile
            .parseFile("C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/round1weights.txt");
      System.out.println(file);
      TestScores scores = new TestScores(file, tesstRun);
      for (String name : scores.participants()) {
         System.out.println(name + ": " + scores.score(name));
      }
   }
}
