import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import net.sf.stackwrap4j.entities.User;

public class GraphAndKeyPanel extends JPanel {

    private GraphPanel graph;
    private KeyPanel key;

    private int usersAdded;

    public GraphAndKeyPanel(ReputationGraph parent) {
        super(new BorderLayout());
        usersAdded = 0;
        graph = new GraphPanel(parent);
        key = new KeyPanel();
        add(graph);
        add(key, BorderLayout.EAST);
    }

    public void addUser(User u, List<RepPoint> rep) {
        Color usersColor = decideColor();
        usersAdded++;
        key.addUser(u, usersColor);
        graph.addUser(u, rep, usersColor);
    }

    private Color decideColor() {
        Color[] colors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.RED, Color.MAGENTA,
                Color.MAGENTA, Color.BLACK };
        return colors[usersAdded % colors.length];
    }

}
