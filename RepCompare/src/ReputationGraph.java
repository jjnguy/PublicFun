import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.sf.stackwrap4j.entities.Reputation;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.query.ReputationQuery;

import org.joda.time.DateTime;

/**
 * I don't want this class to know anything about color graphics, poitns, or anything like that.
 * 
 * @author u0117691
 * 
 */
public class ReputationGraph extends JPanel {

    private GraphAndKeyPanel graph;
    private InfoPane info;

    private StackWrapDataAccess data;
    private Map<String, User> users;
    private Map<String, List<Reputation>> userRep;

    public ReputationGraph() throws IOException, JSONException {
        super(new BorderLayout());
        users = new HashMap<String, User>();
        userRep = new HashMap<String, List<Reputation>>();
        data = new StackWrapDataAccess(StackWrapDataAccess.Key);
        graph = new GraphAndKeyPanel(this);
        info = new InfoPane();
        add(graph);
        add(info, BorderLayout.SOUTH);
    }

    public void addUser(String site, int userId) throws JSONException, IOException,
            ParameterNotSetException {
        AddUserWorker worker = new AddUserWorker(userId, site);
        worker.execute();
    }
    
    public void setKeyVisibility(boolean visibility){
        graph.setKeyVisible(visibility);
    }

    private RepPoint setCurrentDay(RepPoint currentDay, Reputation currentPoint,
            List<RepPoint> points) {
        if (currentDay == null) {
            RepPoint newDay = new RepPoint(currentPoint.getOnDate() * 1000);
            points.add(newDay);
            return newDay;
        } else {
            DateTime dateInList = currentDay.getDate();
            DateTime dateLookingAt = new DateTime(currentPoint.getOnDate() * 1000);
            if ((dateLookingAt.getMillis() - dateInList.getMillis()) > (60 * 60 * 24)) {
                RepPoint newDay = new RepPoint(currentPoint.getOnDate() * 1000);
                points.add(newDay);
                return newDay;
            } else {
                return currentDay;
            }
        }
    }

    private class AddUserWorker extends SwingWorker<List<RepPoint>, Double> {

        private int userId;
        private String site;
        private AddUserInfo addInfo;

        public AddUserWorker(int userId, String site) throws JSONException, IOException {
            this.userId = userId;
            this.site = site;
            User newU = data.getUser(site, userId);
            users.put(site + ":" + userId, newU);
            addInfo = new AddUserInfo(users.get(site + ":" + userId).getDisplayName());
            info.addInfo(addInfo);
            invalidate();
            ReputationGraph.this.repaint();
            invalidate();
            validate();
        }

        @Override
        protected List<RepPoint> doInBackground() throws Exception {
            ReputationQuery query = new ReputationQuery();
            query.setPageSize(ReputationQuery.MAX_PAGE_SIZE);
            List<Reputation> userRepL = users.get(site + ":" + userId).getReputationInfo(query);
            Collections.sort(userRepL, new Comparator<Reputation>() {
                @Override
                public int compare(Reputation o1, Reputation o2) {
                    return (int) (o1.getOnDate() - o2.getOnDate());
                }
            });
            userRep.put(site + ":" + userId, userRepL);
            List<RepPoint> reps = calculateDailyPoints(userId);
            return reps;
        }

        @Override
        protected void process(List<Double> chunks) {
            System.out.println("Processing: " + chunks.get(0));
            addInfo.setProgress((int) (chunks.get(0) * 100));
        }

        @Override
        protected void done() {
            super.done();
            try {
                graph.addUser(users.get(site + ":" + userId), site, this.get());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            info.remove(addInfo);
            invalidate();
            ReputationGraph.this.repaint();
            invalidate();
            validate();
        }

        private List<RepPoint> calculateDailyPoints(int userId) {
            List<RepPoint> points = new ArrayList<RepPoint>();
            int totalRep = 0;
            RepPoint currentDay = null;
            int numComplete = 0;
            for (Reputation r : userRep.get(site + ":" + userId)) {
                double completed = numComplete++ / (double) userRep.get(site + ":" + userId).size();
                publish(completed);
                currentDay = setCurrentDay(currentDay, r, points);
                currentDay.addRep(r);
                totalRep -= r.getNegativeRep();
                totalRep += r.getPositiveRep();
            }
            return points;
        }
    }
}
