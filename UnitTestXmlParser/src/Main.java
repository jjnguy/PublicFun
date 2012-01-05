import gui.FileLocationPanel;

import java.io.FileNotFoundException;

import javax.swing.JFrame;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.junit.testrun;
import domain.mstest.TestRun;
import domain.questionvalues.UnitTestInfoFile;


public class Main {

   public static void main(String[] args) throws FileNotFoundException {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/mstest.xml";
      Jode seeSharp = Jocument.load(fileLocation, "TestRun");
      TestRun seeSharpTestRun = seeSharp.toObject(TestRun.class);
      String fileLocation2 = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/junit.xml";
      Jode java = Jocument.load(fileLocation2, "testrun");
      testrun javaTestRun = java.toObject(testrun.class);
      UnitTestInfoFile file = UnitTestInfoFile
            .parseFile("C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/round1weights.txt");
      JFrame f = new JFrame("Coding Competition Results");
      f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      f.add(new FileLocationPanel());
      f.pack();
      f.setVisible(true);
   }
}
