import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import xmlcomponents.autoparse.AutoParser;
import domain.TestResultXmlFile;
import domain.mstest.TestRun;

public class Main {
   public static void main(String[] args) {
      String fileLocation = "C:\\Users\\U0117691\\Documents\\brian_NEON 2011-12-07 14_32_01.trx";
      String otherFileLocation = "D:/Development/Foldering/TestResults/MergedTestResults.xml";
      Jode j = Jocument.load(fileLocation, "TestRun");
      TestRun tesstRun = AutoParser.parseV2(j, TestRun.class);
      //TestResultXmlFile file = new TestResultXmlFile(fileLocation);
      System.out.println("WooHoo");
   }
}
