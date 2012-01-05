package domain.questionvalues;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents a file containing a list of Unit test information
 * 
 * @author Justin Nelson
 */
public class UnitTestInfoFile implements Iterable<SingleUnitTestInfo> {
   public final List<SingleUnitTestInfo> allTests;
   public final Map<String, List<SingleUnitTestInfo>> testsByQuestionName;
   public final Map<String, List<SingleUnitTestInfo>> testsByUserName;
   public final Map<String, SingleUnitTestInfo> testByUniqueName;

   private UnitTestInfoFile(List<SingleUnitTestInfo> questions) {
      this.allTests = Collections.unmodifiableList(questions);
      HashMap<String, List<SingleUnitTestInfo>> testsByQuestionName = new HashMap<String, List<SingleUnitTestInfo>>();
      HashMap<String, List<SingleUnitTestInfo>> testsByUserName = new HashMap<String, List<SingleUnitTestInfo>>();
      HashMap<String, SingleUnitTestInfo> testByUniqueName = new HashMap<String, SingleUnitTestInfo>();
      for (SingleUnitTestInfo singleItem : questions) {
         if (!testsByQuestionName.containsKey(singleItem.questionName)) {
            testsByQuestionName.put(singleItem.questionName, new ArrayList<SingleUnitTestInfo>());
         }
         if (!testsByUserName.containsKey(singleItem.participantName)) {
            testsByQuestionName.put(singleItem.participantName, new ArrayList<SingleUnitTestInfo>());
         }
         testByUniqueName.put(singleItem.fullTestName(), singleItem);
         testsByQuestionName.get(singleItem.questionName).add(singleItem);
         testsByUserName.get(singleItem.participantName).add(singleItem);
      }
      this.testByUniqueName = Collections.unmodifiableMap(testByUniqueName);
      this.testsByQuestionName = Collections.unmodifiableMap(testsByQuestionName);
      this.testsByUserName = Collections.unmodifiableMap(testsByUserName);
   }

   public static UnitTestInfoFile parseFile(String fileLocation) throws FileNotFoundException {
      List<SingleUnitTestInfo> questions = new ArrayList<SingleUnitTestInfo>();

      Scanner fin = new Scanner(new File(fileLocation));
      while (fin.hasNextLine()) {
         String line = fin.nextLine();
         SingleUnitTestInfo item = SingleUnitTestInfo.parse(line);
         questions.add(item);
      }

      return new UnitTestInfoFile(questions);
   }

   @Override
   public Iterator<SingleUnitTestInfo> iterator() {
      return allTests.iterator();
   }
}
