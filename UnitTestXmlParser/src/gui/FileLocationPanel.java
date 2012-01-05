package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;
import domain.combined.AllTests;
import domain.junit.testrun;
import domain.mstest.TestRun;
import domain.questionvalues.UnitTestInfoFile;
import domain.scoring.TestScores;

public class FileLocationPanel extends JPanel {

   private BrowsePanel java, cSharp, testWeights;
   private JButton go;

   public FileLocationPanel() {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      java = new BrowsePanel("Junit Test Result Location",
            "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/junit.xml");
      cSharp = new BrowsePanel("MSTest Result Location",
            "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/mstest.xml");
      testWeights = new BrowsePanel("Unit Test Weight File",
            "C:/Users/U0117691/workspace2/JNUnitXmlFileParser/resources/round1weights.txt");
      go = new JButton("Calculate Scores");
      go.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               AllTests scores = new AllTests(loadSeeSharpTestScores(cSharp.getSelectedLocation()),
                     loadJavaTestScores(java.getSelectedLocation()), loadUnitTestInfoFile(testWeights
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
      locationText.setFont(new Font("monospace", Font.PLAIN, 12));

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
