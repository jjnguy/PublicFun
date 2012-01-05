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

import domain.combined.AllTests;
import domain.combined.CombinedSingleProblem;
import domain.combined.CombinedSingleTest;
import domain.scoring.UnitTestScore;

public class UserDetailsPanel extends JPanel {
   private AllTests allTests;
   private JLabel userName;
   private JLabel total;
   private JList resultListDisplay;

   public UserDetailsPanel(String userName, AllTests allTests, double score) {
      setLayout(new BorderLayout());
      this.allTests = allTests;
      List<String> data = new ArrayList<String>();
      for (CombinedSingleProblem result : allTests.allProblemsForUser(userName)) {
         data.add(result.problemName);
      }
      resultListDisplay = new JList(data.toArray(new String[data.size()]));
      resultListDisplay.setCellRenderer(new TestListCellRenderer(allTests, userName));
      this.userName = new JLabel("Name: " + userName);
      this.total = new JLabel("Total: " + score);
      JPanel northPanel = new JPanel();
      northPanel.add(this.userName);
      northPanel.add(this.total);
      add(northPanel, BorderLayout.NORTH);
      add(resultListDisplay, BorderLayout.CENTER);
   }

   private static class TestListCellRenderer extends DefaultListCellRenderer {
      private AllTests allTests;
      private String userName;

      public TestListCellRenderer(AllTests allTests, String userName) {
         this.allTests = allTests;
         this.userName = userName;
      }

      @Override
      public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         CombinedSingleProblem score = allTests.singleProblemForUser(userName, value.toString());
         setText(value.toString() + " - " + score.score() + "/" + score.totalPossible());
         Color myRed = new Color(237, 148, 150);
         Color myYellow = new Color(233, 240, 145);
         Color myGreen = new Color(146, 239, 162);
         if (score.score() <= 0) {
            // red
            setBackground(myRed);
         } else if (score.score() < score.totalPossible()) {
            // yellow
            setBackground(myYellow);
         } else if (score.score() == score.totalPossible()) {
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
