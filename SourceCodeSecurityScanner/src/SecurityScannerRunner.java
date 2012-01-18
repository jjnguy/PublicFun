import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;

public class SecurityScannerRunner {
   public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException,
         IllegalAccessException, UnsupportedLookAndFeelException {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      new DirectoryInputFrame();
   }

   private static void scanDirectory(File root) throws FileNotFoundException {
      if (!root.exists()) {
         throw new FileNotFoundException("The supplied file doesn't exist: " + root.getAbsolutePath());
      }
      List<SecurityException> failures = new ArrayList<SecurityException>();
      if (root.isFile()) {
         scanFile(root);
      } else {
         for (File child : root.listFiles()) {
            try {
               scanDirectory(child);
            } catch (SecurityException se) {
               failures.add(se);
            }
         }
      }
      if (!failures.isEmpty()) {
         StringBuilder masterError = new StringBuilder();
         for (SecurityException se : failures) {
            masterError.append(se.getMessage());
         }
         throw new SecurityException(masterError.toString());
      }
   }

   private static void scanFile(File file) throws FileNotFoundException {
      if (file.isDirectory())
         throw new IllegalArgumentException("The given file was actually a directoy. - Justin probably screwed up.");
      if (!(file.getName().endsWith(".cs") || file.getName().endsWith(".java")))
         return;
      if (isFileWhiteList(file)) {
         return;
      }
      StringBuilder fileText = new StringBuilder();
      Scanner fin = new Scanner(file);
      String line;
      while (fin.hasNextLine()) {
         line = fin.nextLine().trim();
         if (line.startsWith("//"))
            continue;
         fileText.append(line);
      }
      scanText(fileText.toString().replaceAll("\\s+", ""), file.getAbsolutePath());
   }

   private static boolean isFileWhiteList(File file) {
      return (file.getName().equalsIgnoreCase("Application.java") //
            || file.getName().equalsIgnoreCase("Application.cs")//
            || file.getName().equalsIgnoreCase("CodeCompHelpers.java")//
            || file.getName().equalsIgnoreCase("CodeCompTests.java")//
            || file.getName().equalsIgnoreCase("CodeCompTests.cs") //
            || file.getName().equalsIgnoreCase("AssemblyInfo.cs"));
   }

   private static void scanText(String text, String fileLocation) {
      List<String> violations = new ArrayList<String>();
      for (BlackListRegexes regex : BlackListRegexes.values()) {
         violations.addAll(regex.findViolations(text));
      }
      if (!violations.isEmpty()) {
         StringBuilder message = new StringBuilder("\n> '").append(fileLocation).append("' contained ")
               .append(violations.size()).append(" potentially dangerous statements. -");
         for (String violation : violations) {
            message.append("\n  ").append(violation);
         }
         throw new SecurityException(message.toString());
      }
   }

   public static enum BlackListRegexes {
      NO_FORCE_EXIT("(System|Environment)\\.[eE]xit\\(.+?\\);", true), //
      NO_EXTERNAL_PROCS("(ProcessBuilder|Runtime(?!Exception)|Process|ProcessStartInfo).*?;", true), //
      NO_BAD_SQL("\"[^\"]*?(DELETE|DROP(?!down)|TRUNCATE|INSERT)[^\"]*?\"", false), //
      NO_JAVA_IO("java\\.io.*?;", true), //
      NO_C$_IO("System\\.IO.*?;", true);//

      private Pattern p;

      public final String regex;
      public final boolean caseSensitive;

      private BlackListRegexes(String regex, boolean caseSensitive) {
         this.regex = regex;
         this.caseSensitive = caseSensitive;
         this.p = Pattern.compile(this.regex, this.caseSensitive ? 0 : Pattern.CASE_INSENSITIVE);
      }

      public List<String> findViolations(String inputText) {
         List<String> violations = new ArrayList<String>();
         Matcher m = p.matcher(inputText);
         while (m.find()) {
            violations.add(m.group());
         }
         return violations;
      }
   }

   public static class DirectoryInputFrame extends JFrame {
      public DirectoryInputFrame() {
         super("Dangerous File Scanner");
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         final JButton scan = new JButton("Select and scan!");
         scan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
               final JFileChooser chooser = new JFileChooser();
               chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               int choice = chooser.showOpenDialog(DirectoryInputFrame.this);
               if (choice != JFileChooser.APPROVE_OPTION)
                  return;
               SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                  @Override
                  protected void done() {
                     super.done();
                     scan.setEnabled(true);
                     scan.setText("Select and scan!");
                     JOptionPane.showMessageDialog(DirectoryInputFrame.this,
                           "Done scanning files.  Results can be found on your desktop.");
                  }

                  @Override
                  protected Void doInBackground() throws Exception {
                     String fileBody = "";
                     FileSystemView filesys = FileSystemView.getFileSystemView();
                     try {
                        scanDirectory(chooser.getSelectedFile());
                     } catch (SecurityException se) {
                        fileBody += "The scan completed sucessfully and threats were found.  See below.  \n\n"
                              + se.getMessage();
                     } catch (Exception e) {
                        fileBody += "The scan did not complete sucessfully.  The error was:\n\n" + e.getMessage();
                     }
                     if (fileBody.equals("")) {
                        fileBody = "The scan completed sucessfully and no threats were found.";
                     }
                     String timestamp = "";
                     Calendar c = Calendar.getInstance();
                     timestamp += c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE);
                     File toSaveTo = new File(filesys.getHomeDirectory().getAbsolutePath() + "\\security_report_"
                           + timestamp + ".txt");
                     PrintStream out = new PrintStream(toSaveTo);
                     out.print(fileBody);
                     out.close();
                     return null;
                  }
               };
               scan.setEnabled(false);
               scan.setText("Working...");
               worker.execute();
            }
         });
         add(scan);
         pack();
         setVisible(true);
      }
   }
}
