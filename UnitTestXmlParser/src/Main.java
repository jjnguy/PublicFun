import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import xmlcomponents.autoparse.AutoParser;
import domain.TestResultXmlFile;
import domain.mstest.TestRun;

public class Main {
   public static void main(String[] args) {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/testResult.xml";
      String otherFileLocation = "D:/Development/Foldering/TestResults/MergedTestResults.xml";
      Jode j = Jocument.load(fileLocation, "TestRun");
      TestRun tesstRun = j.toObject(TestRun.class);
      //TestResultXmlFile file = new TestResultXmlFile(fileLocation);
      System.out.println("WooHoo");
   }
}
