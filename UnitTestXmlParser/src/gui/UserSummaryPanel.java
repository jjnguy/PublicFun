package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.combined.AllTests;

public class UserSummaryPanel extends JPanel {
   private static final long serialVersionUID = 1L;

   private AllTests allScores;

   private JLabel nameLabel;
   private JLabel scoreLabel;

   public UserSummaryPanel(AllTests allScores) {
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
      this.allScores = allScores;
      JLabel nameLabel_label = new JLabel("Name: ");
      nameLabel = new JLabel();
      JLabel scoreLabel_label = new JLabel("Score: ");
      scoreLabel = new JLabel();
      add(nameLabel_label);
      add(nameLabel);
      add(scoreLabel_label);
      add(scoreLabel);
   }

   public void updateForUser(String user) {
      nameLabel.setText(user);
      scoreLabel.setText(allScores.getScoreForUser(user) + "");
   }
}