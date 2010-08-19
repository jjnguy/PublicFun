import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.sf.stackwrap4j.entities.User;

public class KeyPanel extends JPanel {

    private List<UserColorPair> users;

    public KeyPanel() {
        users = new ArrayList<UserColorPair>();
        setPreferredSize(new Dimension(200, getPreferredSize().height));
        setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
    }

    public void addUser(User u, Color c) {
        users.add(new UserColorPair(u, c));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        g2.setFont(f);
        FontMetrics met = g.getFontMetrics();
        final int xGap = 3;
        final int yGap = 2;
        final int cubeSide = 4;
        int totalHeight = met.getHeight() * users.size() + yGap * (users.size() - 1);
        int height = (getHeight() - totalHeight) / 2;
        int maxWidth = 0;
        g2.setColor(Color.BLACK);
        for (UserColorPair u_c : users) {
            g2.setColor(u_c.color);
            g.fillRect(xGap, height, cubeSide, cubeSide);
            g2.setColor(Color.BLACK);
            g.drawString(u_c.user.getDisplayName(), xGap + cubeSide * 2, height + met.getHeight()
                    / 2);
            double strWidth = xGap * 6
                    + met.getStringBounds(u_c.user.getDisplayName(), g2).getWidth();
            maxWidth = Math.max(maxWidth, (int) (strWidth));
            height += yGap + met.getHeight();
        }
        if (maxWidth != 0)
            setPreferredSize(new Dimension(maxWidth, 400));
        validate();
    }
}
