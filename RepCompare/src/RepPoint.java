import java.util.ArrayList;
import java.util.List;

import net.sf.stackwrap4j.entities.Reputation;

import org.joda.time.DateTime;

public class RepPoint {
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