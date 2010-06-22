import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import net.sf.stackwrap4j.StackOverflow;
import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.datastructures.AnswerAutoFetchList;
import net.sf.stackwrap4j.entities.Answer;
import net.sf.stackwrap4j.entities.Question;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.exceptions.ParameterNotSetException;
import net.sf.stackwrap4j.http.HttpClient;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.query.AnswerQuery;
import net.sf.stackwrap4j.query.QuestionQuery;

public class Main {
    public static void main(String[] args) throws JSONException, IOException,
            ParameterNotSetException {
        Proxy prox = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                "webproxy.int.westgroup.com", 80));
        HttpClient.proxyServer = prox;
        StackWrapper sw = new StackOverflow("RhtZB9-r0EKYJi-OjKSRUg");
        testAutoFetchList(sw);
    }

    private static void testAutoFetchList(StackWrapper sw) {
        List<Answer> answers = new AnswerAutoFetchList(sw,
                (AnswerQuery) new AnswerQuery().setIds(4));
        for (Answer a: answers){
            System.out.println(a.getQuestionId());
        }
    }

    /**
     * Finds the average answers posted per day since the first answer
     * 
     * @return answers per day
     * @throws ParameterNotSetException
     * @throws JSONException
     * @throws IOException
     */
    public static double getAnswersPerDay(StackWrapper sw, int userId)
            throws IOException, JSONException, ParameterNotSetException {
        long firstAnswer = Long.MAX_VALUE;
        long lastAnswer = Long.MIN_VALUE;
        int answerCount = 0;
        List<Answer> answers = Util.getAllAnswers(userId, sw);
        answerCount += answers.size();
        for (Answer a : answers) {
            if (a.getCreationDate() < firstAnswer)
                firstAnswer = a.getCreationDate();
            if (a.getCreationDate() > lastAnswer)
                lastAnswer = a.getCreationDate();
        }
        long answerRange = lastAnswer - firstAnswer;
        double days = answerRange / 60 / 60 / (double) 24;
        return answerCount / days;
    }

    public static double getAverageAnswerScore(StackWrapper sw, int userId)
            throws JSONException, IOException, ParameterNotSetException {
        int voteSum = 0;
        int answerCount = 0;
        List<Answer> answers = Util.getAllAnswers(userId, sw);
        for (Answer a : answers) {
            answerCount++;
            voteSum += a.getScore();
        }
        return voteSum / (double) answerCount;
    }

    public static double getAcceptedAnswerPercentage(StackWrapper sw, int userId)
            throws JSONException, IOException, ParameterNotSetException {
        int acceptedCount = 0;
        int answerCount = 0;
        List<Answer> answers = Util.getAllAnswers(userId, sw);
        for (Answer a : answers) {
            answerCount++;
            if (a.isIsAccepted())
                acceptedCount++;
        }
        return acceptedCount / (double) answerCount;
    }

    public static List<StatsEntry> replicateStatsPage(String tag,
            StackWrapper sw) throws IOException, JSONException,
            ParameterNotSetException {

        long _30Days = 60L * 60L * 24L * 30L;
        long currentSeconds = System.currentTimeMillis() / 1000L;
        long seconds30DaysAgo = currentSeconds - _30Days;
        int page = 1;
        QuestionQuery qQuery = (QuestionQuery) new QuestionQuery()
                .setPage(page).setPageSize(Util.MAX_PAGE_SIZE).setFromDate(
                        seconds30DaysAgo).setToDate(currentSeconds).setTags(
                        "java");
        List<Question> allQuestionsTagged = new ArrayList<Question>();
        while (true) {
            System.out.println("Retrieving page " + page);
            List<Question> newQ = sw.listQuestions(qQuery);
            allQuestionsTagged.addAll(newQ);
            if (allQuestionsTagged.size() < Util.MAX_PAGE_SIZE)
                break;
            qQuery.setPage(++page);
        }
        System.out.println(allQuestionsTagged.size());
        List<Answer> allAnswersTagged = new ArrayList<Answer>();
        page = 1;
        for (Question q : allQuestionsTagged)
            allAnswersTagged.addAll(q.getAnswers());
        Map<Integer, StatsEntry> allUsers = new HashMap<Integer, StatsEntry>();
        for (Answer a : allAnswersTagged) {
            if (a.isCommunityOwned())
                continue;
            StatsEntry s = allUsers.get(a.getOwnerId());
            if (s == null) {
                s = new StatsEntry(a.getOwnerId());
                allUsers.put(s.uId, s);
            }
            s.addAnswer(a.getScore());
        }
        List<StatsEntry> ret = new ArrayList<StatsEntry>();
        ret.addAll(allUsers.values());
        Collections.sort(ret);
        return ret;
    }

    public static class StatsEntry implements Comparable<StatsEntry> {
        private int uId;
        private int votes;
        private int answers;

        public StatsEntry(int uId) {
            this.uId = uId;
            votes = 0;
            answers = 0;
        }

        public void addAnswer(int score) {
            answers++;
            votes += score;
        }

        @Override
        public int compareTo(StatsEntry arg0) {
            return this.votes - arg0.votes;
        }

        @Override
        public String toString() {
            return "" + uId + " " + votes + " " + answers;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + answers;
            result = prime * result + uId;
            result = prime * result + votes;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            StatsEntry other = (StatsEntry) obj;
            if (answers != other.answers)
                return false;
            if (uId != other.uId)
                return false;
            if (votes != other.votes)
                return false;
            return true;
        }
    }
}
