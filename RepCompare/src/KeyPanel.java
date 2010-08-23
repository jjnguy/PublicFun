import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
        setPreferredSize(new Dimension(200, getPreferredSize().height));
        setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
    }

    public void addUser(User u, String site, Color c) {
        users.add(new UserColorPair(u, site, c));
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
        int totalHeight = 30 * users.size() + yGap * (users.size() - 1);
        int height = (getHeight() - totalHeight) / 2;
        int maxWidth = 0;
        g2.setColor(Color.BLACK);
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
        if (maxWidth != 0)
            setPreferredSize(new Dimension(maxWidth, 400));
        getParent().validate();
        validate();
    }
}
