package domain.questionvalues;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class QuestionWeightFile {
   public final List<QuestionWeight> questions;

   private QuestionWeightFile(List<QuestionWeight> questions) {
      this.questions = Collections.unmodifiableList(questions);
   }

   public static QuestionWeightFile parseFile(String fileLocation) throws FileNotFoundException {
      List<QuestionWeight> questions = new ArrayList<QuestionWeight>();

      Scanner fin = new Scanner(new File(fileLocation));
      while (fin.hasNextLine()) {
         String line = fin.nextLine();
         QuestionWeight item = QuestionWeight.parse(line);
         questions.add(item);
      }
      
      return new QuestionWeightFile(questions);
   }
}
