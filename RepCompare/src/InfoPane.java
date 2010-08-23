import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InfoPane extends JPanel {

    public InfoPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addInfo(Info info) {
        add(info);
        repaint();
    }
}
