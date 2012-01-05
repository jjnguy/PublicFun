package gui;

import java.awt.Component;
import java.awt.Font;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import domain.combined.AllTests;
import domain.combined.CombinedSingleTest;

public class UserCellRenderer extends DefaultListCellRenderer {
   private AllTests allScores;

   public UserCellRenderer(AllTests allScores) {
      this.allScores = allScores;
   }

   @Override
   public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected,
         boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      setFont(new Font("sans", Font.PLAIN, 18));
      setText(String.format("%-14s-%5.2f", value.toString(), allScores.getScoreForUser(value.toString())));
      final Set<CombinedSingleTest> usersScores = allScores.allTestsForUser(value.toString());
      return this;
   }
}
