package domain;
import java.util.List;

import xmlcomponents.Jode;
import xmlcomponents.autoparse.AutoParser;
import xmlcomponents.manipulation.Xformer;

public class TestSuite {

   private List<SuiteProperty> props;
   private List<TestCase> testCases;

   public TestSuite(Jode j) {
      props = j.single("properties").children().xform(new Xformer<SuiteProperty>() {
         @Override
         public SuiteProperty xform(Jode j) {
            return AutoParser.parse(j, SuiteProperty.class);
         }
      });
      testCases = j.children("testcase").xform(new Xformer<TestCase>() {
         @Override
         public TestCase xform(Jode j) {
            return AutoParser.parse(j, TestCase.class);
         }
      });
   }
}
