package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.mstest.TestRun;
import domain.questionvalues.UnitTestInfoFile;
import domain.scoring.TestScores;
import domain.scoring.UnitTestScore;

public class AllUserResultPanel extends JPanel {
   private static final long serialVersionUID = 1L;

   private TestScores data;

   private UserSummaryPanel summary;
   private JList users;
   private JButton sortByName, sortByScore;
   private JButton exportData;

   public AllUserResultPanel(TestScores data) {
      super(new BorderLayout());
      this.data = data;
      summary = new UserSummaryPanel(data);
      users = new JList(data.participants().toArray(new String[data.participants().size()]));
      users.setCellRenderer(new UserCellRenderer(data));
      users.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            int idx = users.locationToIndex(evt.getPoint());
            String clickedParticipant = users.getModel().getElementAt(idx).toString();
            summary.updateForUser(clickedParticipant);
            if (evt.getClickCount() == 2) {
               UserDetailsPanel details = new UserDetailsPanel(clickedParticipant, AllUserResultPanel.this.data
                     .getScoresForParticipant(clickedParticipant), AllUserResultPanel.this.data
                     .score(clickedParticipant));
               JDialog dialog = new JDialog();
               dialog.add(details);
               dialog.pack();
               dialog.setVisible(true);
            }
         }
      });
      sortByName = new JButton("Sort By Name");
      sortByName.addMouseListener(sortingListener(new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            return o1.compareTo(o2);
         }
      }));
      sortByScore = new JButton("Sort By Score");
      sortByScore.addMouseListener(sortingListener(new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            return -new Double(AllUserResultPanel.this.data.score(o1)).compareTo(new Double(
                  AllUserResultPanel.this.data.score(o2)));
         }
      }));
      exportData = new JButton("Export Data");
      exportData.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            JDialog dia = new JDialog();
            dia.add(new ExportPanel());
            dia.setVisible(true);
         }
      });
      JPanel westPanel = new JPanel(new BorderLayout());
      westPanel.add(new JLabel("User Name - Score"), BorderLayout.NORTH);
      JScrollPane scroll = new JScrollPane(users);
      westPanel.add(scroll, BorderLayout.CENTER);
      JPanel sortButtonPanel = new JPanel();
      sortButtonPanel.add(sortByName);
      sortButtonPanel.add(sortByScore);
      westPanel.add(sortButtonPanel, BorderLayout.SOUTH);
      add(westPanel, BorderLayout.WEST);
      add(new ProblemSummaryPanel(data), BorderLayout.CENTER);
      JPanel eastPane = new JPanel();
      eastPane.add(exportData);
      add(eastPane, BorderLayout.EAST);
   }

   private static class ExportPanel extends JPanel {

   }

   private MouseListener sortingListener(final Comparator<String> sortMethod) {
      return new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            Object selected = users.getSelectedValue();
            List<String> dataList = new ArrayList<String>(data.participants());
            Collections.sort(dataList, sortMethod);
            users.setListData(dataList.toArray(new String[dataList.size()]));
            users.setSelectedValue(selected, true);
         }
      };
   }

   private static class UserCellRenderer extends DefaultListCellRenderer {
      private TestScores allScores;

      public UserCellRenderer(TestScores allScores) {
         this.allScores = allScores;
      }

      @Override
      public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         setFont(new Font("sans", Font.PLAIN, 18));
         setText(String.format("%-14s-%5.2f", value.toString(), allScores.score(value.toString())));
         final List<UnitTestScore> usersScores = allScores.getScoresForParticipant(value.toString());
         return this;
      }
   }

   private static class UserSummaryPanel extends JPanel {
      private static final long serialVersionUID = 1L;

      private TestScores allScores;

      private JLabel nameLabel;
      private JLabel scoreLabel;

      public UserSummaryPanel(TestScores allScores) {
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
         scoreLabel.setText(allScores.score(user) + "");
      }
   }

   public static void main(String[] args) throws FileNotFoundException {
      String fileLocation = "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/testResult.xml";
      Jode j = Jocument.load(fileLocation, "TestRun");
      TestRun tesstRun = j.toObject(TestRun.class);
      UnitTestInfoFile file = UnitTestInfoFile
            .parseFile("C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/round1weights.txt");
      TestScores s = new TestScores(file, tesstRun);
      JFrame f = new JFrame("Coding Competition Results");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.add(new AllUserResultPanel(s));
      f.pack();
      f.setVisible(true);
   }
}
