import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.http.HttpClient;
import net.sf.stackwrap4j.json.JSONException;

public class MainFrame extends JFrame {

    private AddUserDialog addUser;
    private ReputationGraph repGraph;

    public MainFrame() throws IOException, JSONException, ParameterNotSetException {
        super("Reputation Graph Compare");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addUser = new AddUserDialog(this);
        repGraph = new ReputationGraph();
        add(repGraph);
        createMenu();
        pack();
        setVisible(true);
    }

    private void createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem addUserItem = new JMenuItem("Add User");
        final JCheckBox keyVilibleItem = new JCheckBox("Hide Key");
        keyVilibleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repGraph.setKeyVisibility(!keyVilibleItem.isSelected());
            }
        });
        addUserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addUser.showCreateUserDialog();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                } catch (ParameterNotSetException e1) {
                    e1.printStackTrace();
                }
            }
        });
        fileMenu.add(addUserItem);
        fileMenu.add(keyVilibleItem);
        bar.add(fileMenu);
        setJMenuBar(bar);
    }

    public static void main(String[] args) throws IOException, JSONException,
            ParameterNotSetException {
        // HttpClient.proxyServer = new Proxy(Proxy.Type.HTTP, new
        // InetSocketAddress("webproxy.int.westgroup.com", 80));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParameterNotSetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AddUserDialog extends JDialog {

        private boolean okPressed;
        
        private JComboBox sites;
        private JLabel label;
        private JTextField id;
        private JButton ok;

        private AddUserDialog(JFrame parent) throws IOException, JSONException {
            super(parent);
            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            JPanel mainPane = new JPanel();
            sites = new JComboBox();
            label = new JLabel("User ID: ");
            id = new JTextField(8);
            id.addKeyListener(enterPressed);
            sites.addKeyListener(enterPressed);
            ok = new JButton("Ok");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    okPressed = true;
                    setVisible(false);
                }
            });
            mainPane.add(sites);
            mainPane.add(label);
            mainPane.add(id);
            mainPane.add(ok);
            add(mainPane);
            setModal(true);
            for (String s : new StackWrapDataAccess().getAllSites()) {
                sites.addItem(s);
            }
            pack();
        }

        private void showCreateUserDialog() throws NumberFormatException, JSONException,
                IOException, ParameterNotSetException {
            okPressed = false;
            setVisible(true);
            if (okPressed)
                repGraph.addUser(sites.getSelectedItem().toString(), Integer.parseInt(id.getText()));
        }

        private KeyListener enterPressed = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    ok.doClick();
                }
            }
        };
    }
}
