import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JPanel;

import org.joda.time.DateTime;


public class GraphPanel extends JPanel {

    private ReputationGraph parent;
    private KeyPanel key;
    
    public GraphPanel(ReputationGraph parent){
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        paintRule(g2);
        int usernum = -1;
        for (int uId : users.keySet()) {
            usernum = ++usernum % colors.length;
            g2.setColor(colors[usernum]);
            List<RepPoint> points = dailyPoints.get(uId);
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
        g2.setColor(Color.LIGHT_GRAY);
        int repIncrement = calculateRepIncrement();
        for (int i = minRep; i <= maxRep; i += repIncrement) {
            int y = translateRepToYVal(i);
            g2.drawLine(0, y, getWidth(), y);
        }
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }
    

    private int translateRepToYVal(int rep) {
        if (rep > maxRep || rep < minRep)
            throw new IllegalArgumentException(
                    "Cannot translate rep out of range");
        int height = getHeight();
        int repDiff = maxRep - minRep;
        double scale = height / ((double) repDiff);
        int shiftedRep = minRep < 0 ? rep - minRep : rep;
        return height - (int) (shiftedRep * scale);
    }

    private int translateDateToXVal(DateTime date) {
        long dateDiff = maxDate.getMillis() - minDate.getMillis();
        int width = getWidth();
        double scale = width / ((double) dateDiff);
        long shiftedDate = date.getMillis() - minDate.getMillis();
        int translatedVal = (int) (shiftedDate * scale);
        return translatedVal;
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
