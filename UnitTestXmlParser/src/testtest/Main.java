package testtest;
import java.io.FileNotFoundException;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import xmlcomponents.autoparse.ClassCreation;
import domain.mstest.TestRun;
import domain.questionvalues.UnitTestInfoFile;

public class Main {
   public static void main(String[] args) throws FileNotFoundException {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/testResult.xml";
      String junit_fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/sample_junit.xml";
      Jode j = Jocument.load(junit_fileLocation, "testrun");
      //TestRun tesstRun = j.toObject(TestRun.class);
      System.out.println("WooHoo");
      UnitTestInfoFile file = UnitTestInfoFile
            .parseFile("C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/round1weights.txt");
      System.out.println(file);
      ClassCreation.createClasses(j, "C:\\Users\\U0117691\\Desktop\\output");
      // TestScores scores = new TestScores(file, tesstRun);
   }
}
