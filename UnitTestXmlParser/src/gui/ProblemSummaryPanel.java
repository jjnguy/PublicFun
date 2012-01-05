package gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;

import domain.combined.AllTests;
import domain.scoring.TestScores;

public class ProblemSummaryPanel extends JPanel {

   private AllTests scores;

   private JList scoresList;

   public ProblemSummaryPanel(AllTests scores) {
      this.scores = scores;
      scoresList = new JList(scores.testNames.toArray());
      scoresList.setCellRenderer(new ProblemListCellRendereer(scores));
      add(scoresList);
   }

   private static class ProblemListCellRendereer extends DefaultListCellRenderer {
      private AllTests scores;

      public ProblemListCellRendereer(AllTests scores) {
         this.scores = scores;
      }

      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         String pName = value.toString();
         int passed = scores.getUsersWhoPassedTest(pName).size();
         int failed = scores.getUsersWhoFailedTest(pName).size();
         double percent = (passed / ((double) passed + failed)) * 100;
         setText(pName + " - " + percent + "%");
         return this;
      }
   }
}
