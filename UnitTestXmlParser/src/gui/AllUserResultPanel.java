package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.combined.AllTests;
import domain.combined.CombinedSingleTest;
import domain.junit.testrun;
import domain.mstest.TestRun;
import domain.questionvalues.SingleUnitTestInfo;
import domain.questionvalues.UnitTestInfoFile;
import domain.scoring.TestScores;
import domain.scoring.UnitTestScore;

public class AllUserResultPanel extends JFrame {
   private static final long serialVersionUID = 1L;

   private AllTests data;

   private UserSummaryPanel summary;
   private JList users;
   private JButton sortByName, sortByScore;
   private JButton exportData;

   public AllUserResultPanel(AllTests data) {
      super("Test Results");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());
      this.data = data;
      summary = new UserSummaryPanel(data);
      users = new JList(data.userNames.toArray(new String[data.userNames.size()]));
      users.setCellRenderer(new UserCellRenderer(data));
      users.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            int idx = users.locationToIndex(evt.getPoint());
            String clickedParticipant = users.getModel().getElementAt(idx).toString();
            summary.updateForUser(clickedParticipant);
            if (evt.getClickCount() == 2) {
               UserDetailsPanel details = new UserDetailsPanel(clickedParticipant, AllUserResultPanel.this.data,
                     AllUserResultPanel.this.data.getScoreForUser(clickedParticipant));
               JDialog dialog = new JDialog();
               dialog.add(details);
               dialog.pack();
               dialog.setVisible(true);
            }
         }
      });
      sortByName = new JButton("Sort By Name");
      sortByName.addActionListener(sortingListener(new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            return o1.compareTo(o2);
         }
      }));
      sortByScore = new JButton("Sort By Score");
      sortByScore.addActionListener(sortingListener(new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            return -new Double(AllUserResultPanel.this.data.getScoreForUser(o1)).compareTo(new Double(
                  AllUserResultPanel.this.data.getScoreForUser(o2)));
         }
      }));
      exportData = new JButton("Export Data");
      exportData.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int choice = chooser.showSaveDialog(AllUserResultPanel.this);
            if (choice != JFileChooser.APPROVE_OPTION) {
               return;
            }
            try {
               exportToCsv(chooser.getSelectedFile().getAbsolutePath());
            } catch (FileNotFoundException e1) {
               e1.printStackTrace();
            }
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
      pack();
   }

   private void exportToCsv(String location) throws FileNotFoundException {
      PrintStream out = new PrintStream(new File(location));
      List<String> uniqueTestNames = new ArrayList<String>(data.allTests.keySet());
      Collections.sort(uniqueTestNames);
      out.print("User Name,Total,");
      String currentUserName = data.testInfo.testByUniqueName.get(uniqueTestNames.get(0)).participantName;
      for (String uniqueName : uniqueTestNames) {
         SingleUnitTestInfo singleInfo = data.testInfo.testByUniqueName.get(uniqueName);
         if (!singleInfo.participantName.equals(currentUserName)) {
            out.println();
            break;
         }
         out.print(singleInfo.questionName + " - " + singleInfo.testName + ",");
      }
      out.print(data.testInfo.testByUniqueName.get(uniqueTestNames.get(0)).participantName + "," + data.getScoreForUser(currentUserName) + ",");
      for (String uniqueName : uniqueTestNames) {
         SingleUnitTestInfo singleInfo = data.testInfo.testByUniqueName.get(uniqueName);
         if (!singleInfo.participantName.equals(currentUserName)) {
            out.println();
            currentUserName = singleInfo.participantName;
            out.print(singleInfo.participantName + "," + data.getScoreForUser(currentUserName) + ",");
         }
         out.print(data.getScoreByUniqueName(uniqueName) + ",");
      }
      out.close();
   }

   private ActionListener sortingListener(final Comparator<String> sortMethod) {
      return new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            Object selected = users.getSelectedValue();
            List<String> dataList = new ArrayList<String>(data.userNames);
            Collections.sort(dataList, sortMethod);
            users.setListData(dataList.toArray(new String[dataList.size()]));
            users.setSelectedValue(selected, true);
         }
      };
   }
}
