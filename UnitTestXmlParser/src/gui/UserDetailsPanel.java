package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import domain.combined.CombinedSingleTest;
import domain.scoring.UnitTestScore;

public class UserDetailsPanel extends JPanel {
   private Set<CombinedSingleTest> testReults;
   private JLabel userName;
   private JLabel total;
   private JList resultListDisplay;

   public UserDetailsPanel(String userName, Set<CombinedSingleTest> testReults, double score) {
      setLayout(new BorderLayout());
      this.testReults = testReults;
      List<String> data = new ArrayList<String>();
      for (CombinedSingleTest result : testReults) {
         data.add(result.singleInfo.questionName);
      }
      resultListDisplay = new JList(data.toArray(new String[data.size()]));
      resultListDisplay.setCellRenderer(new TestListCellRenderer(testReults));
      this.userName = new JLabel("Name: " + userName);
      this.total = new JLabel("Total: " + score);
      JPanel northPanel = new JPanel();
      northPanel.add(this.userName);
      northPanel.add(this.total);
      add(northPanel, BorderLayout.NORTH);
      add(resultListDisplay, BorderLayout.CENTER);
   }

   private static class TestListCellRenderer extends DefaultListCellRenderer {
      private Map<String, CombinedSingleTest> scores;

      public TestListCellRenderer(Set<CombinedSingleTest> scores) {
         this.scores = new HashMap<String, CombinedSingleTest>();
         for (CombinedSingleTest score : scores) {
            this.scores.put(score.singleInfo.questionName, score);
         }
      }

      @Override
      public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         CombinedSingleTest score = scores.get(value);
         setText(score.singleInfo.questionName + " - " + score.score() + "/" + score.singleInfo.weight);
         Color myRed = new Color(237, 148, 150);
         Color myYellow = new Color(233, 240, 145);
         Color myGreen = new Color(146, 239, 162);
         if (score.score() <= 0) {
            // red
            setBackground(myRed);
         } else if (score.score() < score.singleInfo.weight) {
            // yellow
            setBackground(myYellow);
         } else if (score.score() == score.singleInfo.weight) {
            // green
            setBackground(myGreen);
         } else {
            throw new IllegalStateException(
                  "The user shouldn't be able to score more points than the question is worth.");
         }
         return this;
      }
   }
}
