package domain.mstest;

import xmlcomponents.Jocument;

public class TestRun {
   
   private String id;
   
   private TestSettings testSettings;
   private Times times;
   private ResultSummary resultSummary;
   private TestDefinitions testDefinitions;
   private TestLists testLists;
   private TestEntries testEntries;
   private Results results;
   public TestRun(){};
   public TestRun(String fileLocation) {
      Jocument j = Jocument.load(fileLocation);
      testSettings = new TestSettings(j.single("TestSettings"));
      times = new Times(j.single("Times"));
      resultSummary = new ResultSummary(j.single("ResultSummary"));
      testDefinitions = new TestDefinitions(j.single("TestDefinitions"));
      testLists = new TestLists(j.single("TestLists"));
      testEntries = new TestEntries(j.single("TestEntries"));
      results = new Results(j.single("Results"));
   }
}
