package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.junit.testrun;
import domain.mstest.TestRun;
import domain.questionvalues.UnitTestInfoFile;
import domain.scoring.TestScores;

public class FileLocationPanel extends JPanel {

   private BrowsePanel java, cSharp, testWeights;
   private JButton go;

   public FileLocationPanel() {
      java = new BrowsePanel("Junit Test Result Location", "");
      cSharp = new BrowsePanel("MSTest Result Location", "");
      testWeights = new BrowsePanel("Unit Test Weight File", "");
      go = new JButton("Calculate Scores");
      go.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               TestScores scores = new TestScores(loadUnitTestInfoFile(testWeights.getSelectedLocation()),
                     loadSeeSharpTestScores(cSharp.getSelectedLocation()), loadJavaTestScores(java
                           .getSelectedLocation()));
               JFrame rootWindow = new AllUserResultPanel(scores);
               rootWindow.setVisible(true);
            } catch (FileNotFoundException e1) {
               e1.printStackTrace();
            }
         }
      });
      add(java);
      add(cSharp);
      add(testWeights);
      add(go);
   }

   private static TestRun loadSeeSharpTestScores(String location) {
      Jode seeSharp = Jocument.load(location, "TestRun");
      TestRun seeSharpTestRun = seeSharp.toObject(TestRun.class);
      return seeSharpTestRun;
   }

   private static testrun loadJavaTestScores(String location) {
      Jode java = Jocument.load(location, "testrun");
      testrun javaTestRun = java.toObject(testrun.class);
      return javaTestRun;
   }

   private static UnitTestInfoFile loadUnitTestInfoFile(String location) throws FileNotFoundException {
      UnitTestInfoFile file = UnitTestInfoFile.parseFile(location);
      return file;
   }
}

class BrowsePanel extends JPanel {
   private JLabel label;
   private JTextField locationText;
   private JButton browse;

   public BrowsePanel(String labelText, String defaultLocation) {
      super(new BorderLayout());
      label = new JLabel(labelText);
      locationText = new JTextField(defaultLocation == null ? "" : defaultLocation);
      browse = new JButton("Browse");

      locationText.setColumns(80);

      browse.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            JFileChooser chooser = new JFileChooser();
            int choice = chooser.showOpenDialog(BrowsePanel.this);
            if (choice != JFileChooser.APPROVE_OPTION)
               return;
            locationText.setText(chooser.getSelectedFile().getAbsolutePath());
         }
      });

      add(label, BorderLayout.WEST);
      add(locationText, BorderLayout.CENTER);
      add(browse, BorderLayout.EAST);
   }

   public String getSelectedLocation() {
      return locationText.getText();
   }
}
