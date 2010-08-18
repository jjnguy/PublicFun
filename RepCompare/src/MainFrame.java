import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.json.JSONException;

public class MainFrame extends JFrame {

    private ReputationGraph repGraph;

    public MainFrame() throws IOException, JSONException, ParameterNotSetException {
        super("Reputation Graph Compare");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repGraph = new ReputationGraph();
        repGraph.setPreferredSize(new Dimension(400, 400));
        repGraph.addUser("Stack Overflow", 2598);
        add(repGraph);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, JSONException, ParameterNotSetException {
        new MainFrame();
    }
}
