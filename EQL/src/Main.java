import java.io.IOException;
import java.util.List;

import net.sf.stackwrap4j.StackOverflow;
import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.entities.Answer;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.query.AnswerQuery;

public class Main {
    public static void main(String[] args) throws JSONException, IOException,
            ParameterNotSetException {
        StackWrapper sw = new StackOverflow("RhtZB9-r0EKYJi-OjKSRUg");
        System.out.println(getAnswersPerDay(sw, 2598));
    }

    /**
     * Finds the average answers posted per day since the first answer
     * 
     * @return answers per day
     * @throws ParameterNotSetException
     * @throws JSONException
     * @throws IOException
     */
    public static double getAnswersPerDay(StackWrapper sw, int userId) throws IOException,
            JSONException, ParameterNotSetException {
        AnswerQuery query = new AnswerQuery();
        int page = 1;
        int pageSize = 50;
        query.setPage(page).setPageSize(pageSize).setIds(userId);
        long firstAnswer = Long.MAX_VALUE;
        long lastAnswer = Long.MIN_VALUE;
        int answerCount = 0;
        while (true) {
            List<Answer> answers = sw.getAnswersByUserId(query);
            answerCount += answers.size();
            for (Answer a : answers) {
                if (a.getCreationDate() < firstAnswer)
                    firstAnswer = a.getCreationDate();
                if (a.getCreationDate() > lastAnswer) 
                    lastAnswer = a.getCreationDate();
            }
            if (answers.size() < pageSize)
                break;
            query.setPage(++page);
        }
        long answerRange = lastAnswer - firstAnswer;
        double days = answerRange / 60 / 60 / (double)24;
        return answerCount / days;
    }

    public static double getAverageAnswerScore(StackWrapper sw, int userId) throws JSONException,
            IOException, ParameterNotSetException {
        AnswerQuery query = new AnswerQuery();
        int page = 1;
        int pageSize = 50;
        query.setPage(page).setPageSize(pageSize).setIds(userId);
        int voteSum = 0;
        int answerCount = 0;
        while (true) {
            List<Answer> answers = sw.getAnswersByUserId(query);
            for (Answer a : answers) {
                answerCount++;
                voteSum += a.getScore();
            }
            if (answers.size() < pageSize)
                break;
            query.setPage(++page);
        }
        return voteSum / (double) answerCount;
    }

    public static double getAcceptedAnswerPercentage(StackWrapper sw, int userId)
            throws JSONException, IOException, ParameterNotSetException {
        AnswerQuery query = new AnswerQuery();
        int page = 1;
        int pageSize = 50;
        query.setPage(page).setPageSize(pageSize).setIds(userId);
        int acceptedCount = 0;
        int answerCount = 0;
        while (true) {
            List<Answer> answers = sw.getAnswersByUserId(query);
            for (Answer a : answers) {
                answerCount++;
                if (a.isIsAccepted())
                    acceptedCount++;
            }
            if (answers.size() < pageSize)
                break;
            query.setPage(++page);
        }
        return acceptedCount / (double) answerCount;
    }
}
