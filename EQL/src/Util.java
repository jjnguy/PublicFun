import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.entities.Answer;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.query.AnswerQuery;

public class Util {

    public static final int MAX_PAGE_SIZE = 100;
    
    public static List<Answer> getAllAnswers(int userId, StackWrapper sw)
            throws IOException, JSONException, ParameterNotSetException {
        List<Answer> allAnswers = new ArrayList<Answer>();
        int page = 1;
        int pageSize = MAX_PAGE_SIZE;
        AnswerQuery query = (AnswerQuery) new AnswerQuery().setPage(page)
                .setPageSize(pageSize).setIds(userId);
        while (true) {
            List<Answer> newAnswers = sw.getAnswersByUserId(query);
            allAnswers.addAll(newAnswers);
            if (newAnswers.size() < pageSize)
                break;
        }
        return allAnswers;
    }
    

}
