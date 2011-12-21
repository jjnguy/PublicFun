package domain.mstest;

/**
 * Encapsulates all of the data from the C# test run
 * 
 * @author Justin Nelson
 * 
 */
public class TestRun {

   public String id;
   public String name;
   public String runUser;
   public String xmlns;

   public TestSettings TestSettings;
   public Times Times;
   public ResultSummary ResultSummary;
   public TestDefinitions TestDefinitions;
   public TestLists TestLists;
   public TestEntries TestEntries;
   public Results Results;
}
