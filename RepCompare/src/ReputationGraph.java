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
    private Map<Integer, List<DailyRepPoint>> dailyPoints;

    private int minRep = 0, maxRep;
    private DateTime minDate = new DateTime(2060, 1, 1, 1, 1, 1, 1), maxDate = new DateTime(0);

    public ReputationGraph() throws IOException, JSONException {
        super();
        users = new HashMap<Integer, User>();
        userRep = new HashMap<Integer, List<Reputation>>();
        dailyPoints = new HashMap<Integer, List<DailyRepPoint>>();
        data = new StackWrapDataAccess();
        setBackground(Color.WHITE);
    }

    public void addUser(String site, int userId) throws JSONException, IOException,
            ParameterNotSetException {
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
        calculateDailyPoints(userId);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        paintRule(g2);
        List<DailyRepPoint> points = dailyPoints.get(2598);
        int totalRep = 0;
        for (DailyRepPoint p : points) {
            // System.out.println("Printing date: " + p.getDate());
            totalRep += p.getRepToday();
            paintPoint(p, totalRep, g2);
        }
    }

    private void paintPoint(DailyRepPoint p, int totalRep, Graphics2D g2) {
        int y = translateRepToYVal(totalRep);
        int x = translateDateToXVal(p.getDate());
        g2.fillOval(x, y, 5, 5);
    }

    private void paintRule(Graphics2D g2) {
        Color saveColor = g2.getColor();
        Stroke saveStroke = g2.getStroke();
        g2.setColor(Color.LIGHT_GRAY);
        int yIncrement = calculateYIncrement();
        for (int i = minRep; i <= maxRep; i += yIncrement) {
            int y = translateRepToYVal(i);
            g2.drawLine(0, y, getWidth(), y);
        }
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }

    private int translateRepToYVal(int rep) {
        if (rep > maxRep || rep < minRep)
            throw new IllegalArgumentException("Cannot translate rep out of range");
        int height = getHeight();
        int repDiff = maxRep - minRep;
        double scale = height / ((double) repDiff);
        int shiftedRep = minRep < 0 ? rep - minRep : rep;
        return (int) (shiftedRep * scale);
    }

    private int translateDateToXVal(DateTime date) {
        long dateDiff = maxDate.getMillis() - minDate.getMillis();
        int width = getWidth();
        double scale = width / ((double) dateDiff);
        long shiftedDate = date.getMillis() - minDate.getMillis();
        int translatedVal = (int) (shiftedDate * scale);
        return translatedVal;
    }

    private int calculateYIncrement() {
        int yDiff = maxRep - minRep;
        if (yDiff > 100000) {
            return 200000;
        } else if (yDiff > 50000) {
            return 25000;
        } else if (yDiff > 25000) {
            return 10000;
        } else if (yDiff > 10000) {
            return 5000;
        } else if (yDiff > 5000) {
            return 2500;
        } else if (yDiff > 2500) {
            return 1000;
        } else if (yDiff > 1000) {
            return 500;
        } else if (yDiff > 500) {
            return 250;
        } else if (yDiff > 250) {
            return 100;
        } else {
            return 50;
        }
    }

    private void calculateDailyPoints(int userId) {
        int maxRep = 0;
        int minRep = 1000000;
        DateTime minDate = new DateTime(2060, 1,1,1,1,1,1);
        DateTime maxDate = new DateTime(0);
        List<DailyRepPoint> points = new ArrayList<DailyRepPoint>();
        int totalRep = 0;
        DailyRepPoint currentDay = null;
        for (Reputation r : userRep.get(userId)) {
            currentDay = setCurrentDay(currentDay, r, points);
            currentDay.addRep(r);
            totalRep -= r.getNegativeRep();
            totalRep += r.getPositiveRep();
            maxRep = Math.max(totalRep, maxRep);
            minRep = Math.min(totalRep, minRep);
            minDate = minDate.isBefore(currentDay.getDate()) ? minDate : currentDay.getDate();
            maxDate = maxDate.isAfter(currentDay.getDate()) ? maxDate : currentDay.getDate();
        }
        dailyPoints.put(userId, points);
        this.maxRep = Math.max(maxRep, this.maxRep);
        this.minRep = Math.min(minRep, this.minRep);
        this.minDate = minDate.isBefore(this.minDate) ? minDate : this.minDate;
        this.maxDate = maxDate.isAfter(this.maxDate) ? maxDate : this.maxDate;
    }

    private DailyRepPoint setCurrentDay(DailyRepPoint currentDay, Reputation currentPoint,
            List<DailyRepPoint> points) {
        if (currentDay == null) {
            DailyRepPoint newDay = new DailyRepPoint(currentPoint.getOnDate() * 1000);
            points.add(newDay);
            return newDay;
        } else {
            DateTime dateInList = currentDay.getDate();
            DateTime dateLookingAt = new DateTime(currentPoint.getOnDate() * 1000);
            if (dateInList.getDayOfYear() != dateLookingAt.getDayOfYear()) {
                DailyRepPoint newDay = new DailyRepPoint(currentPoint.getOnDate() * 1000);
                points.add(newDay);
                return newDay;
            } else {
                return currentDay;
            }
        }
    }

    private class DailyRepPoint {
        private int repThisDay;
        private List<Reputation> reps;
        private DateTime date;

        public DailyRepPoint(long start) {
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

        public boolean hitCap() {
            return repThisDay >= 200;
        }

        public boolean passedCap() {
            return repThisDay > 200;
        }
    }
}
