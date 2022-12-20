package database;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class CollaborationDatabase {
    private static final Queue<String> reviewers = new LinkedList<>();

    public Session openSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        return session;
    }

    public void commitCloseSession(Session session, NativeQuery<?> query) {
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public Connection connect() throws SQLException {
        final String url = "jdbc:postgresql://localhost:5432/data_bot";
        final String user = "postgres";
        final String password = "qwerty";

        return DriverManager.getConnection(url, user, password);
    }

    public void deleteSender(String chatId) {
        Session session = openSession();
        NativeQuery<?> query;
        query = session.createSQLQuery("UPDATE reviewers SET chatid_sender = null WHERE chatid_sender = " + chatId);
        commitCloseSession(session, query);
    }

    public void insertToDatabase(String chatId) {
        Session session = openSession();
        NativeQuery<?> query;
        query = session.createSQLQuery("INSERT INTO reviewers (chatid_reviewer) SELECT (" + chatId + ") " +
                "WHERE NOT EXISTS(SELECT * FROM reviewers WHERE chatid_reviewer = " + chatId + ")");
        commitCloseSession(session, query);
        //добавляем в очередь получателя
        reviewers.offer(chatId);
    }

    public String forwardToReviewer(String sender) throws SQLException {
        Session session = openSession();
        NativeQuery<?> query;
        NativeQuery<?> query1;


        String potentialReviewer = reviewers.poll();
        reviewers.offer(potentialReviewer);
        if (sender.equals(potentialReviewer)) {
            while (true) {
                String newReviewer = reviewers.poll();
                reviewers.offer(newReviewer);
                if (selectSender("SELECT chatid_sender FROM reviewers " +
                        "WHERE chatid_reviewer = " + potentialReviewer) == null){
                    query1 = session.createSQLQuery("UPDATE reviewers SET chatid_sender = " + sender +
                    " WHERE chatid_reviewer = " + newReviewer + " AND chatid_sender is null");
                    commitCloseSession(session, query1);
                    return newReviewer;
                }
            }
        }
        query = session.createSQLQuery("UPDATE reviewers SET chatid_sender = " + sender +
                " WHERE chatid_reviewer = " + potentialReviewer + " AND chatid_sender is null");
        commitCloseSession(session, query);
        return potentialReviewer;
    }

    public String checkSender(String chatId) throws SQLException {
        String sender = selectSender("SELECT chatid_sender FROM reviewers " +
                "WHERE chatid_reviewer = " + chatId);

        if (sender != null) {
            deleteSender(sender);
            return sender;
        } else {
            return null;
        }
    }

    public String selectSender(String sql) throws SQLException{
        String sender = null;
        Connection connection = connect();
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while (result.next()) {
            sender = result.getString(1);
        }

        String res_sender = sender;
        result.close();
        stmt.close();
        connection.close();
        return res_sender;
    }
}
