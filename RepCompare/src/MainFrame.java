import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
        addUser = new AddUserDialog();
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
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                } catch (JSONException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (ParameterNotSetException e1) {
                    // TODO Auto-generated catch block
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParameterNotSetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private class AddUserDialog extends JDialog {

        private JComboBox sites;
        private JTextField id;
        private JButton ok;

        private AddUserDialog() throws IOException, JSONException {
            JPanel mainPane = new JPanel();
            sites = new JComboBox();
            id = new JTextField(8);
            ok = new JButton("Ok");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    setVisible(false);
                }
            });
            mainPane.add(sites);
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
            setVisible(true);
            repGraph.addUser(sites.getSelectedItem().toString(), Integer.parseInt(id.getText()));
        }

    }
}
