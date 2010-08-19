import java.awt.Dimension;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.swing.JFrame;

import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.http.HttpClient;
import net.sf.stackwrap4j.json.JSONException;

public class MainFrame extends JFrame {

    private ReputationGraph repGraph;

    public MainFrame() throws IOException, JSONException, ParameterNotSetException {
        super("Reputation Graph Compare");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repGraph = new ReputationGraph();
        repGraph.addUser("Stack Overflow", 2598);
        repGraph.addUser("Stack Overflow", 34509);
        repGraph.addUser("Stack Overflow", 1288);
        add(repGraph);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, JSONException, ParameterNotSetException {
        // HttpClient.proxyServer = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("webproxy.int.westgroup.com", 80));
        new MainFrame();
    }
}
