import java.io.FileNotFoundException;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import xmlcomponents.autoparse.ClassCreation;
import domain.mstest.TestRun;

public class Main {
   public static void main(String[] args) throws FileNotFoundException {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/testResult.xml";
      String otherFileLocation = "D:/Development/Foldering/TestResults/MergedTestResults.xml";
      Jode j = Jocument.load(fileLocation, "TestRun");
      ClassCreation.createClasses(j, "C:/Users/U0117691/Desktop/output");
      TestRun tesstRun = j.toObject(TestRun.class);
      //TestResultXmlFile file = new TestResultXmlFile(fileLocation);
      System.out.println("WooHoo");
   }
}
