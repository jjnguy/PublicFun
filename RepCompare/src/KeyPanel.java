import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.json.JSONException;

public class KeyPanel extends JPanel {

    private List<UserColorPair> users;

    private StackWrapDataAccess data;

    public KeyPanel() throws IOException, JSONException {
        users = new ArrayList<UserColorPair>();
        data = new StackWrapDataAccess(StackWrapDataAccess.Key);
        setPreferredSize(new Dimension(200, 400));
        setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
    }

    public void addUser(User u, String site, Color c) {
        users.add(new UserColorPair(u, site, c));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Title Stuff
        Graphics2D g2 = (Graphics2D) g;
        Font titleFont = new Font(Font.SERIF, Font.BOLD, 18);
        g2.setFont(titleFont);
        // Setup for the rest
        FontMetrics met = g.getFontMetrics();
        int titleHeight = met.getHeight();
        int titleWidth = met.stringWidth("Key");
        g2.drawString("Key", (getWidth() / 2) - (titleWidth / 2), titleHeight + 5);
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        g2.setFont(f);
        met = g.getFontMetrics();
        final int xGap = 3;
        final int yGap = 2;
        final int cubeSide = 4;
        int totalHeight = 30 * users.size() + yGap * (users.size() - 1);
        int height = (getHeight() - totalHeight) / 2;
        int maxWidth = 0;
        g2.setColor(Color.BLACK);
        // iterate through all users
        for (UserColorPair u_c : users) {
            g2.setColor(u_c.color);
            g.fillRect(xGap, height, cubeSide, cubeSide);
            g2.setColor(Color.BLACK);
            g.drawString(u_c.user.getDisplayName(), xGap + cubeSide * 2, height + met.getHeight()
                    / 2);
            double strWidth = xGap * 5
                    + met.getStringBounds(u_c.user.getDisplayName(), g2).getWidth();
            g.drawImage(data.getIconBySite(u_c.site), (int) (strWidth), height - 15, 30, 30, null);
            maxWidth = Math.max(maxWidth, (int) (strWidth + 30 + 5));
            height += yGap + 30;
        }
        // reset the width
        if (maxWidth != 0)
            setPreferredSize(new Dimension(maxWidth, getParent().getHeight()));
    }
}
