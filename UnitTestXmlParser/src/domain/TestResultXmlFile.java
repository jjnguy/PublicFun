package domain;
import java.util.List;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import xmlcomponents.manipulation.Xformer;


public class TestResultXmlFile {
   
   private List<TestSuite> suites;
   
   public TestResultXmlFile(String fileLocation){
      Jode joc = Jocument.load(fileLocation,"testsuites");
      suites = joc.children().xform(new Xformer<TestSuite>() {
         @Override
         public TestSuite xform(Jode j) {
            return new TestSuite(j);
         }
      });
   }
}
