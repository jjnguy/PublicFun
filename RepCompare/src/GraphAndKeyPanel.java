import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.json.JSONException;

public class GraphAndKeyPanel extends JPanel {

    private GraphPanel graph;
    private KeyPanel key;

    private int usersAdded;

    public GraphAndKeyPanel(ReputationGraph parent) throws IOException, JSONException {
        super(new BorderLayout());
        usersAdded = 0;
        graph = new GraphPanel(parent);
        key = new KeyPanel();
        add(graph);
        add(key, BorderLayout.EAST);
    }

    public void addUser(User u, String site, List<RepPoint> rep) {
        Color usersColor = decideColor();
        usersAdded++;
        key.addUser(u, site, usersColor);
        graph.addUser(u, site, rep, usersColor);
    }

    public void setKeyVisible(boolean visibility){
        key.setVisible(visibility);
    }
    
    private Color decideColor() {
        Color[] colors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.RED, Color.MAGENTA, Color.BLACK };
        return colors[usersAdded % colors.length];
    }
}
