import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import net.sf.stackwrap4j.entities.User;

import org.joda.time.DateTime;

public class GraphPanel extends JPanel {

    private final int Edge_Padding = 16;

    private int minRep = 0, maxRep = Integer.MIN_VALUE;
    private DateTime minDate = new DateTime(2060, 1, 1, 1, 1, 1, 1), maxDate = new DateTime(0);

    private List<UserColorPair> users;
    private Map<String, List<RepPoint>> idToRepPoints;

    public GraphPanel(ReputationGraph parent) {
        users = new ArrayList<UserColorPair>();
        idToRepPoints = new HashMap<String, List<RepPoint>>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 400));
    }

    public void addUser(User u, String site, List<RepPoint> reps, Color c) {
        users.add(new UserColorPair(u, site, c));
        idToRepPoints.put(site +":"+u.getId(), reps);
        repaint();
        int repSum = 0;
        for (RepPoint r : reps) {
            repSum += r.getRepToday();
            minRep = Math.min(minRep, repSum);
            maxRep = Math.max(maxRep, repSum);
            minDate = minDate.isBefore(r.getDate()) ? minDate : r.getDate();
            maxDate = maxDate.isBefore(r.getDate()) ? r.getDate() : maxDate;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("Paint a graph panel");
        Graphics2D g2 = (Graphics2D) g;
        paintRule(g2);
        for (UserColorPair u_c : users) {
            g2.setColor(u_c.color);
            List<RepPoint> points = idToRepPoints.get(u_c.site +":"+u_c.user.getId());
            if (points == null)
                continue;
            int totalRep = 0;
            for (RepPoint p : points) {
                totalRep += p.getRepToday();
                paintPoint(p, totalRep, g2);
            }
        }
    }

    private void paintPoint(RepPoint p, int totalRep, Graphics2D g2) {
        final int width = 4;
        final int height = 4;
        int y = translateRepToYVal(totalRep);
        int x = translateDateToXVal(p.getDate());
        g2.fillOval(x - width / 2, y - height / 2, width, height);
    }

    private void paintRule(Graphics2D g2) {
        Color saveColor = g2.getColor();
        Stroke saveStroke = g2.getStroke();
        paintXRule(g2);
        paintYRule(g2);
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }

    private void paintYRule(Graphics2D g2) {
        Color saveColor = g2.getColor();
        Stroke saveStroke = g2.getStroke();
        g2.setColor(Color.LIGHT_GRAY);
        int repIncrement = calculateRepIncrement();
        for (int i = minRep; i <= maxRep; i += repIncrement) {
            int y = translateRepToYVal(i);
            g2.setColor(Color.BLACK);
            g2.drawString("" + i, Edge_Padding / 2, y);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(Edge_Padding, y, getWidth() - Edge_Padding / 2, y);
        }
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }

    private void paintXRule(Graphics2D g2) {
        Color saveColor = g2.getColor();
        Stroke saveStroke = g2.getStroke();
        g2.setColor(Color.LIGHT_GRAY);
        long dateIncrement = calculateDateIncrement();
        for (long i = minDate.getMillis(); i <= maxDate.getMillis(); i += dateIncrement) {
            int x = translateDateToXVal(new DateTime(i));
            g2.setColor(Color.BLACK);
            g2.drawString(new DateTime(i).toString("MM-dd-YYYY"), x, getHeight()
                    - ((Edge_Padding / 2) - 3));
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, Edge_Padding, x, getHeight() - Edge_Padding);
        }
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }

    private long calculateDateIncrement() {
        long dateDiff = maxDate.getMillis() - minDate.getMillis();
        final double desiredNumberOfLines = 3.0;
        double dateIncrement = dateDiff / desiredNumberOfLines;
        return (long) dateIncrement;
    }

    private int translateRepToYVal(int rep) {
        int height = getHeight() - Edge_Padding * 2;
        int repDiff = maxRep - minRep;
        double scale = height / ((double) repDiff);
        int shiftedRep = minRep < 0 ? rep - minRep : rep;
        return (height - (int) (shiftedRep * scale)) + Edge_Padding;
    }

    private int translateDateToXVal(DateTime date) {
        long dateDiff = maxDate.getMillis() - minDate.getMillis();
        int width = getWidth() - Edge_Padding * 2;
        double scale = width / ((double) dateDiff);
        long shiftedDate = date.getMillis() - minDate.getMillis();
        int translatedVal = (int) (shiftedDate * scale);
        return translatedVal + Edge_Padding;
    }

    private int calculateRepIncrement() {
        int yDiff = maxRep - minRep;
        final double desiredNumberOfLines = 10.0;
        double repIncrement = yDiff / desiredNumberOfLines;
        if (repIncrement > 10000)
            return 20000;
        if (repIncrement > 5000)
            return 10000;
        if (repIncrement > 2500)
            return 5000;
        if (repIncrement > 1000)
            return 2500;
        if (repIncrement > 500)
            return 1000;
        if (repIncrement > 250)
            return 500;
        if (repIncrement > 100)
            return 250;
        if (repIncrement > 50)
            return 100;
        if (repIncrement > 25)
            return 50;
        return 10;
    }
}
