import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.joda.time.DateTime;

import net.sf.stackwrap4j.entities.Reputation;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.query.ReputationQuery;

public class ReputationGraph extends JPanel {

    private StackWrapDataAccess data;
    private Map<Integer, User> users;
    private Map<Integer, List<Reputation>> userRep;
    private Map<Integer, List<RepPoint>> dailyPoints;

    private Color[] colors = new Color[] { Color.BLACK, Color.BLUE, Color.RED,
            Color.GREEN, Color.ORANGE, Color.CYAN, Color.MAGENTA };

    private int minRep = 0, maxRep = Integer.MIN_VALUE;
    private DateTime minDate = new DateTime(2060, 1, 1, 1, 1, 1, 1),
            maxDate = new DateTime(0);

    public ReputationGraph() throws IOException, JSONException {
        super();
        users = new HashMap<Integer, User>();
        userRep = new HashMap<Integer, List<Reputation>>();
        dailyPoints = new HashMap<Integer, List<RepPoint>>();
        data = new StackWrapDataAccess("RhtZB9-r0EKYJi-OjKSRUg");
        setBackground(Color.WHITE);
    }

    public void addUser(String site, int userId) throws JSONException,
            IOException, ParameterNotSetException {
        AddUserWorker worker = new AddUserWorker(userId, site);
        worker.execute();
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

    private List<RepPoint> calculateDailyPoints(int userId) {
        int maxRep = Integer.MIN_VALUE;
        int minRep = 0;
        DateTime minDate = new DateTime(2060, 1, 1, 1, 1, 1, 1);
        DateTime maxDate = new DateTime(0);
        List<RepPoint> points = new ArrayList<RepPoint>();
        int totalRep = 0;
        RepPoint currentDay = null;
        for (Reputation r : userRep.get(userId)) {
            currentDay = setCurrentDay(currentDay, r, points);
            currentDay.addRep(r);
            totalRep -= r.getNegativeRep();
            totalRep += r.getPositiveRep();
            maxRep = Math.max(totalRep, maxRep);
            minRep = Math.min(totalRep, minRep);
            minDate = minDate.isBefore(currentDay.getDate()) ? minDate
                    : currentDay.getDate();
            maxDate = maxDate.isAfter(currentDay.getDate()) ? maxDate
                    : currentDay.getDate();
        }
        this.maxRep = Math.max(maxRep, this.maxRep);
        this.minRep = Math.min(minRep, this.minRep);
        this.minDate = minDate.isBefore(this.minDate) ? minDate : this.minDate;
        this.maxDate = maxDate.isAfter(this.maxDate) ? maxDate : this.maxDate;
        return points;
    }

    private RepPoint setCurrentDay(RepPoint currentDay,
            Reputation currentPoint, List<RepPoint> points) {
        if (currentDay == null) {
            RepPoint newDay = new RepPoint(currentPoint.getOnDate() * 1000);
            points.add(newDay);
            return newDay;
        } else {
            DateTime dateInList = currentDay.getDate();
            DateTime dateLookingAt = new DateTime(
                    currentPoint.getOnDate() * 1000);
            if ((dateLookingAt.getMillis() - dateInList.getMillis()) > (60 * 60 * 24)) {
                RepPoint newDay = new RepPoint(currentPoint.getOnDate() * 1000);
                points.add(newDay);
                return newDay;
            } else {
                return currentDay;
            }
        }
    }

    private class RepPoint {
        private int repThisDay;
        private List<Reputation> reps;
        private DateTime date;

        public RepPoint(long start) {
            date = new DateTime(start);
            reps = new ArrayList<Reputation>();
            repThisDay = 0;
        }

        public void addRep(Reputation r) {
            reps.add(r);
            repThisDay += r.getPositiveRep();
            repThisDay -= r.getNegativeRep();
        }

        public DateTime getDate() {
            return date;
        }

        public int getRepToday() {
            return repThisDay;
        }
    }

    private class AddUserWorker extends SwingWorker<List<RepPoint>, Void> {

        private int userId;
        private String site;

        public AddUserWorker(int userId, String site) {
            this.userId = userId;
            this.site = site;
        }

        @Override
        protected List<RepPoint> doInBackground() throws Exception {
            User newU = data.getUser(site, userId);
            users.put(userId, newU);
            ReputationQuery query = new ReputationQuery();
            query.setPageSize(ReputationQuery.MAX_PAGE_SIZE);
            List<Reputation> userRepL = newU.getReputationInfo(query);
            Collections.sort(userRepL, new Comparator<Reputation>() {
                @Override
                public int compare(Reputation o1, Reputation o2) {
                    return (int) (o1.getOnDate() - o2.getOnDate());
                }
            });
            userRep.put(userId, userRepL);
            List<RepPoint> reps = calculateDailyPoints(userId);
            dailyPoints.put(userId, reps);
            return reps;
        }

        @Override
        protected void done() {
            super.done();
            ReputationGraph.this.repaint();
        }
    }
}
