package domain.questionvalues;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a file containing a list of Unit test information
 * 
 * @author Justin Nelson
 */
public class UnitTestInfoFile {
   public final List<SingleUnitTestInfo> questions;

   private UnitTestInfoFile(List<SingleUnitTestInfo> questions) {
      this.questions = Collections.unmodifiableList(questions);
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
}
