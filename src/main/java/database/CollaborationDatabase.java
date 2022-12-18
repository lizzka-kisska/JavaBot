package database;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.LinkedList;
import java.util.Queue;

public class CollaborationDatabase {
    private static final Queue<String> reviewers = new LinkedList<>();
    public Session openSession(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        return session;
    }
    public void commitCloseSession(Session session, NativeQuery<?> query){
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
//        HibernateUtil.shutdown();
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

    public String forwardToReviewer(String sender){
        Session session = openSession();
        NativeQuery<?> query;
        NativeQuery<?> query1;

        String reviewer = reviewers.poll();
        reviewers.offer(reviewer);
        if (sender.equals(reviewer)){
            String newReviewer =  reviewers.poll();
            reviewers.offer(newReviewer);
            query1 = session.createSQLQuery("UPDATE reviewers SET chatid_sender = " + sender +
                    " WHERE chatid_reviewer = " + newReviewer + " AND chatid_sender is null");
            commitCloseSession(session, query1);
            return newReviewer;
        }
        query = session.createSQLQuery("UPDATE reviewers SET chatid_sender = " + sender +
                " WHERE chatid_reviewer = " + reviewer + " AND chatid_sender is null");
        commitCloseSession(session, query);
        return reviewer;
    }

}
//    INSERT INTO reviewers (chatid_reviewer, chatid_sender) VALUES (" + chatId + ", null)
//UPDATE reviewers set chatid_sender = 3 WHERE chatid_reviewer = 1
//insert into reviewers (chatid_reviewer) select (1) where not exists(select * from reviewers where chatid_reviewer = 1)